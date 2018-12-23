package com.therxpx.utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.UpdateConflictException;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.therxpx.relaxedlogin.models.AppConfig;
import com.therxpx.relaxedlogin.models.AppRole;
import com.therxpx.relaxedlogin.models.NewUser;
import com.therxpx.relaxedlogin.models.Role;
import com.therxpx.relaxedlogin.models.StatusForm;
import com.therxpx.relaxedlogin.models.Team;
import com.therxpx.relaxedlogin.models.User;

public class CouchDb {
	private String userDB = "_users";
	private final String STATUSUPDATES = "statusupdates";
	private final String TEAMDB = "teams";
	private HttpClient httpClient;
	private CouchDbInstance dbInstance;
	private CouchDbConnector db;
	private URI url;

	public CouchDb() {
		// Empty Constructor
	}
	
	/**
	 * Provide database name to automatically connect to the supplied db on localhost when instantiating
	 * @param dbName
	 */
	public CouchDb(String dbName) {
		this.connect(dbName, true);
	}
	
	
	/** Create HTTP Client, instance and connect to given database name, set create to true to create database
	 * if it does not exist.
	 * @param dbName Database Name to connect / create
	 * @param create Create database if it doesn't exist
	 */
	public void connect(String dbName, boolean create) {

		try {
			httpClient = new StdHttpClient.Builder()
					.url("http://admin:admin@localhost:5984")
					.build();
		} catch (MalformedURLException e) {
			System.out.println("Could not connect to database, malformed URL: ");
			System.out.println(e.getLocalizedMessage() );
		}
		dbInstance = new StdCouchDbInstance(httpClient);
		db = dbInstance.createConnector(dbName, create);
		
		//System.out.println("Connected to CouchDB Server, on DB: " + dbName);

	}
	
	/*public void testUser() {
		User user = db.get(User.class, "org.couchdb.user:carl");
		
		System.out.println("User Derived Key: " + user.getDerived_key() );
	}*/
	
	/** Get all users in the _users database
	 * 
	 * @return List<User>
	 */
	public List<User> getUsers() {
		this.connect(userDB, false);
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/byName")
				.viewName("byName");
	
		return db.queryView(query, User.class);
	}
	
	/**
	 * Give user name, method appends the prefix to the user name automatically to query Couch with this ID.
	 * @param username
	 * @return User class
	 */
	public User getUser(String username) {
		User user = new User();
		String id = user.getPrefix() + username;
		
		this.connect(userDB, false);
		
		user = db.get(User.class, id);
		
		user = new UserAuthority().setUserAdmin(user);
		
		//System.out.println("   User is admin: " + user.isAdmin() );
		
		return user;
		
	}
	
	/**
	 * Updates user by ID on the _users database, then performs get on updated user.
	 * @param user
	 * @return User updated
	 */
	public User updateUser(User user) {
		this.connect(userDB, false);
		
		User oldUser = this.db.get(User.class, user.getId());
		
		user.setDerived_key( oldUser.getDerived_key() );
		user.setSalt( oldUser.getSalt() );
		user.setIterations( oldUser.getIterations() );
		user.setPassword_scheme(oldUser.getPassword_scheme()); 
		
		if (user.getRoles() == null || user.getRoles().size() == 0) {
			List<String> blankRole = new ArrayList<String>();
			user.setRoles(blankRole);
		}
		
		db.update(user);
		
		//Update user from DB, reset admin authority
		user = this.getUser(user.getName() );
		
		//Try to update security of user logged in
		SecurityContextHolder.getContext().setAuthentication( new UsernamePasswordAuthenticationToken(user, user.getDerived_key(), user.getAuthorities() ) );
		
		
		return user;
	}
	
	/**
	 * Gets currently logged in user name, then calls the getUser(String username) method
	 * @return User class object populated
	 */
	public User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//String currentPrincipalName = authentication.getName();
		
		//return this.getUser(currentPrincipalName);
		
		return (User) authentication.getPrincipal();
		
	}

	/**
	 * Formulates the localhost:5984/_session url
	 * @return URI object
	 */
	public URI getUrl() {
		try {
			this.url = new URI("http://localhost:5984/_session");
		} catch (URISyntaxException e) {
			System.out.println("Error: " + e.getLocalizedMessage() );
		}
		
		return url;
	}
	
	/**
	 * Creates a new team in the "Teams" database
	 * @param team
	 */
	public Team addTeam(Team team) {
		this.connect(TEAMDB, true);
		
		db.create(team);
		
		//System.out.println("  Team created: " + team.getId() );
		
		return team;
	}
	
	/**
	 * Gets all teams in the Teams database
	 * @return List<Team>
	 */
	public List<Team> getTeams() {
		this.connect(TEAMDB, true);
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/byName")
				.viewName("byName");
	
		List<Team> teams = db.queryView(query, Team.class);
		
		return teams;
	}
	
	/**
	 * Gets one team in the Team object
	 * @param id
	 * @return Team object
	 */
	public Team getTeam(String id) {
		this.connect(TEAMDB, false);
		
		return db.get(Team.class, id);
	}
	
	/**
	 * Calls the update command on the given Team object, returns updated team object. Connects to team db
	 * @param team
	 * @return Team object
	 */
	public Team updateTeam(Team team) {
		this.connect(TEAMDB, false);
		
		db.update(team);
		
		return db.get(Team.class, team.getId());
	}
	
	/**
	 * Given the NewUser class, creates CouchDB user in the _user database through manual "PUT" command
	 * @param user
	 */
	public void createUser(NewUser user) {
		User usr = new User();
		
		String id = usr.getPrefix() + user.getName() ;
		
		//System.out.println( user.toJson() );
		
		try {
			URL url = new URL("http://admin:admin!@localhost:5984/_users/" + id );
			
			//System.out.println( url.toString() );
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();			
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream outStream = conn.getOutputStream();
			OutputStreamWriter outStreamWriter = new OutputStreamWriter( outStream, "UTF-8" );
			
			outStreamWriter.write( user.toJson() );
			outStreamWriter.flush();
			outStreamWriter.close();
			outStream.close();
			
			conn.connect();
			
			//System.out.println( conn.getResponseCode() );
			//System.out.println( conn.getResponseMessage() );
			
			//read the inputstream and print it
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result2 = bis.read();
			while(result2 != -1) {
			    buf.write((byte) result2);
			    result2 = bis.read();
			}//end while
			//String result = buf.toString();
			//System.out.println(result);

		} catch (IOException e) {
			System.out.println("Could not connect: " + e.getLocalizedMessage() );
			//e.printStackTrace();
		}//End Try catch
		
	}//createUser()
	
	/**
	 * Creates a new status update and returns a populated StatusForm object
	 * @param sf
	 * @return StatusForm
	 */
	public StatusForm addStatus(StatusForm sf) {
		this.connect("StatusUpdates", true);
		
		if( sf.getId().isEmpty() ) {
			sf.setId(null);
			sf.setRev(null);
			this.db.create(sf);
		} else {
			this.db.update(sf);
		}
		return sf;
	}
	
	/**
	 * Gets all status in the statusupdates database
	 * @return List<StatusForm>
	 */
	public List<StatusForm> getStatuses() {
		this.connect("statusupdates", true);
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/index")
				.viewName("byUsername");
	
		List<StatusForm> statuses = db.queryView(query, StatusForm.class);
		
		return statuses;
	}
	
	/**
	 * Gets all statuses for a given username in the statusupdate database
	 * @param username
	 * @return List<StatusForm>
	 */
	public List<StatusForm> getStatuses(String username){
		this.connect("statusupdates", true);
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/index")
				.viewName("byUsername")
				.key(username);
	
		List<StatusForm> statuses = db.queryView(query, StatusForm.class);
		
		/* Sort by date (old to new) */
		Collections.sort(statuses);
		
		/* Reverse these to show newest at top */
		Collections.reverse(statuses);
		
		return statuses;
	}
	
	/**
	 * Gets a status with the given id
	 * @param id
	 * @return StatusForm
	 */
	public StatusForm getStatus(String id) {
		this.connect(STATUSUPDATES, true);
		
		return this.db.get(StatusForm.class, id);
	}
	
	/**
	 * Connects to TEAMDB, returns a List of strings of teams the given user is a part of.
	 * @param username
	 * @return List of String
	 */
	public List<String> getUserTeams(String username) {
		this.connect(TEAMDB, false);
		
		ComplexKey startKey = ComplexKey.of(username);
		ComplexKey endKey = ComplexKey.of(username,  ComplexKey.emptyObject() );
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/members")
				.viewName("byMemberRole")
				//.group(true)
				.startKey(startKey)
				.endKey(endKey);
		
		ViewResult r = db.queryView(query);
		List<Row> rows = r.getRows();
		
		List<String> userTeams = new ArrayList<String>();
		
		for(Row row : rows) {
			//System.out.println( row.toString() );
			String team = row.getValue();
			userTeams.add(team);
		}

		return userTeams;
		
	}
	
	/**
	 * Connects to TEAMDB, returns List of String of teams where the current authenticated user has the Admin role of the team
	 * @return List of String
	 */
	public List<Team> getUserAdminTeams() {
		User user = this.getUser();
		
		this.connect(TEAMDB, false);
		
		ComplexKey startKey = ComplexKey.of(user.getName(), new Team().getAdmin() );
		ComplexKey endKey = ComplexKey.of(user.getName(), new Team().getAdmin() );
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/members")
				.viewName("byMemberRole")
				//.group(true)
				.includeDocs(true)
				.startKey(startKey)
				.endKey(endKey);
		
		List<Team> teams = db.queryView(query, Team.class);
		
		return teams;
	}
	
	/**
	 * Gets List of status by username and team, leveraging a view called byUserTeam created in couch
	 * @param username
	 * @param team
	 * @return List<StatusForm>
	 */
	public List<StatusForm> getStatusbyUserTeam(String username, String team){
		this.connect(STATUSUPDATES, false);
		
		List<String> keyList = Arrays.asList(username, team);
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/index")
				.viewName("byUserTeam")
				.key(keyList);
		
		List<StatusForm> statuses = db.queryView(query, StatusForm.class);
		
		Collections.sort(statuses);
		Collections.reverse(statuses);
		
		return statuses;
	}
	
	/**
	 * Send statusForm object (fully populated) to delete this from the DB. This automatically connects to correct DB
	 * @param statusForm
	 */
	public void deleteStatus(StatusForm statusForm) {
		this.connect(STATUSUPDATES, false);
		
		try {
		db.delete(statusForm);
		} catch (UpdateConflictException e) {
			System.out.println("Could not delete status" );
			System.out.println(e.getLocalizedMessage() );
		}
	}
	
	/**
	 * Deletes from the teams database, auto connects to teams db. Pass ID string, this populates the string.
	 * @param id
	 */
	public void deleteTeam(String id) {
		this.connect(TEAMDB, false);
		
		Team team = this.getTeam(id);
		
		if( team.getId().isEmpty() ) {
			//Do nothing
		} else {
			try {
				db.delete(team);
			} catch (UpdateConflictException e) {
				System.out.println("   Error deleting: " + e.getLocalizedMessage() );
			}
		}
	}
	
	/**
	 * Gets all roles from the teams database. Automatically connects to db.
	 * @return List<Role>
	 */
	public List<Role> getRoles(){
		this.connect(TEAMDB, false);
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/roles")
				.viewName("byRole")
				.includeDocs(true);
		
		List<Role> roles = db.queryView(query, Role.class);
		
		return roles;
	}
	
	/**
	 * Connect to TEAMDB, create given role.
	 * @param role
	 * @return
	 */
	public Role createRole(Role role) {
		this.connect(TEAMDB, false);
		
		db.create(role);
		
		//System.out.println("   Created Role: " + role.getId());
		
		return role;
	}
	
	/**
	 * Connect to team TEAMDB and retrieve the role with the given ID
	 * @param id
	 * @return Populated Role class, if found
	 */
	public Role getRole(String id) {
		this.connect(TEAMDB, false);
		
		return this.db.get(Role.class, id);
	}
	
	/**
	 * Connect to TEAMDB and delete the role with the given ID
	 * @param id
	 */
	public void deleteRole(String id) {
		this.connect(TEAMDB, false);
		
		Role role = this.getRole(id);
		
		this.db.delete(role);
	}
	
	/**
	 * Perform search on STATUSUPDATES db for the given user, using httpurlconnection post call. Leverages special index.
	 * @param searchTerm
	 * @param username
	 * @return
	 */
	public List<StatusForm> searchStatus(String searchTerm, String username) {
		//System.out.println("Performing search: ");
		//this.connect(STATUSUPDATES, false);
		
		//curl -H 'Content-Type: application/json' -X POST 'http://admin:admin@localhost:5984/statusupdates/_index' -d '{"index": {"fields": ["statusText", "username"]}, "name":"statusText-index","type": "json","ddoc" : "indexByTextAndUser"}'
		
		String designDoc = "_design/indexByTextAndUser";
		
		String searchPhrase = "{ \"selector\" : {\n" + 
				"	\"statusText\" : {\"$regex\": \"(?i)" + searchTerm+ "\"},\n"
				+ "	\"username\" : {\"$eq\" : \"" + username +  "\"} \n" + 
				"  },\n"
				+ "\"use_index\": [\"" + designDoc + "\", \"statusText-index\"]}";
		
		try {
			URL url = new URL("http://admin:admin!@localhost:5984/"+STATUSUPDATES + "/_find" );
			
			//System.out.println( url.toString() );
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();			
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream outStream = conn.getOutputStream();
			OutputStreamWriter outStreamWriter = new OutputStreamWriter( outStream, "UTF-8" );
			
			//System.out.println(searchPhrase);
			outStreamWriter.write( searchPhrase );
			outStreamWriter.flush();
			outStreamWriter.close();
			outStream.close();
			
			conn.connect();
			
			//System.out.println( conn.getResponseCode() );
			//System.out.println( conn.getResponseMessage() );
			
			//read the inputstream and print it
			String result;
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result2 = bis.read();
			while(result2 != -1) {
			    buf.write((byte) result2);
			    result2 = bis.read();
			}//end while
			
			result = buf.toString();
			//System.out.println(result);
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(result);
			
			JsonNode docs = root.path("docs");

			String docsString = docs.toString();
			
			if(docsString.length() > 0) {
				List<StatusForm> statuses = mapper.readValue(docsString, new TypeReference<List<StatusForm>>(){});
				return statuses;
			}

		} catch (IOException e) {
			System.out.println("Could not connect: " + e.getLocalizedMessage() );
			//e.printStackTrace();
		}//End Try catch
		
		return null;
		
		/**
		 * 
		 * http://localhost:5984/statusupdates/_find
		 * { "selector" : {
			"statusText" : {"$regex": "(?i)Keep"}	
				}
			}
		 */
	}
	
	/** 
	 * Generic creation object. Should call connect of this same db object before creating. Pass in any object to create it.
	 * @param o
	 * @return
	 */
	public Object create(Object o) {
		this.db.create(o);
		
		return o;
	}
	
	
	/** 
	 * Generic update. SHould call connect with this same db object before updating.
	 * @param o
	 * @return
	 */
	public Object update(Object o) {
		this.db.update(o);
		
		return o;
	}
	
	/**
	 * Connects to the AppConfig db, as defined in the AppConfig model, returns the populated AppConfig object
	 * @return AppConfig
	 */
	public AppConfig getConfig() {
		this.connect(AppConfig.getAppconfigdb(), true);
		
		ViewQuery query = new ViewQuery()
				.designDocId("_design/index")
				.viewName("config")
				.includeDocs(true);
		
		List<AppConfig> config = db.queryView(query, AppConfig.class);
		
		if (config.isEmpty()) {
			System.out.println("  No config found, creating new object" );
			AppConfig newConfig = new AppConfig();
			AppRole blankRole = new AppRole("", "");
			
			newConfig.setAdminRole(blankRole);
			newConfig.setId("");
			newConfig.setRev("");

			return newConfig;
		} else {
			return config.get(0);
		}
	}
}//CouchDb class

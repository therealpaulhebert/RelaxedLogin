/*
Views are identified in the following format:
<db>/_design/<designDocId>/_view/<viewName>
*/

/*
_users/_design/byName/_view/byName
*/
function (doc) {
  if(doc.name){
    emit(doc.name, doc);
  }
}

/*
appconfiguration/_design/index/_view/config
*/
function (doc) {
  if(doc.type && doc.type=="config") {
    emit(doc._id, 1);
  }
}

/*
statusupdates/_design/index/UserTeams
*/
function (doc) {
  if(doc.username && doc.team){
    emit( [doc.username, doc.team], 1);
  }
}

/*
This is probably not used. A real _index is used in this ones place
statusupdates/_design/index/byStatusText
*/
function (doc) {
  if(doc.type && doc.type=="statusUpdate") {
    emit(doc.statusText, 1)
  }
}

/*
statusupdates/_design/index/byUserTeam
*/
function (doc) {
  if(doc.username && doc.team){
    emit( [doc.username, doc.team], doc);
  }
}

/*
statusupdates/_design/index/byUsername
*/
function (doc) {
  if(doc.username && doc.username !=""){
        emit(doc.username, doc);
  }
}

/*
teams/_design/byName/_view/byName
*/
function (doc) {
  if(doc.type && doc.type=="team"){
    emit(doc.name, doc);
  }
}

/*
teams/_design/members/_view/byMemberRole
*/
function (doc) {
  if(doc.members) {
    for( var i=0, m=doc.members.length; i<m; i++){
      emit([doc.members[i].name, doc.members[i].role], doc.name);
    }
  }
}

/*
teams/_design/roles/_view/byRole
*/
function (doc) {
  if(doc.type && doc.type=="role") {
    emit(null, doc._id);
  }
}

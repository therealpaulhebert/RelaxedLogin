## Synopsis

This project is a Spring Boot Web / Security based web app. It uses Mustache as the view templating engine, with Bootstrap for styling, and jQuery for some UI slickness. The User login is based in CouchDb's built in _user database. Although the Ektorp CouchDb library was used, I didn't extend the classes with the CouchDbRepositorySupport. I used the STS IDE to build this project.

Users can create and edit status updates relating to a team. They can search through their status updates and edit those statues. Users can also create new teams and edit teams for which they are an Admin.

## Motivation

I couldn't find a full example of Spring Boot and Mustache. I had some trouble getting it to work until I found a website where the extension for the view files was .mustache. Then I wanted to use CouchDb to host the data and login info. I found the built in user database to be pretty cool!

## Installation

Download the project and open in Eclipse or STS. Download CouchDB and install locally. You will need to create the views for CouchDB also. In the src/main/resources/templates folder, the views.js file can be referenced to build views in CouchDB. This is not automatic, but a reference for manually building the views.

I also used a self signed certificate for HTTPS hosting. You should create one yourself and point to it in the /src/main/resources/application.properties file

## API Reference

> To be added later


## License

GNU GPL.

## Screen Shots
Login Screen
![Screen shot of login](/images/LoginScreen.png?raw=true "Login Screen")

New User Registration
![New User Screen](/images/NewUser.png?raw=true "New User Screen")

After login, users are taken to a screen where they can see existing status.
![Start Page](/images/ExistingStatuses.png?raw=true "Statuses Screen")

Clicking the Create Update link shows a form where users can create a new status update.
![New Status Update Page](/images/NewStatus.png?raw=true "New Status Screen")

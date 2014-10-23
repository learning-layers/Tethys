#Tethys

A cloud platform for managing services and its virtual machines, storage containers as well as users with their storage containers. Works together with OpenStack. Formerly known as "i5Cloud".


## Getting started

## Contribution guidelines

> Based on [Thoughbot’s Git protocol][thoughtbot].

### How to keep this repository clean

* DON'T commit files that are specific to your development environment or process
* DO your work in feature branches
* DO oberserve issues in JIRA
* DO delete local and remote feature branches after merging
* DO rebase frequently to assimilate to changes

### How to work with your feature branches

1 Assign JIRA ticket to yourself, create one if necessary

2 Create local feature branch from develop branch

    git checkout develop
    git pull
    git checkout -b <your-developer-id>-<issue-name>
    
3a Work on your local branch and commit them

3b Rebase frequently and resolve conflicts

    git fetch origin
    git rebase origin/develop
    
4 Repeat 3a and 3b until feature is complete and passed all tests. Share/Push your branch

    git push origin <your-developer-id>-<issue-name>
    
5 Let someone else review and merge your feature branch with the develope branch. Keep track of your actions in JIRA
    
### Initial setup
Before you can deploy our application you need to create a few files to fill in necessary information.
To make it easier for you we created several example files, which you can use as a template.
Here is a list of those files:

    ./gradle.properties.example
    ./src/main/resource/oidc/properties.example
    ./src/main/webapp/WEB-INF/classes/META-INF/persistence.xml.example
  
As you already saw in persistence.xml we are using MySQL. You need to setup a MySQL-Server. To do you can use following provided MySQL-Script:

    ./otherResources/mysql/create_db.sql

Developed by Gordon Lawrenz

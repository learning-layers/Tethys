#Tethys

A cloud platform for managing services and its virtual machines, storage containers as well as users with their storage containers. Works together with OpenStack. Formerly known as "i5Cloud".


## Usage

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

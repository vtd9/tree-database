# tree-database

## Overview of the application interface
This Java code launches the graphical user interface (GUI) to get and set information in
the forestry MySQL database. The commonplace packages JDBC and JavaFX were used.
Assume the view of an field biologist that has
the authorization to perform certain getting and setting actions:

* View all tree species in the database with their common names.
* Add a new common name to a species existing in the database.
* View all sightings of a particular species.
* Add a new sighting of a particular species.
* View all occupied habitats with their corresponding species.
* View all habitats known to support a particular species.

## Versions used
### Java
`java 16.0.2 2021-07-20`

`Java(TM) SE Runtime Environment (build 16.0.2+7-67)`

`Java HotSpot(TM) 64-Bit Server VM (build 16.0.2+7-67, mixed mode, sharing)`

### JDBC driver for MySQL
`8.00.28`

### JavaFX
`JavaFX Version: 13`

`JavaFX Runtime Version: 13+14`

## Compiling the code
Since the development was done in the NetBeans IDE with Maven, the entire project will be zipped together for ease of use.
The source code is in tree/app/src/main/java and its subdirectories.

Alternatively, instead of NetBeans, the application can be compiled on the command line:


## Using the app

### Species tab

### Sighting tab

### Habitat tab

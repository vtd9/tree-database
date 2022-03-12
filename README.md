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
`java 16.0.2 2021-07-20` `Java(TM) SE Runtime Environment (build 16.0.2+7-67)`

For example, C:\Program Files\Java\jdk-16.0.2.

### JDBC driver for MySQL
`8.0.28` 

<a href="https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-installing.html">Connector/J Installation</a>

For example, C:\Program Files (x86)\MySQL\Connector J 8.0.

### JavaFX
`JavaFX Version: 13` `JavaFX Runtime Version: 13+14`

<a href="https://gluonhq.com/products/javafx/"> JavaFX downloads</a>

For example, the install can be in the NetBeans directory: C:\Program Files\NetBeans-12.6\netbeans\javafx\modules. In addition, just downloading and extracting the zip file means it can be moved anywhere: e.g., C:\openjfx-17.0.2_windows-x64_bin-sdk.

## Compiling and executing
Since the development was done in the NetBeans IDE with Maven, the entire project will be zipped together for ease of use.
The source code is in tree\app\src\main\java and its subdirectories. The db.properties file should be in the app\ directory, and the pom.xml file should contain a dependency to the JDBC driver, such as
```
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>13</version>
    </dependency>
    <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.28</version>
</dependency>
```

Alternatively, instead of NetBeans, the application can be compiled on the command line. Navigate into the aforementioned java\ directory of the project, then compile using `javac`.

On Windows (<a href="https://stackoverflow.com/questions/6623161/javac-option-to-compile-all-java-files-under-a-given-directory-recursively">reference</a>):
`dir /s /B *.java > sources.txt`

If the JavaFX package is not included with the Java install, explicitly state the module path:

`javac --module-path "C:\openjfx-17.0.2_windows-x64_bin-sdk\javafx-sdk-17.0.2\lib" @sources.txt`

To run, explicitly state the module path, the JavaFX controls module, and the JDBC driver .jar in the Java class path. Make sure a reference (i.e., ".") is included to the class path, separated from the JDBC jar with a semicolon (";"). For example,

`java --module-path "C:\openjfx-17.0.2_windows-x64_bin-sdk\javafx-sdk-17.0.2\lib" --add-modules javafx.controls --class-path "C:\Program Files (x86)\MySQL\Connector J 8.0\mysql-connector-java-8.0.28.jar;." gui.App`

With the command line execution, make sure that the file db.properties is in the same tree\app\src\main\java directory, and that this file contains the correct MySQL server, user, and password information, such as

```
# MySQL DB parameters
user=root
password=abc
url=jdbc:mysql://localhost:3306/forestry
```

Upon successful execution, the interface will appear.

## Using the app

### Species tab

* *View all tree species in the database with their common names.* Click the "Refresh" button.
* *Add a new common name to a species existing in the database.* At the bottom of the window, choose a species from the dropdown menu. Enter a new common name in the adjacent field, then click the "Add" button.

### Sighting tab
* *View all sightings of a particular species.* Enter either the scientific or common name in the "Name" field, select the appropriate radio button, and click the "Search" button.
* *Add a new sighting of a particular species.* At the bottom of the window, choose a species from the dropdown menu, enter a date in the format YYY-MM-DD if known, the latitude (can have a decimal; required), the longitude (can also have a decimal; required), and the altitude (in meters, to the nearest whole number), if known.

### Habitat tab
* *View all occupied habitats with their corresponding species.* Click the "Refresh" button.
* *View all habitats known to support a particular species.* Enter either the scientific or common name in the "Name" field, select the appropriate radio button, and click the "Search" button.

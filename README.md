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

My install is in C:\Program Files\Java\jdk-16.0.2

### JDBC driver for MySQL
`8.0.28` 

<a href="https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-installing.html">Connector/J Installation</a>

My install is in C:\Program Files (x86)\MySQL\Connector J 8.0

### JavaFX
`JavaFX Version: 13` `JavaFX Runtime Version: 13+14`

<a href="https://gluonhq.com/products/javafx/"> JavaFX downloads</a>

My install is with the NetBeans directory: C:\Program Files\NetBeans-12.6\netbeans\javafx\modules. In addition, just downloading and extracting the zip file means it can be moved anywhere: e.g., C:\openjfx-17.0.2_windows-x64_bin-sdk.

## Compiling
Since the development was done in the NetBeans IDE with Maven, the entire project will be zipped together for ease of use.
The source code is in tree/app/src/main/java and its subdirectories. The db.properties file should be in the app\ directory, and the pom.xml file should contain a dependency to the JDBC driver, such as
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

Alternatively, instead of NetBeans, the application can be compiled on the command line after navigating into the aforementioned java/ directory of the project:

On Windows (referenced this <a href="https://stackoverflow.com/questions/6623161/javac-option-to-compile-all-java-files-under-a-given-directory-recursively">StackOverflow post</a>):
`dir /s /B *.java > sources.txt`

`javac @sources.txt`

If the JavaFX package is included with the Java install, explicitly state the module path:
`java --module-path "C:\Users\RrbDellDesktop3\Downloads\openjfx-17.0.2_windows-x64_bin-sdk\javafx-sdk-17.0.2\lib" --add-modules javafx.controls -jar App.jar`



## Using the app

### Species tab

### Sighting tab

### Habitat tab

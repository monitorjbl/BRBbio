# BRBbio

Makes analysis of high-throughput sampling data super easy. With this application, you can upload your raw data (using a template Excel file), and define custom functions to normalize and extract Z factors.

# Installing

As of now, there is no real installer for this application. It's packaged as a standalone executable JAR, so all you really need to do is [download it from here]() and then run it with this command:

```
java -jar brbbio-0.1-RC.jar
```

The application will now be running on your machine on port 8080, with a default user/pass of `admin/admin`. To visit it, just go to http://localhost:8080.

# Configuring

Config options are fairly light at the moment, and are limited to the database settings. You may add your own config file by passing in the following parameter (note that having it *before* the `-jar` is important):

**Windows**

```
java -Dbio.conf="\\c:\\path\\to\\conf" -jar brbbio-0.1-RC.jar
```

**\*nix**

```
java -Dbio.conf="/path/to/conf" -jar brbbio-0.1-RC.jar
```

The config file should look like the example below. If the `embedded.use` value is set to `true`, all `standalone.*` settings are ignored. The application comes bundled with an embedded H2 database, but it is not the best performing DB in the world. If you'd like to use a standalone database, you may set the `embedded.use` value to `false` and set the `standalone.*` values appropriately.

```
# Embedded DB settings
embedded.use=true
embedded.dataPath=hts

# Standalone DB settings
standalone.driver=com.mysql.jdbc.Driver
standalone.jdbcUrl=jdbc:mysql://localhost:3306/dbname
standalone.user=root
standalone.pass=root
```

# Building

This is for anyone that would like to play around with the source code. Building is very simple, but you'll need a couple of things before you get started:

* [Java JDK 7+](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
* [Maven 3+](http://maven.apache.org/download.cgi)
* [Git](http://git-scm.com/book/en/Getting-Started-Installing-Git)

Once you have these dependencies in place, you can checkout the code and build:

```
git clone https://github.com/monitorjbl/BRBbio.git
cd BRBbio
mvn clean package
```

The default Maven build profile skips unit tests because they take some time to execute. If you'd like to run the build with unit tests on, you can do this:

```
mvn clean package -Pdevelop
```

# How it works

For the curious, this app simply provides a nice way to upload data to a fixed database schema. Pretty much all analysis uses the same data model, but has different normalization functions. The model is pretty simple:

```
Placeholder for ERD (sorry, don't know how to get this on the README.md yet)
```

Once the data has been bent to fit this model, the custom functions can be pretty applied to them by simple SQL substitution. A template SQL used by the app can be seen in the `src/main/resources/sql/normalize.sql` file. The submitted function is evaluated with simple string replacement to manufacture the final SQL that is run.
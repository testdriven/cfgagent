# Configuration agent

The problem with application usually is that they tend to have too many configuration options. Database connection string, SMTP server settings, file locations (more than one), configuration of connection pool... just to name a few. As the number of options grows (the project I'm working on now has about 200 of those) passing them all in the command line using JAVA_OPTS is just not possible. And I don't mean like "not practical" but plain and simple not possible due to the limits of what a command-line can take. There has to be a better way to do it.

There actually is a mechanism that one can use with Tomcat called catalina.properties (and I'm sure one would find similar ones in other containers) but that is not going to fly if we want to select which set of options we want to use for this particular run or if the values should come from environmental variables (for example from Docker or Heroku).

## JVM Agent to the rescue

I've been looking for a while for a nice, pluggable solution to this problem. My idea was roaming about something that could be fixed but parameterized at the same time, could be specified at execution time (just like JAVA_OPTS are) and in general to be just easy to use regardless of the execution environment.

Looking at it from different angles I turned my attention to what is executed before the main method. As it turns out there is such a thing and it is the concepts of Java Agents.

## Usage:

Create a file called ```system.properties``` that will contain key-value pairs like so:

```
app.db.driver=oracle.jdbc.OracleDriver
app.db.url=jdbc:oracle:thin:{DB_USERNAME:oracle}/{DB_PASSWORD:system}@{ORACLE_PORT_1521_TCP_ADDR:127.0.0.1}:{ORACLE_PORT_1521_TCP_PORT:1521}:{ORACLE_SID:XE}
```

Please note the notation: the key cannot be parameterized but the value can use placeholders in format ```{placeholder:default-value}```

Then you can use it loke so:

```
CATALINA_OPTS="-javaagent:path/to/cfgagent.jar=path/to/system.properties" ./catalina.sh start
```

or

```
MAVEN_OPTS="-javaagent:path/to/cfgagent.jar=path/to/system.properties" mvn clean install
```

If ```path/to/system.properties``` is not specified it is assumed that the file name is ```system.properties``` and that it resides in the current working forlder.

For debugging the agent takes one system property in particular, called "-Dcfgagent.debug=true". To make sure it is always set add it to the _OPTS part like so:

```
MAVEN_OPTS="-Dcfgagent.debug=true -javaagent:path/to/cfgagent.jar=path/to/system.properties" mvn clean install
```

#!/bin/sh -e

mvn -f ../pom.xml clean install
MAVEN_OPTS="-Dcfgagent.debug=true -javaagent:../target/cfgagent.jar=../src/test/config/system.properties" mvn -version

#!/bin/bash
export $(cat .env | tr -d ' ' | grep -v "#" | xargs)
export JAVA_HOME="/c/Program Files/Eclipse Adoptium/jdk-17.0.18.8-hotspot"
export PATH="$JAVA_HOME/bin:$PATH"
export M2_HOME="/C/Program Files/Apache Maven/apache-maven-3.9.11"
export PATH="$M2_HOME/bin:$PATH"
mvn install && mvn spring-boot:run
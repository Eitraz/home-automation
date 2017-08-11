#!/bin/bash
cd home-automation && \
git pull && \
mvn clean package -DskipTests && \
cd target && \
cp home-automation-*.jar home-automation.jar && \
mv home-automation.jar / && \
cd / && \
java -Dip=$1 -jar home-automation.jar --spring.config.location=file:application.properties
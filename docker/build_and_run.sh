#!/bin/bash
cd home-automation && \
git pull && \
mvn clean package -DskipTests && \
cd target && \
cp home-automation-*.jar home-automation.jar && \
java -Dip=$1 -jar home-automation.jar
#!/bin/bash
cd home-automation && \
git pull && \
mvn clean package -DskipTests && \
cd target && \
cp home-automation-*.jar home-automation.jar && \
java -Dip=$ip -jar home-automation.jar
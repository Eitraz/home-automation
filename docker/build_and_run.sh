#!/bin/bash

cd /
rm -rf home-automation
git clone git@github.com:Eitraz/home-automation.git
cd home-automation
mvn clean package -DskipTests
cp target/home-automation-*.jar /home-automation.jar
cd /
rm -rf home-automation

java -Dip=$ip -jar home-automation.jar
#!/bin/bash
source /etc/profile;
mvn --version;
cd /root/$1;
mvn clean install -Dmaven.test.skip=true;




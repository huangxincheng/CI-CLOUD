#!/bin/bash
deployAppName=$1
deployAppVersion=$2
deoloyAppDir=$3

cd /root/$deoloyAppDir/$deployAppName
cp ./target/$deployAppName.jar ./docker
cd /root/$deoloyAppDir/$deployAppName/docker


docker build -f ./Dockerfile -t hxc/$deployAppName:$deployAppVersion .

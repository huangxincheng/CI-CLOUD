#!/bin/bash
# 回收镜像
deployAppName=$1
deployAppVersion=$2

# 回收资源命令（这里只回收master的 其他节点可扩展回收）
docker images|grep "$deployAppName"|grep -v "$deployAppVersion"|awk -F ' ' '{print $3}'|xargs docker rmi -f

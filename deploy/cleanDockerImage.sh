#!/bin/bash
deployAppName=$1
deployAppVersion=$2

# 回收资源命令（这里只回收master的 其他节点可扩展回收）
docker rmi $(docker images | grep "none" | awk '{print $3}')
docker images|grep "registry.cn-shenzhen.aliyuncs.com/huangxincheng/$deployAppName"|grep -v "$deployAppVersion"|awk -F ' ' '{print $3}'|xargs docker rmi
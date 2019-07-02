#!/bin/bash
deployAppName=$1
deployAppVersion=$2
deployDockerVersion=$3
imageId=`docker images -q hxc/$deployAppName:$deployAppVersion`
echo $imageId

# 这里需要替换自己的阿里云账号密码
docker login --username=XXX --password=XXX registry-vpc.cn-shenzhen.aliyuncs.com
docker tag $imageId registry-vpc.cn-shenzhen.aliyuncs.com/huangxincheng/$deployAppName:$deployDockerVersion
docker push registry-vpc.cn-shenzhen.aliyuncs.com/huangxincheng/$deployAppName:$deployDockerVersion




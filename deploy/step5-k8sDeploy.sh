#!/bin/bash
deployAppName=$1
deployK8sVersion=$2
old=`grep 'image' /root/k8s-$deployAppName-deployment.yaml`
echo $old
new="        image: registry-vpc.cn-shenzhen.aliyuncs.com/huangxincheng/$deployAppName:$deployK8sVersion"
echo $new
# 替换文件里面镜像
sed -i "s!$old!$new!" /root/k8s-$deployAppName-deployment.yaml

# 拉取镜像
docker pull registry-vpc.cn-shenzhen.aliyuncs.com/huangxincheng/$deployAppName:$deployK8sVersion
docker images -q registry-vpc.cn-shenzhen.aliyuncs.com/huangxincheng/$deployAppName:$deployK8sVersion

# k8s部署
kubectl apply -f /root/k8s-$deployAppName-deployment.yaml
kubectl apply -f /root/k8s-$deployAppName-deployment-hpa.yaml

# 镜像小，启动快， 当无法使用arthas
#FROM fabric8/java-alpine-openjdk8-jdk
#FROM fabric8/java-jboss-openjdk8-jdk
#FROM openjdk:8-jdk-alpine
FROM registry-vpc.cn-shenzhen.aliyuncs.com/huangxincheng/java-alpine-openjdk8-jdk
MAINTAINER huangxincheng 249270087@qq.com
VOLUME /tmp
EXPOSE 80
RUN mkdir -p /home/ci && chmod 755 /home/ci
COPY ./ci-cloud-gateway.jar /home/ci
ENTRYPOINT ["/sbin/tini", "--"]
CMD java ${JAVA_OPTS} -jar home/ci/ci-cloud-gateway.jar
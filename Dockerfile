FROM jdk:1.8

MAINTAINER zk

WORKDIR /data

ENV JAR_NAME "my-site-1.0.2.RELEASE.jar"
# 设置编码
ENV LANG=en_US.UTF-8
ENV DIR "/data/server/"

# 使用host模式容器和宿主机共享network，这时候localhost就可以访问宿主机端口了。
#ENV NETWORK=host

# 软链接解决时间不对
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    mkdir $DIR

ADD /target/$JAR_NAME /data/server/$JAR_NAME

ENTRYPOINT ["sh","-c","java -jar $DIR$JAR_NAME --spring.profiles.active=prod"]
#设置镜像基础，jdk24
FROM openjdk:24
#设置镜像对外暴露端口
EXPOSE 7270
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp
#ARG：引用plugin中配置的 JAR_FILE 文件
ARG JAR_FILE
#将当前 target 目录下的 jar 放置在根目录下，命名为 app.jar。
ADD target/${JAR_FILE} /village-e-station.jar
#执行启动命令
ENTRYPOINT ["java", "-jar","/village-e-station.jar"]
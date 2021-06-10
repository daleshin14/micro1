# FROM openjdk:8-jdk-alpine
FROM registry.diasoft.ru:5000/diasoft-parent-jdk8:1.0.0
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
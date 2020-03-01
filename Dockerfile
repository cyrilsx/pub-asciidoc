FROM openjdk:11-jdk-alpine

ARG JAR_FILE

ADD target/${JAR_FILE} /app.jar

ENV SPRING_PROFILES_ACTIVE docker

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
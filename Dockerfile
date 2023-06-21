FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app

COPY src /app/src
COPY pom.xml /app/pom.xml

RUN mvn clean package

FROM openjdk:17-jdk-slim

COPY --from=builder app/target/*.jar /app/my-app.jar

ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]

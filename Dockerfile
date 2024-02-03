FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app

COPY src /app/src
COPY pom.xml /app/pom.xml
COPY checkstyle.xml /app/checkstyle.xml
COPY checkstyle-suppressions.xml /app/checkstyle-suppressions.xml
COPY lombok.config /app/lombok.config

RUN mvn clean package

FROM openjdk:17-jdk-slim

COPY --from=builder /app/target/*.jar /app/my-app.jar

ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]

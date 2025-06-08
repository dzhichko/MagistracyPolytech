
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

COPY src src

ENV TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal
ENV DOCKER_HOST=unix:///var/run/docker.sock

RUN ./mvnw clean verify

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

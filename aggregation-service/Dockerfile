FROM gradle:8.11-jdk21 AS build
WORKDIR /app
COPY src /app/src
COPY build.gradle.kts /app/
RUN gradle clean build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar", "app.jar"]
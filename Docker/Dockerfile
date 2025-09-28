FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY ../ .
RUN mvn clean package

FROM openjdk:21-jdk-slim as runtime
COPY --from=build /app/target/*.jar /financial-management.jar

ENV SPRING_PROFILES_ACTIVE=prod
CMD ["java", "-jar", "/financial-management.jar"]
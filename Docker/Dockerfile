FROM maven:3-openjdk-21-slim as build
WORKDIR /app
COPY ../ .
RUN mvn clean package

FROM openjdk:21-jdk-slim as runtime
COPY --from=build /app/target/*.jar /financial-management.jar
CMD ["java", "-jar", "/financial-management.jar"]
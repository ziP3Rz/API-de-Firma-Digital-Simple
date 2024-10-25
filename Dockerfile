FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app

COPY firma-digital.api/pom.xml .
COPY firma-digital.api/src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/firma-digital.api-0.0.1-SNAPSHOT.jar firma-digital.api-0.0.1-SNAPSHOT.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "firma-digital.api-0.0.1-SNAPSHOT.jar"]

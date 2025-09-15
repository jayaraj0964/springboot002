# Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml /app/
COPY src /app/src/
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/spring-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

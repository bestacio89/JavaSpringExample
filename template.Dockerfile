# Build stage with Java 17 image
FROM maven:3.8.1-openjdk-17 AS builder

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package

# Base image for running the application (using Java 17)
FROM openjdk:17-jre-alpine

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the JAR from the build stage
COPY --from=builder /usr/src/app/target/*.jar /usr/src/app/app.jar

# Expose the application port
EXPOSE 8080

# Entrypoint and command to run the application
ENTRYPOINT ["java", "-jar", "/usr/src/app/app.jar"]

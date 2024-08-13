# Base image for build stage with Java 17
FROM maven:3.8.1-openjdk-17 AS builder

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Package the application (without running tests)
ARG SKIP_TESTS=true
RUN mvn clean package -DskipTests=${SKIP_TESTS}

# Test stage (Optional)
FROM builder AS tester
# Run tests if SKIP_TESTS is not set to true
RUN mvn test

# Production stage
FROM eclipse-temurin:17-jre AS production

# Set the working directory inside the container
WORKDIR /usr/src/app

# Expose the application port
EXPOSE 8080

# Copy the JAR from the build stage
COPY --from=builder /usr/src/app/target/*.jar /usr/src/app/app.jar

# Entrypoint and command to run the application
ENTRYPOINT ["java", "-jar", "/usr/src/app/app.jar"]

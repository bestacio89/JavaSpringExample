# Specify the working directory
WORKDIR /usr/src/app

# Copy project files
COPY . /usr/src/app

# Build stage with Java 17 image
RUN mvn package

# Base image for running the application (using Java 17)
FROM openjdk:17-jre-alpine

# Copy the JAR from the build stage
COPY --from=builder /usr/src/app/target/*.jar /app.jar

# Expose the port
EXPOSE 8080

# Entrypoint and command to run the application
ENTRYPOINT ["java"]
CMD ["-jar", "/app.jar"]
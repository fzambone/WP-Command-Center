# Use the official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/authentication-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 4000

# Command to run the application
CMD ["java", "-jar", "app.jar"]
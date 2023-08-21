# Use the official Gradle image as the base image
FROM gradle:6.8.3-jdk8 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the build.gradle and settings.gradle files to the container
COPY build.gradle settings.gradle ./

# Copy the entire source code to the container
COPY src/ ./src/
COPY files/ ./files/

# Build the application using Gradle
RUN gradle build -x test

# Use the official OpenJDK 8 as the base image for the final image
FROM openjdk:8-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage to the container
COPY --from=build /app/build/libs/blog-0.0.1.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Run the Java application
CMD ["java", "-jar", "app.jar"]

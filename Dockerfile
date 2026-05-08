
# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copy the pom.xml and source code
COPY . .
# Build the JAR file inside the container
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image

# Use an official Python runtime as the base image

FROM eclipse-temurin:21-jdk-alpine
LABEL authors="Abul Hasan"
# Set the working directory inside the container

# Copy the dependency file into the container
COPY --from=build /app/target/*.jar app.jar


# Expose the application port
EXPOSE 8081

# Define the default command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
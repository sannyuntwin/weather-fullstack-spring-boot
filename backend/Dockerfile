# Use Maven 3.9 with Eclipse Temurin JDK 17 as the base image
# This provides both Maven and the Java Development Kit needed to build and run our Spring Boot application
FROM maven:3.9-eclipse-temurin-17

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven configuration files (pom.xml) first
# This takes advantage of Docker's layer caching - if pom.xml doesn't change,
# Maven won't re-download dependencies, making subsequent builds faster
COPY pom.xml .

# Download Maven dependencies
# This step runs Maven in offline mode to download all required dependencies
RUN mvn dependency:go-offline -B

# Copy the entire source code into the container
# We copy this after downloading dependencies to optimize build caching
COPY src ./src

# Build the Spring Boot application using Maven
# -B: Batch mode (non-interactive)
# -DskipTests: Skip running tests (you can remove this for production builds)
RUN mvn clean package -B -DskipTests

# Expose port 8080 for the Spring Boot application
# Spring Boot applications run on port 8080 by default
EXPOSE 8080

# Command to run the Spring Boot application
# The JAR file is created in the target directory with the format: {artifactId}-{version}.jar
CMD ["java", "-jar", "target/weather-api-0.0.1-SNAPSHOT.jar"]

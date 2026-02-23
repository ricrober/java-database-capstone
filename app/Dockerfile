# ==========================================
# STAGE 1: Build Stage (The Builder)
# ==========================================
# Use a Maven image with JDK 17 to compile the Java source code.
# We alias this stage as 'builder' to reference it in the next stage.
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Set the working directory inside the container for all subsequent commands.
WORKDIR /app

# Copy the Project Object Model (POM) file to download dependencies.
COPY pom.xml .

# Copy the entire source code directory into the container.
COPY src ./src

# Build the application and package it into a JAR file.
# We skip tests to speed up the container build process in this specific environment.
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Runtime Stage (The Final Image)
# ==========================================
# Use a lightweight JRE (Java Runtime Environment) image.
# This keeps the final image size small and more secure.
FROM eclipse-temurin:17.0.15_6-jre

# Set the working directory for the application execution.
WORKDIR /app

# Copy only the compiled JAR file from the 'builder' stage.
# This ignores the source code and build tools, leaving a clean production image.
COPY --from=builder /app/target/back-end-0.0.1-SNAPSHOT.jar app.jar

# Inform Docker that the container listens on port 8080 at runtime.
EXPOSE 8080

# The command to run the application when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]

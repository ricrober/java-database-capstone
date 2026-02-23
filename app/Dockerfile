# ==========================================
# STAGE 1: Build Stage (The Builder)
# ==========================================
# This stage uses a full JDK and Maven to compile the source code.
# We name it 'builder' to allow the next stage to grab files from it.
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Define the working directory inside the container for build operations.
WORKDIR /app

# Copy only the pom.xml first. This allows Docker to cache the 
# downloaded dependencies unless the pom.xml changes.
COPY pom.xml .

# Copy the actual source code into the builder container.
COPY src ./src

# Compile the application and create the executable JAR file.
# '-DskipTests' is used here to minimize build time in the CI/CD pipeline.
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Runtime Stage (The Production Image)
# ==========================================
# We switch to a lightweight JRE image which does not contain Maven or the JDK.
# This makes the final image significantly smaller and more secure.
FROM eclipse-temurin:17.0.15_6-jre

# Set the working directory where the application will live.
WORKDIR /app

# Copy the JAR file produced in the 'builder' stage into this fresh image.
# We rename it to 'app.jar' for simplicity in the ENTRYPOINT.
COPY --from=builder /app/target/back-end-0.0.1-SNAPSHOT.jar app.jar

# Document that the application is expected to run on port 8080.
# Note: This is primarily for documentation and doesn't actually 'open' the port.
EXPOSE 8080

# Configure the container to run as an executable.
# Using the exec form ["executable", "param1"] is preferred over the shell form.
ENTRYPOINT ["java", "-jar", "app.jar"]

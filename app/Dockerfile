# ==========================================
# ARGUMENTS: Define versions in one place
# ==========================================
ARG JAVA_VERSION=17
# We use a consistent base for both stages to ensure compatibility
ARG BASE_IMAGE=eclipse-temurin

# ==========================================
# STAGE 1: Build Stage (The Builder)
# ==========================================
FROM maven:3.9.9-${BASE_IMAGE}-${JAVA_VERSION} AS builder

WORKDIR /app

# Cache dependencies: Copy pom.xml and run go-offline 
# (This ensures faster builds if source code changes but dependencies don't)
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: Runtime Stage (The Production Image)
# ==========================================
# Using the same JAVA_VERSION and BASE_IMAGE for consistency
FROM ${BASE_IMAGE}:${JAVA_VERSION}-jre

WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/back-end-0.0.1-SNAPSHOT.jar app.jar

# Standard port for Spring Boot
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

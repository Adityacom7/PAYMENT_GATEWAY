# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy only pom.xml first (better build caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the Spring Boot JAR
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/target/authorization-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Start the service
ENTRYPOINT ["java", "-jar", "app.jar"]
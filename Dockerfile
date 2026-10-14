# Use Maven to build the app
#FROM maven:3.9.6-eclipse-temurin-17 AS build
#WORKDIR /app
#COPY . .
#RUN mvn clean package -DskipTests
#
# Run the packaged JAR
#FROM eclipse-temurin:17-jdk
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
#
# Health check configuration
#HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
#  CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1
#
#ENTRYPOINT ["java","-jar","app.jar"]
# Stage 1: Build the application using JDK 25
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Step A: Cache dependencies
COPY R-Transfer/pom.xml .
RUN mvn dependency:go-offline -B

# Step B: Copy source code and build
COPY R-Transfer/src ./src
RUN mvn clean package -DskipTests


# Stage 2: Secure, ultra-lightweight JRE 25 runtime environment
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install curl so your Actuator health check works perfectly on Alpine
RUN apk add --no-cache curl

# Security: Run as a non-privileged system user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the compiled jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Health check configuration tailored for Render's dynamic port assignment
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
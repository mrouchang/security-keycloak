# --- Stage 1: Build the application ---
FROM maven:3.9.8-eclipse-temurin-21 AS builder

WORKDIR /app

# Create module directory
RUN mkdir -p spring-gateway

# Copy parent pom and module pom
COPY pom.xml .
COPY spring-gateway/pom.xml ./spring-gateway/

# Fetch dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY spring-gateway/src ./spring-gateway/src

# Build all modules
RUN mvn clean install -DskipTests -B

# --- Stage 2: Runtime image for spring-gateway ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built spring-gateway JAR
COPY --from=builder /app/spring-gateway/target/spring-gateway-*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

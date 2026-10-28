# --- Stage 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Step 1: Copy only the pom.xml to cache dependencies
COPY pom.xml .

# Fetch all dependencies. This layer is cached unless pom.xml changes.
RUN mvn dependency:go-offline -B

# 2. Create the child module directories
RUN mkdir key-cloak-server spring-gateway

# 3. Copy the pom.xml for each child module to cache dependencies properly
COPY key-cloak-server/pom.xml ./key-cloak-server/
COPY spring-gateway/pom.xml ./spring-gateway/

# 4. Copy the complete source code for all modules
COPY key-cloak-server/src ./key-cloak-server/src
COPY spring-gateway/src ./spring-gateway/src

# Run the build
RUN mvn clean install -B

# --- Stage 2: Create the runtime image ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the compiled .jar file from the builder stage
# Note: Replace 'your-app-name' with the actual artifactId from your pom.xml
COPY --from=builder ./spring-gateway/target/your-app-name-*.jar app.jar

# Expose the application port (adjust as necessary)
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

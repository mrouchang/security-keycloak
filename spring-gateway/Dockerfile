# --- Stage 1: Build the application ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Step 1: Copy only the pom.xml to cache dependencies
COPY pom.xml .

# Fetch all dependencies. This layer is cached unless pom.xml changes.
RUN mvn dependency:go-offline -B

# Step 2: Copy the source code and build the application
COPY src ./src

# Run the build. (Optional: Add -DskipTests if you want to skip tests)
RUN mvn clean install -B

# --- Stage 2: Create the runtime image ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the compiled .jar file from the builder stage
# Note: Replace 'your-app-name' with the actual artifactId from your pom.xml
COPY --from=builder /app/target/your-app-name-*.jar app.jar

# Expose the application port (adjust as necessary)
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

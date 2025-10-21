# Use official Java runtime
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy Maven build files
COPY target/studentexpense-1.0.0.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]

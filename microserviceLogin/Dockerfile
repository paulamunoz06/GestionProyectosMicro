# Use the same base image as your other services
FROM eclipse-temurin:21-alpine

# Create non-root user (consistent with your other services)
RUN addgroup -g 1028 devopsc && \
    adduser -D -G devopsc admin

# Set working directory
WORKDIR /app

# Copy the JAR file (using wildcard to match the version)
ARG JAR_FILE=target/microserviceLogin-*.jar
COPY ${JAR_FILE} app.jar

# Ensure the app directory is owned by the non-root user
RUN chown -R admin:devopsc /app

# Switch to non-root user
USER admin

# Expose the port (documentation only - actual mapping is in Compose)
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
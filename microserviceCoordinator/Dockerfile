# Use the same base image for consistency
FROM eclipse-temurin:21-alpine

# Create non-root user with same UID/GID as other services
RUN addgroup -g 1028 devopsc && \
    adduser -D -G devopsc admin

# Set working directory
WORKDIR /app

# Copy the built JAR (wildcard to handle version numbers)
ARG JAR_FILE=target/microserviceCoordinator-*.jar
COPY ${JAR_FILE} app.jar

# Environment variables (override in docker-compose.yml if needed)
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8081

# Ensure proper permissions
RUN chown -R admin:devopsc /app

# Switch to non-root user
USER admin

# Expose port (documentation only - actual mapping is in Compose)
EXPOSE 8081

# Startup command (add JVM options if needed)
ENTRYPOINT ["java", "-jar", "app.jar"]
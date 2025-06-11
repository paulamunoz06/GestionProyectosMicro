FROM eclipse-temurin:21-alpine

# Non-root user setup
RUN addgroup -g 1028 devopsc && adduser -D -G devopsc admin

WORKDIR /app
COPY target/microserviceStudent-*.jar app.jar

# Optional: Environment variables (if needed)
ENV SPRING_PROFILES_ACTIVE=prod

# Permissions
RUN chown -R admin:devopsc /app
USER admin

# Optional but recommended for documentation
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
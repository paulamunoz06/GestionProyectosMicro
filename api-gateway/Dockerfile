FROM eclipse-temurin:21-alpine

# Non-root user setup
RUN addgroup -g 1028 devopsc && adduser -D -G devopsc admin

WORKDIR /app
COPY target/api-gateway-*.jar app.jar

# Optional: Environment variables (if needed)
ENV SPRING_PROFILES_ACTIVE=prod

# Permissions
RUN chown -R admin:devopsc /app
USER admin


ENTRYPOINT ["java", "-jar", "app.jar"]
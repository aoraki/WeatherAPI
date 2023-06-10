FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.11_9

# Some standard handy tools to have in your container
RUN apk add --no-cache curl netcat-openbsd nss bash bind-tools

EXPOSE 8080

COPY build/libs/*.jar WeatherAPI.jar

# Run with optimised settings
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=80.0", "-jar", "WeatherAPI.jar"]
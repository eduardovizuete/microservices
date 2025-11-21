FROM openjdk:21-slim
WORKDIR /app
COPY target/service-notify-1.0.0.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

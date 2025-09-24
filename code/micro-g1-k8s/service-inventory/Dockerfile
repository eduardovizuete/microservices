FROM openjdk:21-slim
WORKDIR /app
COPY target/service-inventory-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
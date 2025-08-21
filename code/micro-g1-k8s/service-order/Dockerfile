FROM openjdk:21-slim
WORKDIR /app
COPY target/service-order-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

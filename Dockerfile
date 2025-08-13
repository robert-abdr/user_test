FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY /build/libs/service.jar build/

WORKDIR /app/build

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
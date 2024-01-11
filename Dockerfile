FROM openjdk:17-jdk-slim

EXPOSE 8080

RUN ./mvnw compile
RUN ./mvnw package

COPY target/*.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
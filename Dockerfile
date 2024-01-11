FROM openjdk:17-jdk-slim

EXPOSE 8080

RUN ./mvnw compile
RUN ./mvnw package

COPY . .

ENTRYPOINT [ "java", "-jar", "target/spring-boot-ecom-0.0.1.jar" ]
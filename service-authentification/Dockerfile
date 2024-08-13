FROM amazoncorretto:21-alpine-jdk
ARG JAR_FILE=target/service-authentification-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
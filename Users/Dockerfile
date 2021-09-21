
FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
COPY build/libs/Users-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

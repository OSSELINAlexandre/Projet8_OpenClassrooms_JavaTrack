
FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
COPY build/libs/Users-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

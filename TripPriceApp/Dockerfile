
FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
COPY build/libs/TripPriceApp-1.0-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app.jar"]

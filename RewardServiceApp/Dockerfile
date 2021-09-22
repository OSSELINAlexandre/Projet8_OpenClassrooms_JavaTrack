FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
COPY build/libs/RewardServiceApp-1.0-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]


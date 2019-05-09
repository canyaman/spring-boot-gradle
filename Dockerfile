FROM openjdk:8-jdk-alpine

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} spring-boot-app.jar

ENV SPRING_PROFILES_ACTIVE=container
ENV SERVER_PORT=8080
ENV MANAGEMENT_SERVER_PORT=9090

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar","/spring-boot-app.jar"]
EXPOSE $SERVER_PORT $MANAGEMENT_SERVER_PORT
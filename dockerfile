FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/UsersService-0.0.1-SNAPSHOT.jar UsersService.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "UsersService.jar"]
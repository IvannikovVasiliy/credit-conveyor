FROM openjdk:17-jdk-alpine3.14
WORKDIR /configuration
ADD ./target/configuration-0.0.1-SNAPSHOT.jar configuration.jar
ENTRYPOINT ["java", "-jar", "configuration.jar"]
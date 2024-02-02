FROM openjdk:17-jdk-alpine3.14
ADD ./target/discovery-0.0.1-SNAPSHOT.jar discovery.jar
ENTRYPOINT ["java", "-jar", "discovery.jar"]
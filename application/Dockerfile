FROM openjdk:17-jdk-alpine3.14 as builder
WORKDIR /application
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package

FROM openjdk:17-jdk-alpine3.14
WORKDIR /application
COPY --from=builder /application/target/application:0.0.1-SNAPSHOT.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
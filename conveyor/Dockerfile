FROM openjdk:17-jdk-alpine3.14 as builder
WORKDIR /conveyor
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package

FROM openjdk:17-jdk-alpine3.14
WORKDIR /conveyor
COPY --from=builder /conveyor/target/conveyor:0.0.1-SNAPSHOT.jar conveyor.jar
ENTRYPOINT ["java", "-jar", "conveyor.jar"]
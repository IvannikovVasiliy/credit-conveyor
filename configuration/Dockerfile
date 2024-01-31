FROM openjdk:17-jdk-alpine3.14 as builder
WORKDIR /configuration
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine3.14
WORKDIR /configuration
COPY --from=builder /configuration/target/configuration-0.0.1-SNAPSHOT.jar configuration.jar
ENTRYPOINT ["java", "-jar", "configuration.jar"]
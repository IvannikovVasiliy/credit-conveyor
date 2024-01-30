FROM openjdk:17-jdk-alpine3.14 as builder
WORKDIR /dossier
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package

FROM openjdk:17-jdk-alpine3.14
WORKDIR /dossier
COPY --from=builder /dossier/target/dossier:0.0.1-SNAPSHOT.jar dossier.jar
ENTRYPOINT ["java", "-jar", "dossier.jar"]
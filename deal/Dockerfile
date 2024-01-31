FROM openjdk:17-jdk-alpine3.14 as builder
WORKDIR /deal
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
COPY ./lombok.config ./lombok.config
RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine3.14
WORKDIR /deal
COPY --from=builder /deal/target/deal-0.0.1-SNAPSHOT.jar deal.jar
ENTRYPOINT ["java", "-jar", "deal.jar"]
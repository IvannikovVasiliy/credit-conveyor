#FROM openjdk:17-jdk-alpine3.14 as builder
#WORKDIR /gateway
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#COPY ./src ./src
#RUN ./mvnw clean package -DskipTests
#
#FROM openjdk:17-jdk-alpine3.14
#WORKDIR /gateway
#COPY --from=builder /gateway/target/gateway-0.0.1-SNAPSHOT.jar gateway.jar
#ENTRYPOINT ["java", "-jar", "gateway.jar"]

FROM openjdk:17-jdk-alpine3.14
WORKDIR /gateway
ADD ./target/gateway-0.0.1-SNAPSHOT.jar gateway.jar
ENTRYPOINT ["java", "-jar", "gateway.jar"]
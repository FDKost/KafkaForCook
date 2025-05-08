FROM maven:3.9.9-amazoncorretto-21 AS build
COPY .docker-m2-repo/by/cook/core /root/.m2/repository/by/cook/core
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src/
RUN mvn package -DskipTests

#Running java
FROM openjdk:21-jdk
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /opt/KafkaForCook/forCook.jar
ENTRYPOINT ["java","-jar","/opt/KafkaForCook/forCook.jar"]
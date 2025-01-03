FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /app/target/classlog-0.0.1-SNAPSHOT.jar classlog-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/classlog-0.0.1-SNAPSHOT.jar"]

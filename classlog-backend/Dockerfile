FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/classlog-0.0.1-SNAPSHOT.jar classlog-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/classlog-0.0.1-SNAPSHOT.jar"]
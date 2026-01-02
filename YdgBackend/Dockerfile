FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=target/YdgBackend-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar

RUN apk add --no-cache wget

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

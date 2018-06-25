FROM openjdk:8u131-jre-alpine

COPY target/lib/* /app/lib/
COPY target/chat-cli-client-1.0.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM openjdk:8u131-jre-alpine

COPY target/lib/* /app/lib/
COPY target/auth-service-1.0.jar /app/app.jar
COPY userdatabase.txt /app/userdatabase.txt

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

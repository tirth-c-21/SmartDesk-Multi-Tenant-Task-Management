FROM eclipse-temurin:17-jdk
COPY target/SmartDesk.jar /app.jar
ENTRYPOINT  ["java","-jar","/app.jar"]
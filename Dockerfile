# Utilise une image Maven pour construire le projet
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ ./src/
RUN mvn clean package -DskipTests

# Utilise une image JRE légère pour l'exécution
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/liste-noel-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

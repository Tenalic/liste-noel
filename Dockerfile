# Utilise une image OpenJDK 21 légère et optimisée
FROM eclipse-temurin:21-jdk-jammy

# Définit le répertoire de travail
WORKDIR /app

# Copie le fichier JAR (généré par Maven)
COPY target/liste-noel-0.0.1-SNAPSHOT.jar app.jar

# Définit le port d'écoute (par défaut 8080 pour Spring Boot)
EXPOSE 8080

# Lance l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

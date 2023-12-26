# Utilisez l'image de base adoptopenjdk avec la version 17
FROM agliullin92/openjdk.17.dnd-alpine

# Définissez le répertoire de travail dans le conteneur
WORKDIR /app

# Copiez le fichier pom.xml dans le conteneur à /app
COPY pom.xml .

# Copiez le wrapper Maven dans le conteneur
COPY mvnw .
COPY .mvn .mvn

# Donnez les permissions d'exécution au wrapper Maven
RUN chmod +x mvnw

# Téléchargez les dépendances Maven (ceci permet de mettre en cache les dépendances si le pom.xml n'a pas changé)
RUN ./mvnw dependency:go-offline

# Copiez le reste des fichiers dans le conteneur
COPY src ./src

# Compilez l'application avec Maven
RUN ./mvnw package -DskipTests

# Exposez le port 8080 (ou tout autre port utilisé par votre application)
EXPOSE 8080

# Commande de démarrage de l'application Spring Bootmvn
CMD ["java", "-jar", "target/projet_infra_3_backend-0.0.1-SNAPSHOT.jar"]

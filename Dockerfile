# initialize build and set base image for first stage
FROM maven:3.6.3-openjdk-17 as stage1
# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

# Définissez le répertoire de travail dans le conteneur
WORKDIR /app

# Copiez le fichier pom.xml dans le conteneur à /app
COPY pom.xml .

# Téléchargez les dépendances Maven (ceci permet de mettre en cache les dépendances si le pom.xml n'a pas changé)
RUN mvn dependency:go-offline

# Copiez le reste des fichiers dans le conteneur
COPY src ./src

# Compilez l'application avec Maven
RUN mvn clean install -Dmaven.test.skip=true

# Exposez le port 8080 (ou tout autre port utilisé par votre application)
EXPOSE 8080

# Commande de démarrage de l'application Spring Bootmvn
CMD ["java", "-jar", "target/projet_infra_3_backend-0.0.1-SNAPSHOT.jar"]

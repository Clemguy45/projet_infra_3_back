# Projet Infra 3 Back-end V.1.0.0

*Ce projet a été généré par [Spring Initializr](https://start.spring.io/) version 2.7.17-SNAPSHOT.*

*Ce README ne concerne que la partie **back-end**. Il est donc demandé de consulter le README de la partie **front-end** pour que l'intégralité du projet soit fonctionnelle.*

Dans ce projet, nous regroupons l'ensemble des cartes existantes à l'aide de l'API `Yu-Gi-Oh! API by YGOPRODeck`


## Initialisation

Si vous utilisez le code source du projet, lancez la commande suivante pour démarrer le serveur :
```
docker compose up --build   
```

Dirigez-vous maintenant à l'adresse [http://localhost:8080/](http://localhost:8080/).

## Base de données

La BDD utilisé pour ce projet est de type `NoSql`. C'est une BDD `MongoDB`.

Elle est hébergé sur `Atlas MongDB`. 

## Securité

### *SEM GREP* 

Nous avons utilisé **SEM GREP** pour la sécurité du projet. Ce logiciel analyse s'il y a des failles dans l'application. 

### *DependaBOT*     

DependaBOT est un bot **GitHub** qui analyse le projet et met à jour les dépendances *(notamment du pom.xml)* pour empêcher des failles de sécurité de l'application en créant de nouvelles branches.

## Swagger

Swagger va permettre de générer la documentation de l'API du **back-end**.

Cette dernière est consultable [en cliquant ici](https://projet-infra-3-backend-dev.azurewebsites.net/swagger-ui/index.html)

## Versioning

Pour le versioning, nous avons utilisé **GitHub**. Les branches sont visibles depuis le repository **GitHub** [en cliquant ici](https://github.com/Clemguy45/projet_infra_3_back).

## Azure

L'application est hébergée sur Azure et maintenue à jour grâce à la pipeline hébergée avec GitHub Actions.

Vous pouvez retrouver l'entièreté du projet [en cliquant ici](https://portal.azure.com/#@univ-orleans.fr/resource/subscriptions/fd0e5100-031c-4d74-9e95-41224c6d1b5d/resourceGroups/projet-infra-3/overview)

## Diagrammes

Le diagramme est disponible dans le **back-end** à la racine de ce projet.

## Auteurs

* GUYOT Clément
* POUDROUX Mickael
* TSONGOUANG SONGFACK Lauriche
* BLOT Benjamin

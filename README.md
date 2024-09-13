# Châtop

## Description
Ce projet est une application **Spring Boot** permettant de gérer des locations (rentals). Les utilisateurs peuvent consulter, créer, et mettre à jour des locations.

L'application utilise **Swagger** pour la documentation

## Prérequis
- **JDK 17+**
- **Maven 3.6+**
- **Base de données MySQL**
- **Postman**

## Installation
### Cloner le dépôt
git clone https://github.com/msjemenblc/api-chatop.git
cd api-chatop

### Configuration de la base de données
Dans le fichier src/main/resources/application.properties, configurez votre connexion à MySQL. Exemple de configuration :

spring.datasource.url=jdbc:mysql://localhost:3306/nom_de_la_base
spring.datasource.username=nom_utilisateur
spring.datasource.password=mot_de_passe
spring.jpa.hibernate.ddl-auto=update

### Accéder à l'application
Une fois démarrée, l'application sera accessible à l'adresse suivante :

http://localhost:8080

### Documentation de l'API avec Swagger
L'API dispose d'une documentation Swagger pour faciliter les tests et la visualisation des endpoints :

http://localhost:8080/swagger-ui.html
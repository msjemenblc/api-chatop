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
```
git clone https://github.com/msjemenblc/api-chatop.git
cd api-chatop
```

### Configuration de la base de données
Générez une nouvelle base de données, suivi de ce script pour la création des tables :

```
DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rental_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3ce1i9w1rtics9wjwj8y5y3md` (`rental_id`),
  KEY `FKpsmh6clh3csorw43eaodlqvkn` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `rentals`;
CREATE TABLE IF NOT EXISTS `rentals` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `surface` decimal(38,2) DEFAULT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `owner_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf462yhxa9vd3m2qdmcoixg1fv` (`owner_id`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `USERS_index` (`email`(100))
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

Dans le fichier src/main/resources/application.properties, configurez votre connexion à MySQL. Exemple de configuration :

``` 
  spring.datasource.url=jdbc:mysql://localhost:3306/nom_de_la_base
  spring.datasource.username=nom_utilisateur
  spring.datasource.password=mot_de_passe
  spring.jpa.hibernate.ddl-auto=update
```

### Accéder à l'application
Une fois démarrée, l'application sera accessible à l'adresse suivante :

```
http://localhost:3001
```

Utilisez Postman pour tester les différents endpoints référencés dans la documentation Swagger.

Vous pouvez aussi utiliser le frontend mis à disponisition sur ce repository :

https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring

## Documentation de l'API avec Swagger
L'API dispose d'une documentation Swagger pour faciliter les tests et la visualisation des endpoints :

http://localhost:3001/swagger-ui/index.html

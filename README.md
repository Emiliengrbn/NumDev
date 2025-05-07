# MonProjet

Ce projet est composé d’un front-end Angular et d’un back-end Spring Boot 2.6 (Java 17), avec une base de données MySQL.

Ce fichier README explique comment installer, configurer et utiliser l’application, ainsi que comment lancer les tests et générer les rapports de couverture.

---

## Table des matières

- [Pré-requis](#pré-requis)
- [Installation de la base de données](#installation-de-la-base-de-données)
- [Installation du back-end](#installation-du-back-end)
- [Installation du front-end](#installation-du-front-end)
- [Lancement de l’application](#lancement-de-lapplication)
- [Lancement des tests](#lancement-des-tests)
- [Génération des rapports de couverture](#génération-des-rapports-de-couverture)
- [Auteurs](#auteurs)

---

## Pré-requis

- Java 17
- Maven
- Node.js (version 14 ou supérieure)
- npm ou yarn
- MySQL 5.7 ou supérieur

---

## Installation de la base de données

1. **Créer la base de données**

   - Rendez-vous dans le dossier ressources>sql et recopiez les script dans votre terminal.

2. **Configurer la connexion dans le back-end**

Modifier le fichier `src/main/resources/application.properties` (ou `application.yml`) du back-end :

```
spring.datasource.url=jdbc:mysql://localhost:3306/yoga_studio?allowPublicKeyRetrieval=true
spring.datasource.username=youruser
spring.datasource.password=yourpassword

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=true
oc.app.jwtSecret=yourjwtsecret
oc.app.jwtExpirationMs=86400000
```

---

## Installation du back-end

1. Se placer dans le dossier du back-end :

`cd backend`

2. Compiler et installer les dépendances Maven :

`mvn clean install`

3. Lancer le back-end :

`mvn spring-boot:run`

---

## Installation du front-end

1. Se placer dans le dossier du front-end :

`cd frontend`

2. Installer les dépendances npm :

`npm i`

ou

`yarn install`

3. Lancer le front-end :

`ng serve`

Le front-end sera accessible sur : [http://localhost:4200](http://localhost:4200)

---

## Lancement des tests

### Back-end

`mvn test`

Pour le rapport :

`mvn clean test jacoco:report`

### Front-end

``ng test`

Pour le e2e :

`ng e2e`

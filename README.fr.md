<div align="center">

# Bivouac N Go Backend

<a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-nd/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/">Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License</a>.

---

### **Description**

Ceci est le backend de l'application Bivouac N Go. 

---

</div>

## Table of Contents

- [Organisation du repo](#organisation-du-repo)
- [Lancer l'application](#lancer-lapplication)
    - [Pré-requis](#pré-requis)
    - [Mode Développement](#mode-développement)
    - [Mode Production](#mode-production)
    - [Setup des bases de données](#setup-des-bases-de-données)
- [Contributions](#contributions)
    - [Authors](#authors)

# Organisation du repo
<sup>[(Back to top)](#table-of-contents)</sup>

L'architecture du backend est en microservice. 

Chaque microservice est dans un module. Il y a quatre microservices/modules:

- api_gateway
- user_microservice
- location_microservice
- reservation_microservice

Voici l'organisation des fichiers:

```declarative
.
├── api_gateway
│   ├── build.gradle.kts
│   ├── Dockerfile
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── fr
│       │   │       └── polytech
│       │   │           ├── ApiGatewayApplication.java
│       │   │           └── JWTAuthorizationFilter.java
│       │   └── resources
│       │       └── application.properties
│       └── test
│           └── java
│               └── fr
│                   └── polytech
│                       └── ApiGatewayApplicationTests.java
├── docker-compose.prod.yml
├── docker-compose.yml
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── location_microservice
│   ├── build.gradle.kts
│   ├── Dockerfile
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── fr
│       │   │       └── polytech
│       │   │           ├── BivouacngoApplication.java
│       │   │           ├── controllers
│       │   │           │   ├── AssetController.java
│       │   │           │   └── LocationController.java
│       │   │           ├── KafkaConsumerService.java
│       │   │           ├── models
│       │   │           │   ├── Asset.java
│       │   │           │   ├── Image.java
│       │   │           │   └── Location.java
│       │   │           └── repositories
│       │   │               ├── AssetRepository.java
│       │   │               ├── ImageRepository.java
│       │   │               └── LocationRepository.java
│       │   └── resources
│       │       └── application.properties
│       └── test
│           └── java
│               └── fr
│                   └── polytech
│                       └── BivouacngoApplicationTests.java
├── README.md
├── reservation_microservice
│   ├── build.gradle.kts
│   ├── Dockerfile
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── fr
│       │   │       └── polytech
│       │   │           ├── BivouacngoApplication.java
│       │   │           ├── controllers
│       │   │           │   └── ReservationController.java
│       │   │           ├── KafkaConsumerService.java
│       │   │           ├── models
│       │   │           │   └── Reservation.java
│       │   │           └── repositories
│       │   │               └── ReservationRepository.java
│       │   └── resources
│       │       └── application.properties
│       └── test
│           └── java
│               └── fr
│                   └── polytech
│                       └── BivouacngoApplicationTests.java
├── settings.gradle.kts
├── sql
│   ├── base_location_db.dump
│   ├── base_reservation_db.dump
│   ├── base_user_db.dump
│   ├── dump_databases.sh
│   ├── load_database_dumps.ps1
│   ├── load_database_dumps.sh
│   ├── location.sql
│   ├── reservation.sql
│   ├── restore_databases.ps1
│   ├── restore_databases.sh
│   ├── setup_sql.ps1
│   ├── setup_sql.sh
│   └── user.sql
├── start.ps1
├── start.sh
└── user_microservice
    ├── build.gradle.kts
    ├── Dockerfile
    └── src
        ├── main
        │   ├── java
        │   │   └── fr
        │   │       └── polytech
        │   │           ├── BivouacngoApplication.java
        │   │           ├── controllers
        │   │           │   └── UserController.java
        │   │           ├── dto
        │   │           │   ├── LoginRequest.java
        │   │           │   └── LoginResponse.java
        │   │           ├── KafkaProducerService.java
        │   │           ├── models
        │   │           │   └── User.java
        │   │           └── repositories
        │   │               └── UserRepository.java
        │   └── resources
        │       └── application.properties
        └── test
            └── java
                └── fr
                    └── polytech
                        └── BivouacngoApplicationTests.java

```


# Lancer l'application
<sup>[(Back to top)](#table-of-contents)</sup>
 
Voici un guide pour lancer le backend en mode developpement ou en mode production

## Pré-requis
<sup>[(Back to top)](#table-of-contents)</sup>

Pour la version développeur, il faut au moins gradle 8.10.2 et Java 21.

Pour la version production et développeur il faut Docker et Docker Compose.

Avoir au moins quelques Gigas de stockage libre pour être confortable.

## Mode Développement
<sup>[(Back to top)](#table-of-contents)</sup>

Il y a un script start qui compile les modules, build les Dockers et lance le Docker compose.

Sur Linux:

```declarative
./start.sh
```

Sur Windows PowerShell:

```declarative
.\start.ps1
```

Important: Pour le mode développement, lancer le backend AVANT le frontend, sinon vous risquez d'avoir un conflit de ports.

Si vous souhaitez lancer l'application plusieurs fois, une fois que vous avez build une fois et qu'il n'y a pas de modifications dans le code, il suffit de run, sur Linux et Windows:

```declarative
docker compose up
```

# Mode production
<sup>[(Back to top)](#table-of-contents)</sup>

Le fichier ```docker-compose.prod.yml``` pull les images de production du Container Registry de Gitlab et lance l'application.

Sur Linux et sur Windows PowerShell:

```declarative
docker compose -f docker-compose.prod.yml up
```

# Setup des bases de données
<sup>[(Back to top)](#table-of-contents)</sup>

Pour remplir les base de données, assurez vous d'abord que le backend est lancé.

Ouvrez un autre terminal dans ce projet.

Puis lancez le script qui rempli les bases de données:

Sur Linux:
```declarative
cd sql
./load_database_dumps.sh
```

Sur Windows PowerShell:
```declarative
cd sql
.\load_database_dumps.ps1
```

Normalement le backend est pret sur l'adresse ```http://localhost:8083```.

### Reset des bases de données

Si vous souhaitez completement reset les bases des données pour quelconque raison:

1. Arretez tous les containers de l'application
2. Supprimez les volumes docker: ```docker volume rm <nom_du_volume>```
3. Lancer ```docker compose up```
4. Lancer le script pour charger les bases de données: ```./load_database_dumps.(sh ou ps1)```

# Contributions
<sup>[(Back to top)](#table-of-contents)</sup>

## Authors
<sup>[(Back to top)](#table-of-contents)</sup>

- [**DELOIRE Alexandre**](https://gitlab.polytech.umontpellier.fr/alexandre.deloire01)
- [**JORGE Rémi**](https://gitlab.polytech.umontpellier.fr/remi.jorge)
- [**HE Jiayi**](https://gitlab.polytech.umontpellier.fr/jiayi.he)




<div align="center">

# Bivouac N' Go Backend

<a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-nd/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/">Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License</a>.

---

<img src="images/logo.png" alt="logo" width="350px;">

### **Description**

Bivouac N’ Go is the Airbnb for camping! This platform allows landowners to list their spaces for rent, enabling campers to find and book unique outdoor locations effortlessly. Whether seeking an isolated retreat or a site near hiking trails, Bivouac N’ Go simplifies the camping experience by providing an intuitive and friendly platform. This is the backend repository for the Bivouac N' Go project. It is a microservice architecture that provides the backend services for the Bivouac N' Go project.

---

</div>

**Please read the the thourough Documentation provided:**
- [Bivouac N'Go Documentation Part 1](Bivouac_N_Go_Documentation_Part1.pdf)
- [Bivouac N'Go Documentation Part 2](Bivouac_N_Go_Documentation_Part2.pdf)
- [Bivouac N'Go Documentation Part 3](Bivouac_N_Go_Documentation_Part3.pdf)


French README : [README.fr.md](README.fr.md)
<a href="README.fr.md"><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Flag_of_France.svg/1200px-Flag_of_France.svg.png" width="20" height="15" alt="French version"></a>


## Table of Contents

- [Features](#features)
- [Microservice Architecture](#microservice-architecture)
- [UI/UX](#uiux)
- [MCD (Model Conceptual Design)](#mcd-model-conceptual-design)
- [CI/CD](#cicd)
- [Test Campaigns](#test-campaigns)
- [Technologies Used](#technologies-used)
- [Production Deployment](#production-deployment)
- [Repo Organisation](#repo-organisation)
- [Lauching the Application](#launching-the-application)
- [Development Mode](#development-mode)
- [Production Mode](#production-mode)
- [Database Setup](#database-setup)
- [Documentation](#documentation)
- [Frontend](#frontend)
- [Contributions](#contributions)
- [Authors](#authors)

## Features
<sup>[(Back to top)](#table-of-contents)</sup>

**User Authentication & Profile Management**

- Create an account.
- Modify account information (name, phone number, email, etc.).
- Request account deletion along with all associated data to comply with GDPR regulations.
- Log in to the application.

**Host Features**

- List spaces (cabins, land, etc.) for booking, allowing other users to make reservations.
- Specify available amenities and services when listing their space.
- Manage their listed spaces (edit, remove, etc.).
- View all listed properties and track active or past booking requests.
- Accept or decline reservation requests for their properties.
- Rate and review campers after their stay.

**Camper Features**

- Request reservations for available spaces by selecting dates and group size, with an optional message to the host.
- Search for locations based on filters (dates, number of people, location, etc.).
- View locations on an interactive map.
- Rate and review bookings after the stay.
- View reviews left by hosts about their bookings.
- Track all reservations and their statuses (active, declined, past).
- Access booking history.

**Admin Features**

- Approve account deletion requests.
- Define available amenities and services that hosts can list when renting out their spaces.

**Additional Features**

- Integrated messaging system.
- Smart recommendations for camping spots.
- Custom widgets for mobile devices.
- Identity verification (ID, driver’s license, etc.).

# Microservice Architecture
<sup>[(Back to top)](#table-of-contents)</sup>

To ensure better separation of responsibilities and a clear organization of features, we structured the application around three distinct microservices.

![microservices_diagram](images/microservices_diagram.png)


### Communication
We implemented an API Gateway as the single entry point for all external requests directed to the API. To achieve this, we used Spring Cloud, which centralizes request management and routing to the appropriate microservices. For communication between microservices, we used HTTPS requests and Apache Kafka.

### Security
We implemented Spring Security, a dependency that enables the setup of security mechanisms (authentication and access controls) within each microservice.

### Push Notifications
We used Firebase Cloud Messaging to handle push notifications for the application.

### Database
Each microservice has its own PostgreSQL database.

### Containerization and Container Orchestration
Each microservice consists of two Docker images: one for the Spring Boot application and one for the database. The API Gateway is also containerized in a Docker container. Since the microservices are deployed on a single machine, the containers are orchestrated using Docker Compose.


# UI/UX
<sup>[(Back to top)](#table-of-contents)</sup>

The UI screenshots are organized into five categories, reflecting the main sections of the mobile app as shown in the navigation bar: Profile (Account Management), Explore, My Trips, and My Rentals. For users with dual roles, the Admin section is seamlessly integrated into the Profile.

The color palette used in the screens was carefully chosen to align with our brand identity. Each screen was thoughtfully designed with a strong focus on ergonomics and user experience (UI/UX), ensuring intuitive and seamless navigation across the app.

![ui_1](images/ui_1.png)

**On the left:** The interface when the user tries to access features other than EXPLORE without being logged in.

**On the right:** When the user wants to create an account.

![ui_2](images/ui_2.png)

**On the left:** When the user clicks on PROFILE after logging in.

**On the right:** When the user clicks on DELETE MY ACCOUNT.

![ui_3](images/ui_3.png)

**On the left:** When the user clicks on MY RENTALS, section My Rentals.

**On the right:** When the user clicks on Show Details of one of their rentals.

![ui_4](images/ui_4.png)

**On the left:** When the user clicks on ADD A RENTAL.

**On the right:** When the user clicks on MY RENTALS, section Booking Request.

![ui_5](images/ui_5.png)

**On the left:** User account interface when the user is an Admin.

**On the right:** When the admin clicks on Go To Admin Page, section Rental Management.

![ui_6](images/ui_6.png)

**On the left:** When the admin clicks on Go To Admin Page, section User Management.

**On the right:** When the admin clicks on Details of a user request.

![ui_7](images/ui_7.png)

**On the left:** When the user clicks on EXPLORE.

**On the right:** When the user clicks on the search bar to add parameters.

![ui_8](images/ui_8.png)

**On the left:** When the user clicks on Show Details of one of the search results.

**On the right:** When the user clicks on LOCATION of a property to display it on the map.

![ui_9](images/ui_9.png)

**On the left:** When the user clicks on BOOK.

**On the right:** After filling in the stay information by clicking on BOOK NOW.

![ui_10](images/ui_10.png)

**On the left:** When the user clicks on MY TRIPS.

**On the right:** When the user clicks on Review to leave stars and feedback.


# MCD (Model Conceptual Design)
<sup>[(Back to top)](#table-of-contents)</sup>

This is the theoretical MCD:

![mcd_diagram](images/mcd_diagram.png)

Since we used a microservice architecture, this is the MCD adapted to this architecture:

![mcd_microservices_diagram](images/mcd_microservices_diagram.png)

# CI/CD
<sup>[(Back to top)](#table-of-contents)</sup>

We implemented CI for the backend with several actions:

- Running tests with Gradle
- Building the project with Gradle
- Building Docker images
- Pushing the images to the GitLab Container Registry

The CI also leverages Git commit tags to automatically build tagged Docker images, enabling the release of new application versions.

Here is a flowchart of the CI/CD:

![ci_cd_diagram](images/ci_cd_diagram.png)

The different stages:

![ci_cd_gitlab](images/ci_cd_gitlab.png)


# Test Campaigns
<sup>[(Back to top)](#table-of-contents)</sup>

### Testing Strategy
We developed a formal document to define the strategy to be used throughout the project.

This includes the definition of:
- Test scope
- Test environment
- Stakeholders
- Timeline
- Methodology

![test_strategy_doc](images/test_strategy_doc.png)

### Static Testing
Measures taken include:
- Reviewing commits on GitLab  
- Code reviews  
- Peer programming  

### Automatic Tests

Automatic tests are done in the CI/CD pipeline as seen above.

### System Testing
A dedicated workflow was set up with a specific structure for organizing and executing system tests.

We did two campaigns of testing, here was the planning:

![test_planning](images/test_planning.png)

We followed a thorough protocol to ensure efficient testing of each and every feature.

Each feature has an extensive test plan:

![feature_test](images/feature_test.png)

That we go through before each release to make sure everything works, specifying if each step is a pass or fail and providing screenshots and documentation when something goes wrong:

![feature_test_execution](images/feature_test_execution.png)

The CI allows us to tag releases:

![ci_cd_tag](images/ci_cd_tag.png)

### First and Second Campaign

- The v1.0 version was released using a CI tag.
- The first full testing campaign was executed on the v1.0 release.
- After applying corrective measures, version v1.1 was released using a CI tag.
- A second testing campaign was executed to confirm that the issues were resolved and to ensure no regressions were introduced.

# Technologies used
<sup>[(Back to top)](#table-of-contents)</sup>

### Redux Toolkit
- We used Redux Toolkit on the frontend to manage authentication. This includes securely storing and retrieving tokens via AsyncStorage, handling login and registration requests, and managing related errors.

- This approach allowed us to centralize user state and ensure efficient synchronization with the user interface.

### API Gateway: Spring Cloud
We used Spring Cloud as our API Gateway to centralize communication between our three microservices. It serves as a single entry point for all external requests.

Key benefits include:
- Simplified access management and security policy enforcement  
- Optimized routing to the appropriate microservices  
- Centralized handling of cross-cutting concerns such as authentication and error management  
- Easier scalability and independent deployment of microservices  

### Security: JWT Token Management
We implemented token-based authentication using JWT to secure access to our microservices.

- Every request (except those related to login) sent to the API Gateway includes a JWT in the header.
- The JWT is decoded to retrieve the user’s ID and permissions.
- API routes are protected with role-based access controls.
- Sensitive resources (e.g., admin routes) are accessible only to authorized users.
- Tokens have a limited lifespan (24 hours) and are regenerated whenever roles or permissions are updated.

### Apache Kafka & Zookeeper
We integrated Kafka to enable asynchronous and scalable communication between microservices.

- Microservices communicate by publishing and subscribing to Kafka topics.
- Example: When a user is deleted, a message is sent to relevant topics so that other microservices are notified and can remove any related data accordingly.
- Zookeeper is used to manage and coordinate Kafka brokers, ensuring stability and synchronization across the system.

### Docker & Docker Compose
- Each microservice, along with the API Gateway, is containerized using Docker to ensure isolation and portability.

- Databases, Kafka, and Zookeeper also run in separate Docker containers.

- All containers are orchestrated using Docker Compose, which greatly simplifies starting, stopping, and deploying the application in a production environment.


# Production Deployment
<sup>[(Back to top)](#table-of-contents)</sup>

A `docker-compose.prod.yml` file has been set up for deployment on a production server. 

Simply run Docker Compose with this file, and it will automatically download the latest version of the images from the GitLab Container Registry.

Bash scripts have also been created to configure and populate the databases.


# Repo organisation
<sup>[(Back to top)](#table-of-contents)</sup>


The backend follows a microservices architecture.

Each microservice is organized as its own module. There are four microservices/modules:

- `api_gateway`
- `user_microservice`
- `location_microservice`
- `reservation_microservice`

Below is the project file structure:

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

# Launching the Application
<sup>[(Back to top)](#table-of-contents)</sup>

This guide explains how to run the backend in either development or production mode.

## Prerequisites
<sup>[(Back to top)](#table-of-contents)</sup>

For development mode, you'll need at least **Gradle 8.10.2** and **Java 21**.

For both development and production modes, **Docker** and **Docker Compose** are required.

Make sure you have a few gigabytes of free disk space for a smooth experience.

## Development Mode
<sup>[(Back to top)](#table-of-contents)</sup>

A `start` script is provided to compile the modules, build the Docker images, and start everything with Docker Compose.

On **Linux**:

```bash
./start.sh
```

On **Windows PowerShell**:

```bash
.\start.ps1
```

**Important**: For development mode, make sure to start the backend before the frontend to avoid port conflicts.

If you want to restart the app and no code changes were made, you can simply run (on both Linux and Windows):

```bash
docker compose up
```

# Production Mode
<sup>[(Back to top)](#table-of-contents)</sup>

The `docker-compose.prod.yml` file pulls the production images from GitLab's Container Registry and launches the app.

On **Linux** and **Windows PowerShell**:

```bash
docker compose -f docker-compose.prod.yml up
```

# Database Setup
<sup>[(Back to top)](#table-of-contents)</sup>

To populate the databases, first ensure the backend is up and running.

Then open a new terminal in the project directory and run the following script:

On **Linux**:

```bash
cd sql
./load_database_dumps.sh
```

On **Windows PowerShell**:

```
cd sql
.\load_database_dumps.ps1
```

Once complete, the backend should be accessible at:

`http://localhost:8083`

# Resetting the Databases
<sup>[(Back to top)](#table-of-contents)</sup>

If you need to completely reset the databases:

1. Stop all running containers

2. Remove the Docker volumes:

```bash
docker volume rm <volume_name>
```

3. Start the containers again:

```bash
docker compose up
```

4. Reload the databases:

```bash
./load_database_dumps.sh
```

or

```bash
.\load_database_dumps.ps1
```

# Documentation
<sup>[(Back to top)](#table-of-contents)</sup>

There is a lot more detail about each part of this project in the documentation:

- [Bivouac N'Go Documentation Part 1](Bivouac_N_Go_Documentation_Part1.pdf)
- [Bivouac N'Go Documentation Part 2](Bivouac_N_Go_Documentation_Part2.pdf)
- [Bivouac N'Go Documentation Part 3](Bivouac_N_Go_Documentation_Part3.pdf)

# Frontend
<sup>[(Back to top)](#table-of-contents)</sup>

The frontend of this project can be found [here](https://github.com/alexdeloire/camping-mobile-app-frontend)


# Contributions
<sup>[(Back to top)](#table-of-contents)</sup>

## Authors
<sup>[(Back to top)](#table-of-contents)</sup>

- [**Alexandre Deloire**](https://github.com/alexdeloire)
- [**Jiayi He**](https://github.com/JiayiHE95)
- [**Rémi Jorge**](https://github.com/RemiJorge)
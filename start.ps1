./gradlew user_microservice:bootJar
./gradlew location_microservice:bootJar
./gradlew reservation_microservice:bootJar
./gradlew api_gateway:bootJar

docker compose build

docker compose up

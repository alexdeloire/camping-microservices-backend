# Use Amazon Corretto 21 as the base image
FROM amazoncorretto:21

# Set working directory inside the container
WORKDIR /app

# Copy the build artifact into the container
COPY build/libs/*.jar app.jar

# Define the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

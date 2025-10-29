# Stage 1: Build the application using a Maven image
# FIX: Changed the base image tag to a reliable one
FROM maven:3-openjdk-17 AS builder 
WORKDIR /app
COPY . /app
RUN mvn clean install -DskipTests

# Stage 2: Create the final image using a slim JRE for efficiency
FROM openjdk:17-jre-slim
WORKDIR /app

# Install wget if not present (to get the jetty-runner)
# Note: openjdk:17-jre-slim may need packages installed, but for simplicity, 
# we'll try to rely on the most basic image functionality first.
# If wget is not found, we'll manually copy the runner from the builder stage.

# Copy the Jetty Runner JAR from the Maven Local Repository inside the builder image
# This is more robust than relying on wget in the final slim stage.
# NOTE: The path for the local Maven repository (where dependencies are stored) is usually /root/.m2/repository
COPY --from=builder /root/.m2/repository/org/eclipse/jetty/jetty-runner/9.4.43.v20210718/jetty-runner-9.4.43.v20210718.jar /app/jetty-runner.jar

# Copy the built WAR file from the builder stage's target directory
COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /app/todo-app.war

# Set the port and command to run the WAR file using Jetty
# Use 0.0.0.0 for the host binding, though Jetty runner binds automatically
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "8080", "todo-app.war"]

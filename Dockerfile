# ----------------------------------------------------------------------------------
# STAGE 1: BUILDER
# Purpose: Compile Java, resolve dependencies, and prepare runtime assets.
# ----------------------------------------------------------------------------------
FROM maven:3-openjdk-17 AS builder 
WORKDIR /app
COPY . /app

# FIX: Install wget and update package lists so the download command succeeds.
# This fixes the "wget: command not found" error.
RUN apt-get update && apt-get install -y wget 

# 1. Execute the Maven build. This generates the WAR file.
RUN mvn clean install -DskipTests

# 2. Download the Jetty Runner JAR.
RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.4.43.v20210718/jetty-runner-9.4.43.v20210718.jar -O target/jetty-runner.jar

# ----------------------------------------------------------------------------------
# STAGE 2: RUNNER
# Purpose: Create a small, efficient runtime environment (JRE only) to execute the WAR.
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built artifacts from the builder stage
COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /app/todo-app.war
COPY --from=builder /app/target/jetty-runner.jar /app/jetty-runner.jar

# Specify the container port
EXPOSE 8080

# The command to start the application
ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "8080", "todo-app.war"]

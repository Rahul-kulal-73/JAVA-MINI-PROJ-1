# ----------------------------------------------------------------------------------
# STAGE 1: BUILDER
# Purpose: Compile the Java code, package the WAR file, and download the Jetty Runner.
# ----------------------------------------------------------------------------------
# FIX: Using the verified Maven 3 and OpenJDK 17 combination tag
FROM maven:3-openjdk-17 AS builder 

WORKDIR /app

# Copy all project files into the Docker image
COPY . /app

# 1. Execute the Maven build. This compiles the code and generates the WAR file 
#    in the target directory (e.g., /app/target/todo-app-1.0-SNAPSHOT.war).
RUN mvn clean install -DskipTests

# 2. Download the Jetty Runner JAR into the target directory, making it easy to copy later.
RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.4.43.v20210718/jetty-runner-9.4.43.v20210718.jar -O target/jetty-runner.jar

# ----------------------------------------------------------------------------------
# STAGE 2: RUNNER
# Purpose: Create a small, efficient runtime environment (JRE only) to execute the WAR.
# ----------------------------------------------------------------------------------
# FIX: Corrected tag to 'slim-jre' for a lightweight runtime image
FROM openjdk:17-slim-jre
WORKDIR /app

# Copy the built artifacts from the builder stage
# Copy the compiled WAR file
COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /app/todo-app.war

# Copy the downloaded Jetty Runner JAR
COPY --from=builder /app/target/jetty-runner.jar /app/jetty-runner.jar

# Specify the port the container should listen on (Render requires the process to bind to $PORT)
# While Render sets the $PORT variable, we use the literal "8080" here as a placeholder 
# for Jetty, and Render's infrastructure handles the final port mapping.
EXPOSE 8080

# The command to start the application (replaces the Procfile)
ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "8080", "todo-app.war"]

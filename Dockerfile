# ----------------------------------------------------------------------------------
# STAGE 1: BUILDER
# Purpose: Compile the Java code, package the WAR file, and download the Jetty Runner.
# ----------------------------------------------------------------------------------
FROM maven:3-openjdk-17 AS builder 
WORKDIR /app
COPY . /app

# 1. Execute the Maven build.
RUN mvn clean install -DskipTests

# 2. Download the Jetty Runner JAR into the target directory.
# This JAR is needed to execute the WAR file in the final stage.
RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.4.43.v20210718/jetty-runner-9.4.43.v20210718.jar -O target/jetty-runner.jar

# ----------------------------------------------------------------------------------
# STAGE 2: RUNNER
# Purpose: Create a small, efficient runtime environment (JRE only) to execute the WAR.
# ----------------------------------------------------------------------------------
# FIX: Using the highly reliable Eclipse Temurin JRE on Alpine Linux.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built artifacts from the builder stage
# Copy the compiled WAR file (adjust the name if your Maven artifact name changes)
COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /app/todo-app.war

# Copy the downloaded Jetty Runner JAR
COPY --from=builder /app/target/jetty-runner.jar /app/jetty-runner.jar

# Expose the standard web port (Render automatically handles mapping)
EXPOSE 8080

# The command to start the application: java -jar [runner] --port [port] [war file]
ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "8080", "todo-app.war"]

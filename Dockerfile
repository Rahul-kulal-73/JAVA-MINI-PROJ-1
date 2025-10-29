# Stage 1: Build the application using Maven and download Jetty Runner
FROM maven:3-openjdk-17 AS builder 
WORKDIR /app
COPY . /app
# 1. Run the build
RUN mvn clean install -DskipTests
# 2. Download Jetty Runner into the target directory
RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.4.43.v20210718/jetty-runner-9.4.43.v20210718.jar -O target/jetty-runner.jar

# Stage 2: Create the final image using a slim JRE for efficiency
# FIX: Corrected tag from '17-jre-slim' to the actual Docker Hub tag '17-slim-jre'
FROM openjdk:17-slim-jre
WORKDIR /app

# Copy the built WAR file and the Jetty Runner JAR from the builder stage
COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /app/todo-app.war
COPY --from=builder /app/target/jetty-runner.jar /app/jetty-runner.jar

# Set the port and command to run the WAR file using Jetty
EXPOSE 8080
# The $PORT environment variable is set by Render automatically for us to use.
# We pass 8080 as a placeholder to Jetty, but Render will route traffic correctly.
ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "8080", "todo-app.war"]

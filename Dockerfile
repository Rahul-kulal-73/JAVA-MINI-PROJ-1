# Stage 1: Build the application using a Maven image
FROM maven:3.8.7-openjdk-17 AS builder
WORKDIR /app
COPY . /app
RUN mvn clean install -DskipTests

# Stage 2: Create a lighter final image using only the JRE and copy the WAR file
FROM openjdk:17-jre-slim
WORKDIR /usr/local/tomcat/webapps
# Copy the built WAR file from the builder stage
COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Install Tomcat (or use Jetty runner as before)
# For simplicity and robust Tomcat-like behavior, let's stick to Jetty Runner approach:
# FROM openjdk:17-jre-slim
# WORKDIR /app
# COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /app/todo-app.war
# COPY --from=builder /root/.m2/repository/org/eclipse/jetty/jetty-runner/9.4.43.v20210718/jetty-runner-9.4.43.v20210718.jar /app/jetty-runner.jar
# ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "$PORT", "todo-app.war"]

# The simplest way to run a WAR file using a small container:
# Use the same setup as our Procfile for simplicity:
FROM openjdk:17-jdk-slim
WORKDIR /app
# Install Jetty Runner
RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.4.43.v20210718/jetty-runner-9.4.43.v20210718.jar -O jetty-runner.jar
# Copy the built WAR file
COPY --from=builder /app/target/*.war /app/todo-app.war
# Expose the standard web port (though Render maps it)
EXPOSE 8080
# Start the application using the runner
ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "8080", "todo-app.war"]

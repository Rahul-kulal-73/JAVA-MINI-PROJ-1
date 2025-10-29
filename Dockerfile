# ----------------------------------------------------------------------------------
# STAGE 1: BUILDER - Uses a clean, reliable Temurin JDK image (based on Debian/Ubuntu)
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jdk AS builder 
WORKDIR /app
COPY . /app

# 1. Install Maven and wget using apt-get (This base image guarantees apt-get exists)
RUN apt-get update && \
    apt-get install -y maven wget && \
    apt-get clean

# 2. Execute the Maven build.
RUN mvn clean install -DskipTests

# 3. Download the Jetty Runner JAR.
RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.4.43.v20210718/jetty-runner-9.4.43.v20210718.jar -O target/jetty-runner.jar

# ----------------------------------------------------------------------------------
# STAGE 2: RUNNER (No change, this part was confirmed fine)
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built artifacts
COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /app/todo-app.war
COPY --from=builder /app/target/jetty-runner.jar /app/jetty-runner.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "8080", "todo-app.war"]

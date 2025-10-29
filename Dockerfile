# ----------------------------------------------------------------------------------
# STAGE 1: BUILDER
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jdk AS builder 
WORKDIR /app
COPY . /app

RUN apt-get update && \
    apt-get install -y maven wget && \
    apt-get clean

RUN mvn clean install -DskipTests

# 3. Download the Jetty Runner JAR.
# FIX: Changed version from 9.4.43.v20210718 to 9.4.53.v20231009 to avoid 404 error
RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-runner/9.4.53.v20231009/jetty-runner-9.4.53.v20231009.jar -O target/jetty-runner.jar

# ----------------------------------------------------------------------------------
# STAGE 2: RUNNER
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# The WAR file name is constant: todo-app-1.0-SNAPSHOT.war
COPY --from=builder /app/target/todo-app-1.0-SNAPSHOT.war /app/todo-app.war

# FIX: Copy the NEW version of the Jetty Runner
COPY --from=builder /app/target/jetty-runner.jar /app/jetty-runner.jar

EXPOSE 8080

# The command to start the application
ENTRYPOINT ["java", "-jar", "jetty-runner.jar", "--port", "8080", "todo-app.war"]

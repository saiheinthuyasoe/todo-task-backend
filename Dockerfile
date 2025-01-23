# Use Maven to build the application
# maven:3.9.9-amazoncorretto-23-al2023 ---> Get from Docker Hub
FROM maven:3.9.9-amazoncorretto-23-al2023 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#####################################################
# Use OpenJDK to run the application
# openjdk:25-slim-bullseye ---> Get from Docker Hub
# {todotask-0.0.1.jar} ---> The jar file will be created in the target directory
# /app/target/ --> if you use Intellija IDEA, the jar file will be created in this directory. If you don't see target folder on VScode run this code --> mvn clean install

FROM openjdk:25-ea-5
WORKDIR /app
COPY --from=build /app/target/todotask-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
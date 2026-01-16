# Build stage
FROM maven:3.8.4-eclipse-temurin-11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Package stage
FROM eclipse-temurin:11-jre
COPY --from=build /home/app/target/timezone-converter-0.0.1-SNAPSHOT.jar /usr/local/lib/timezone-converter.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/timezone-converter.jar"]

FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DSkipTests

FROM openjdk:17.0.1-jdk-slim 
COPY --from=build target/demo-0.0.1-SNAPSHOT.jar mojKvart.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mojKvart.jar"]



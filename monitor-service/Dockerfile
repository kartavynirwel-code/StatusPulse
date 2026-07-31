FROM maven:3.9-eclipse-temurin-21 as build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app
COPY --from=build /app/target/monitor-service-0.0.1-SNAPSHOT.jar ./monitor-service.jar

EXPOSE 8080
CMD ["-jar", "monitor-service.jar"]
### BUILDER IMAGE ###
FROM maven:3-jdk-11-slim as builder

COPY pom.xml /build/

RUN mvn --file build/pom.xml --batch-mode package

COPY src /build/src/

RUN mvn --file build/pom.xml --batch-mode package -DskipTests


### PRODUCTION IMAGE ###
FROM openjdk:11-jre-slim

COPY --from=builder build/target/app-*.jar app/app.jar

WORKDIR /app

EXPOSE 4567

CMD ["java", "-jar", "app.jar"]

# Base image olarak Maven ve OpenJDK kullanıyoruz
FROM maven:3.8.5-openjdk-17 AS builder
psql

WORKDIR /backend


COPY . ./
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk


ARG JAR_FILE=target/*.jar


WORKDIR /app


COPY --from=builder /app/${JAR_FILE} app.jar

ENV SPRING_LOCAL_PORT=${SPRING_LOCAL_PORT}
ENV SPRING_DOCKER_PORT=${SPRING_DOCKER_PORT}

ENV ANOTHER_POSTGRES_DB=${ANOTHER_POSTGRES_DB}
ENV ANOTHER_POSTGRES_USER=${ANOTHER_POSTGRES_USER}
ENV ANOTHER_POSTGRES_PASSWORD=${ANOTHER_POSTGRES_PASSWORD}

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

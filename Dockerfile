FROM  eclipse-temurin:21-jdk AS builder
ARG MS_NAME=fpc-backend
WORKDIR /app

COPY ./pom.xml ./mvnw ./mvnw.cmd /app/
COPY .mvn /app/.mvn/
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve

COPY ./src /app/src/
RUN ./mvnw clean package -DskipTests

FROM  eclipse-temurin:21-jdk
WORKDIR /app
RUN mkdir ./logs

COPY --from=builder /app/target/app-1.0.jar /app/app-1.0.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app-1.0.jar"]
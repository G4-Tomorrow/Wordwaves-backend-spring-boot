FROM maven:3.9.8-amazoncorretto-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Xây dựng ứng dụng
RUN mvn package -DskipTests

FROM amazoncorretto:21.0.4

WORKDIR /app

# Copy file jar từ build stage
COPY --from=build /app/target/*.jar app.jar


ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3.9.8-amazoncorretto-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Xây dựng ứng dụng
RUN mvn package -DskipTests

FROM amazoncorretto:21.0.4

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Thiết lập biến môi trường
ENV DATABASE_URL=jdbc:mysql://ugy3xdlg4yzsgnbc:AJMQxWffY78sikU7JJjx@bv50k19rodxkhj0lbdvb-mysql.services.clever-cloud.com:3306/bv50k19rodxkhj0lbdvb
ENV DATABASE_USERNAME=ugy3xdlg4yzsgnbc
ENV DATABASE_PASSWORD=AJMQxWffY78sikU7JJjx
ENV JWT_SIGNER_KEY=6FvxQMx9pcbEFqKFFP4XH748Kg//XkcCtf68+/P7ZRvIXnba3dG7POT/cUCAWV5Q
ENV BREVO_APIKEY=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
ENV BREVO_SENDER_EMAIL=kaitoukid00204@gmail.com

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]

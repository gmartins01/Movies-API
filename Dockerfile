FROM maven:3.9.6-amazoncorretto-17-al2023

WORKDIR /app

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn package -DskipTests


EXPOSE 8080

CMD ["java", "-jar", "target/movies-application.jar"]

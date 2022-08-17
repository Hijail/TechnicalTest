FROM openjdk:jdk
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -Dmaven.test.skip=true

RUN cp ./target/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "./app.jar"]
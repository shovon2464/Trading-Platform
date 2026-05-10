FROM maven:3.9.6-amazoncorretto-21 AS build-tradingserver

WORKDIR /temp_tradingserver

COPY pom.xml ./
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B clean package -DskipTests -Dcheckstyle.skip

FROM amazoncorretto:21

WORKDIR /tradingserver

COPY --from=build-tradingserver /temp_tradingserver/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

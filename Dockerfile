FROM openjdk:17-ea-11-jdk-slim as build
ENV APP_HOME=/apps/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN chmod +x gradlew
RUN ./gradlew build || return 0
COPY src $APP_HOME/src
RUN ./gradlew -x test clean build

FROM openjdk:17-ea-11-jdk-slim
ENV APP_HOME=/apps
ARG ARTIFACT_NAME=app.jar

ARG JAR_FILE_PATH=build/libs/tiny-human-api-0.0.1-SNAPSHOT.jar
WORKDIR $APP_HOME
COPY --from=build $APP_HOME/$JAR_FILE_PATH $ARTIFACT_NAME
COPY firebase_admin_sdk.json $APP_HOME/

RUN mkdir -p $APP_HOME/logs/error

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "app.jar"]

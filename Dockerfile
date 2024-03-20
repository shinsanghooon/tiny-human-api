# JDK 17
FROM openjdk:17-alpine

# 8080 포트로 배포
EXPOSE 8080

# 작업 공간 이동
WORKDIR /app
# Jar 파일 경로
ARG JAR_FILE_PATH=build/libs/*.jar
# Jar 파일 복제
COPY ${JAR_FILE_PATH} app.jar

# Jar 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
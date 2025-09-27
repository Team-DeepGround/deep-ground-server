FROM openjdk:17-jdk-slim

# 시간대 설정
ENV TZ=Asia/Seoul

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 컨테이너가 8080 포트에서 통신하도록 설정합니다.
EXPOSE 8080

# 컨테이너가 실행될 때 실행할 명령어
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
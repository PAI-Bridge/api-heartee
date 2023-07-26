# Docker 이미지의 기본 이미지를 선택합니다. Java 11을 사용하는 것으로 가정합니다.
FROM adoptopenjdk:11-jdk

# 작업 디렉토리를 /app 으로 설정합니다.
WORKDIR /app

# Gradle Wrapper 를 복사합니다.
COPY gradlew .
COPY gradle gradle

# build.gradle 파일을 복사하여 의존성을 설치합니다.
COPY build.gradle .

# 소스 코드를 복사합니다.
COPY src src

# Gradle 빌드를 수행합니다. (로컬 실행 용도)
RUN ./gradlew build

# 빌드된 JAR 파일을 /app 디렉토리로 복사합니다. (로컬 실행 용도)
RUN mv build/libs/*-SNAPSHOT.jar /app/api-heartee.jar

# JAR 파일을 복사합니다. (ec2 배포 용도 - ec2 메모리 사양 이슈)
# COPY api-heartee.jar api-heartee.jar

# Spring Boot 어플리케이션을 실행합니다.
CMD ["java", "-jar", "api-heartee.jar"]
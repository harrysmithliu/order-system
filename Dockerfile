# 使用官方 Maven 构建镜像
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 复制 pom 和源码
COPY pom.xml .
RUN mvn -q -e -U -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# 运行阶段
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

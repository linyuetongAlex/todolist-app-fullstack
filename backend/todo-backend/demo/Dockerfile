# 第一阶段：构建 —— 用一个装好了Maven+JDK17的临时环境，把源代码编译打包成jar
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行 —— 换成一个更"轻量"的、只装了JRE（Java运行环境）的环境，把第一阶段打包好的jar复制过来运行
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
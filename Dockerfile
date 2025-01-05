# 使用OpenJDK 21作为基础镜像
FROM eclipse-temurin:21-jdk

### 设置环境变量
#ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
#    JDBC_DATABASE_URL=jdbc:mysql://mysql:3306/your_database?useSSL=false \
#    JDBC_DATABASE_USER=your_username \
#    JDBC_DATABASE_PASSWORD=your_password \
#    MINIO_ENDPOINT=minio:9000 \
#    MINIO_ACCESS_KEY=your_access_key \
#    MINIO_SECRET_KEY=your_secret_key

# 将Spring Boot应用添加到镜像中
ADD target/APIStream-1.1.1.jar app.jar

# 暴露8080端口（Spring Boot默认端口）
EXPOSE 8080

# 启动Spring Boot应用
ENTRYPOINT ["java", "-jar", "app.jar"]
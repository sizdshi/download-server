FROM openjdk:8 AS backend

MAINTAINER hcshi

WORKDIR /app

COPY server/target/download-server-0.0.1-SNAPSHOT.jar /home/app.jar
RUN ls -al /app
RUN ls -al /home
# 暴露后端应用的端口
EXPOSE 8110

# 启动后端应用
CMD ["java", "-jar", "app.jar"]

# 构建前端应用
FROM node:18.15 AS frontend

# 设置工作目录
WORKDIR /app

## 复制前端应用文件到容器中
#COPY web/ ./
#
## 安装依赖
#RUN npm install
#
## 构建前端应用
#RUN npm run build

# 阶段2: 使用 Nginx 镜像作为基础镜像
FROM nginx:1.21

COPY nginx.conf /etc/nginx/nginx.conf

# 复制前端构建文件到 Nginx 的默认 HTML 目录
COPY  web/dist /usr/share/nginx/html

# 暴露前端应用的端口
EXPOSE 8080


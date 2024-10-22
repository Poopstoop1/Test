# Use uma imagem base com Java
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo jar gerado para o container
COPY target/Mesa-0.0.1-SNAPSHOT.jar /app/Mesa.jar

# Exponha a porta que a aplicação vai usar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/Mesa.jar"]

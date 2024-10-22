# Use uma imagem base com Java
FROM openjdk:21-jdk-slim

# Instale o Maven
RUN apt-get update && apt-get install -y maven

# Copie o código fonte para o container
COPY . /app

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Compile o projeto com Maven
RUN mvn clean package -DskipTests

# Copie o arquivo jar gerado para o container
COPY target/Mesa-0.0.1-SNAPSHOT.jar /app/Mesa.jar

# Exponha a porta que a aplicação vai usar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/Mesa.jar"]

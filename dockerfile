# Stage 1: Build
FROM maven:3.9.3-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar pom.xml primero para cachear dependencias
COPY pom.xml .

# Descargar dependencias offline
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir JAR sin tests
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copiar el JAR generado
COPY --from=build /app/target/recipe-service-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que usa el microservicio
ENV PORT=8001
EXPOSE ${PORT}

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
# --- 1. Aşama: Build (Derleme) ---
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Testleri atlayarak build alıyoruz (hız için)
RUN mvn clean package -DskipTests

# --- 2. Aşama: Runtime (Çalıştırma) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# İlk aşamadan oluşan jar dosyasını kopyalıyoruz
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
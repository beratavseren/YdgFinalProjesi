# --- 2. Aşama: Runtime (Çalıştırma) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# İlk aşamadan oluşan jar dosyasını kopyalıyoruz
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
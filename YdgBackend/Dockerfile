FROM eclipse-temurin:21-jre
# Çalışma dizini proje adı oldu
WORKDIR /YdgBackend

# Build aşamasındaki /YdgBackend/target altından alıp, buraya YdgBackend.jar olarak kopyalıyoruz
COPY --from=build /YdgBackend/target/*.jar YdgBackend.jar

# Başlatma komutu
ENTRYPOINT ["java", "-jar", "YdgBackend.jar"]
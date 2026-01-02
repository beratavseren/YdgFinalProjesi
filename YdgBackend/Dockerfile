FROM eclipse-temurin:21-jre-alpine

# Uygulama klasörü
WORKDIR /YdgBackend

# Dışarıda oluşturulmuş (target klasöründeki) jar dosyasını
# içeriye "YdgBackend.jar" adıyla kopyalıyoruz.
COPY target/*.jar YdgBackend.jar

# Uygulamayı başlatıyoruz
ENTRYPOINT ["java", "-jar", "YdgBackend.jar"]
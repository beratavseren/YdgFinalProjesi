FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache wget

# Uygulama klasörü
WORKDIR /YdgBackend

# Dışarıda oluşturulmuş (target klasöründeki) jar dosyasını
# içeriye "YdgBackend.jar" adıyla kopyalıyoruz.
COPY target/*.jar YdgBackend.jar

# Uygulamayı başlatıyoruz
ENTRYPOINT ["java", "-jar", "YdgBackend.jar"]
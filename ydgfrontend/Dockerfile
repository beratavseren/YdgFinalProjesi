# --- 1. Aşama: Build (Derleme) ---
FROM node:20-alpine AS builder

# Çalışma dizinini ayarla
WORKDIR /app

# Önce sadece paket dosyalarını kopyala (Docker cache optimizasyonu için)
COPY package.json package-lock.json ./

# Bağımlılıkları yükle
RUN npm install

# Kaynak kodun geri kalanını kopyala
COPY . .

# Uygulamayı derle (genelde 'dist' veya 'build' klasörü oluşturur)
RUN npm run build

# --- 2. Aşama: Production (Sunucu) ---
FROM nginx:alpine

# Nginx yapılandırma dosyasını kopyala (Aşağıda oluşturacağız)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 1. aşamada oluşturulan build dosyalarını Nginx'in hizmet verdiği klasöre kopyala
# NOT: Eğer Vite kullanıyorsan '/app/dist', Create React App (CRA) kullanıyorsan '/app/build' yazmalısın.
COPY --from=builder /app/dist /usr/share/nginx/html

# 80 portunu dışarı aç
EXPOSE 80

# Nginx'i başlat
CMD ["nginx", "-g", "daemon off;"]
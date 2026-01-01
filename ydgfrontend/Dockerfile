# --- 1. Aşama: Build (Derleme) ---
FROM node:20-alpine AS builder

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .

ARG REACT_APP_API_URL

ENV REACT_APP_API_URL=$REACT_APP_API_URL

RUN npm run build

# --- 2. Aşama: Production (Sunucu) ---
FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

# DÜZELTME BURADA: /app/dist yerine /app/build yazıyoruz
COPY --from=builder /app/build /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
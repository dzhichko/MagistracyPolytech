
# Используем официальный образ Node.js
FROM node:18-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем зависимости и исходный код
COPY package*.json ./
COPY css/ ./css/
COPY img/ ./img/
RUN npm install --global live-server  # Устанавливаем live-server глобально

# Копируем все файлы приложения
COPY . .

# Открываем порт 3000
EXPOSE 2222

# Запускаем сервер (важно: используем 0.0.0.0 для доступа извне контейнера)
CMD ["live-server", "--port=2222", "--host=0.0.0.0", "/app"]
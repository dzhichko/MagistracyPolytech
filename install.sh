#!/bin/bash

echo "🧪 Запуск тестов..."
docker compose run --rm tests

if [ $? -eq 0 ]; then
  echo "✅ Тесты прошли успешно. Запускаем backend..."
  docker compose up -d --no-build
else
  echo "❌ Тесты не прошли. Backend не будет запущен."
  exit 1
fi
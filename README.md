# MagistracyPolytech
команда: Джичко Андрей, Кашмак Станислав, Муратов Алексей, Спиридонов Владимир (у всех группа 5130904/20105)

тема: 
Разработка информационного сайта для поступающих в магистратуру Политеха

проблема: 
На данный момент нет удобного сервиса для получения информации о доступных вариантах обучения в магистратуре, требованиях к поступающим и планах обучения. Пользователям необходимо вручную открывать приказ по каждой специальности и искать информацию в нем.

 
стек: Postgres, Java, API политеха, JavaScript



Требования:

- Когда пользователь ищет подходящее направление, он хочет использовать поле поиска, чтобы быстро искать программы подходящие его критериям

- Когда пользователь выбирает направление (условно нажимает курсором по названию), он хочет увидеть информацию по программе обучения в удобном виде (на этой же странице, не скачивая дополнительных файлов), чтобы не тратить лишнее время для поиска информации

- Когда меняется документ с информацией по направлению, пользователь хочет получать об этом уведомление на почту, чтобы своевременно получать актуальную информацию по интересующим направлениям



Архитектура:

Общая идея сервиса
Сервис предоставляет пользователям (абитуриентам, студентам, администраторам) доступ к актуальной информации о направлениях магистратуры. Основные операции – это чтение данных (номер направления, краткое название, описание, дата обновления файлов), а также редкие операции записи (обновление информации раз в сутки).

Характер нагрузки и соотношение R/W
Чтение (Read):
  - Около 80–90% запросов составляют операции чтения.
  - При 10 000 запросов в сутки нагрузка распределяется 	равномерно, с возможными пиковыми значениями в периоды 	повышенного интереса (например, в начале приёмной кампании).
Запись (Write):
  - Обновление информации о направлениях происходит раз в сутки, а также возможны точечные корректировки администраторами.
Длительное хранение:
  - Система должна обеспечивать хранение исторических данных за 5 лет, включая дату обновления файлов, что требует надёжного резервного копирования и возможности масштабирования БД.

Архитектура системы (модель C4)

Context Diagram

   ![image](https://github.com/user-attachments/assets/5922abb6-580e-403a-9d21-9534867dce38)
   
Container Diagram

   ![image](https://github.com/user-attachments/assets/72495ffc-0a37-4031-9635-6180eac5102d)


Frontend (Web):
  - Пользовательский интерфейс для просмотра списка направлений, фильтрации, поиска и получения подробной информации.
  
API Gateway:
  Централизованная точка входа, которая:
  - Выполняет маршрутизацию запросов к нужным экземплярам Spring Boot приложения.
  - Реализует балансировку нагрузки, аутентификацию, авторизацию и ограничение скорости запросов (rate limiting).
  - Может кэшировать часто запрашиваемые данные для ускорения ответа.
  
Backend (Spring Boot):
  Обрабатывает бизнес-логику, связанную с направлением магистратуры:
  - Обеспечивает REST API для получения списка направлений, деталей и обновления информации.
  - Интегрирован с API Gateway для распределения нагрузки.
  
База данных (PostgreSQL):
  Хранит таблицу направлений с полями:
  - Номер направления
  - Краткое название
  - Описание
  - Дата обновления файлов
  - Дата и время последнего изменения записи
  - Дополнительная таблица для пользователей для обеспечения разграничения прав доступа.

  
Контракты API и нефункциональные требования

REST API (на базе Spring Boot):
  - Предоставляет операции для получения списка направлений, получения детальной информации, а также для создания и обновления записей (доступно администраторам).
  - Обновление данных происходит автоматически раз в сутки, с возможностью ручной корректировки.
  
Нефункциональные требования:

  Время отклика:
  - Чтение – до 200–300 мс для 95% запросов.
  - Запись – до 500 мс.
  Надёжность:
  - Регулярное резервное копирование данных, репликация PostgreSQL для повышения отказоустойчивости.
  Безопасность:
  - Аутентификация и авторизация (например, с использованием JWT), централизованная обработка безопасности через API Gateway.
  
Масштабирование и отказоустойчивость

API Gateway:
  - Позволяет масштабировать систему горизонтально за счёт маршрутизации запросов на несколько экземпляров backend-приложения.
  - Обеспечивает централизованное управление безопасностью, кэшированием и балансировкой нагрузки, что особенно актуально при росте запросов (например, при увеличении нагрузки до 10 раз).
Backend и база данных:
  - Запуск нескольких экземпляров Spring Boot приложения за балансировщиком (под капотом API Gateway).
  - Масштабирование PostgreSQL посредством добавления реплик для обработки большого количества запросов на чтение.



Тестирование:

Были реализованы unit тесты для всех имеющихся сервисов для проверки передаваемых значений внутри сервера

Интеграционные тесты в нашем случае проверяют корректоность взаимодействия с базой данных, механизма авторизации пользователя и взаимодейтсвия с эндпоинтами сервера


Также было проведено нагрузочное тестирование с использованием инструмента JMeter, которое позволило проверить соблюдение требований по нагрузке. По итогу система отработала удовлетворительно


Сборка проекта:

Сборка выполняется с помощью bash скрипта (install.sh), который в свою очередь сначала разворачивает тестовые контенеры и выполняет тестирование по всем сценариям. После чего при успешном прохождении тестов запускается основной docker compose сценарий и разворачиваются 4 контейнера: redis, postgres, backend, frontend

После развертывания программы пользователь может перейти на страницу нашего сайта в браузере (по адресу localhost:2222 или 127.0.0.1:2222, что одно и то же)

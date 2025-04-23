# Backend API Учебного Центра

Этот проект представляет собой RESTful-сервер для управления учебным центром. Построен с использованием **Spring Boot**, **Spring Security**, **JWT**, **PostgreSQL** и **JavaMailSender**. Поддерживает регистрацию пользователей, управление курсами, запись на курсы и отправку email-уведомлений.
Мини обзор на тест задание https://youtu.be/paF8AnQ4WNQ

---

## 📦 Реализованные возможности

- **Регистрация и авторизация пользователей**
  - Пользователи могут зарегистрироваться и получить JWT-токен.
  - Безопасная авторизация с шифрованием пароля (BCrypt).
  - Назначение роли по умолчанию (`USER`).

- **Безопасность через JWT**
  - Аутентификация без сохранения состояния с использованием JWT.
  - Кастомный фильтр (`JwtFilter`) для проверки токена.
  - Защищённые маршруты, доступные только при наличии валидного токена.

- **Управление курсами**
  - Администраторы могут создавать, редактировать и удалять курсы.
  - Аутентифицированные пользователи могут просматривать курсы.
  - Запись на курс через `/courses/{id}/register`.
  - Просмотр студентов, записанных на курс.
  - Удаление пользователя с курса (ADMIN).

- **Email-уведомления**
  - Приветственное письмо при регистрации.
  - Уведомление при успешной записи на курс.

- **Swagger-документация**
  - Документация OpenAPI 3 доступна по адресу `/swagger-ui.html` или `/swagger-ui/index.html`.

---

## ⚙️ Используемые технологии

- **Java 21**
- **Spring Boot 3.4.4**
- **Spring Data JPA**
- **Spring Security 6.4.4**
- **PostgreSQL**
- **JWT (библиотека JJWT)**
- **Lombok**
- **JavaMailSender**
- **Swagger (springdoc-openapi)**

---

## 📁 Структура проекта (упрощённая)

```
src/main/java/com/example/demo
├── controller       // REST-контроллеры
├── model            // Сущности (AppUser, Course, Role)
├── repository       // Репозитории JPA
├── service          // Бизнес-логика
├── security         // JWT и Spring Security
├── email            // EmailService для уведомлений
└── DemoApplication  // Точка входа
```

---

## ▶️ Запуск приложения

### 1. Настройка PostgreSQL
Убедитесь, что PostgreSQL работает, и пропишите логин/пароль в `application.properties`:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/learningcenter
spring.datasource.username=ИМЯ_ПОЛЬЗОВАТЕЛЯ
spring.datasource.password=ПАРОЛЬ
```

### 2. Настройка почты (пример с Gmail)
```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=ВАШ_EMAIL@gmail.com
spring.mail.password=ПАРОЛЬ (в пропертис мой стоит)
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 3. Запуск с помощью Maven
```bash
mvn spring-boot:run
```

---

## 🔐 Основные API-маршруты

### Аутентификация
- `POST /auth/register` – регистрация нового пользователя
- `POST /auth/login` – вход и получение токена

### Курсы
- `POST /courses` – создание курса (только ADMIN)
- `GET /courses` – просмотр всех курсов
- `GET /courses/{id}` – получить информацию о курсе по ID
- `PUT /courses/{id}` – обновление курса (только ADMIN)
- `DELETE /courses/{id}` – удаление курса (только ADMIN)
- `POST /courses/{id}/register` – запись на курс (нужен токен)
- `DELETE /courses/{courseId}/remove/{userId}` – удалить пользователя с курса (только ADMIN)
- `GET /courses/{courseId}/students` – просмотр студентов на курсе с фильтрацией по имени/почте

### Swagger
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- для постман стоит файл **Learning_Center_Postman_Collection**

---

## ✅ Заметки
- Для регистрации можно использовать только email в качестве логина (например, `user@gmail.com`).
- Для тестирования через Swagger можно временно отключить защиту JWT.
- Обработка ошибок реализована при регистрации и записи на курс.

---

## 📬 Контакты
@beka_24k тг


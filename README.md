# 🚢 Customs Declaration Status Tracker

[![Java](https://img.shields.io/badge/Java-17-%23ED8B00)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-%236DB33F)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-%23336791)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-%232496ED)](https://www.docker.com/)

Система мониторинга статусов таможенных деклараций с веб-интерфейсом и API для интеграции.

## 📌 Основные функции

- Отслеживание статусов деклараций (Принята, Проверка, Выпуск)
- REST API для запроса текущего статуса
- Уведомления через WebHook/SSE
- Фоновая проверка "зависших" деклараций (каждый час)
- Ролевая модель доступа:
    - 👔 Таможенный инспектор (полный доступ)
    - 📝 Декларант (просмотр своих деклараций)
    - 🔧 Администратор (управление пользователями)

## 🛠 Технологический стек

| Компонент       | Технология                          |
|-----------------|-------------------------------------|
| Бэкенд         | Java 17, Spring Boot 3.1            |
| База данных    | PostgreSQL 15                       |
| Сборка         | Gradle                              |
| Контейнеризация| Docker                              |
| Фронтенд       |   |

## 🚀 Быстрый старт

### Требования
- JDK 17+
- Docker 20+
- PostgreSQL 15+

### Запуск через Docker
```bash
docker-compose up --build
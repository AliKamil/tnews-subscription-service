# TNEWS

## Описание | Description

TNEWS - это сервис для персонализированного получения новостей. Проект построен по микросервисной архитектуре и включает в себя три сервиса:

TNEWS is a service for personalized news delivery. The project follows a microservice architecture and includes three services:

- **subscription-service** – управляет подписками пользователей и хранит их в PostgreSQL.  
  Manages user subscriptions and stores them in PostgreSQL.
- **aggregator-service** – агрегирует новости с портала Dzen (и потенциально других источников), хранит их в MongoDB и раз в заданный период времени подбирает новости по подпискам.  
  Aggregates news from the Dzen portal (and potentially other sources), stores them in MongoDB, and periodically selects news based on subscriptions.
- **client-service** – обеспечивает связь между агрегатором новостей и пользователями через Telegram-бота, передавая пользователям отфильтрованные новости и принимая их команды.  
  Acts as a bridge between the news aggregator and users via a Telegram bot, delivering filtered news and processing user commands.

## Технологии | Technologies

- **Backend**: Java, Spring Boot
- **Базы данных | Databases**: PostgreSQL, MongoDB
- **Контейнеризация | Containerization**: Docker
- **Сборка | Build System**: Gradle
- **Тестирование | Testing**: JUnit, H2, Flapdoodle

## Запуск проекта | Running the project

### Требования | Requirements

- Установленный Docker и Docker Compose  
  Installed Docker and Docker Compose
- Java 21
- Gradle
- PostgreSQL и MongoDB (либо запуск через Docker)  
  PostgreSQL and MongoDB (or launch via Docker)

### Конфигурация | Configuration

Перед запуском необходимо задать токен Telegram-бота в файле конфигурации (`application.yml` или переменных окружения):

Before launching, set the Telegram bot token in the configuration file (`application.yml` or environment variables):

```yaml
bot:
  name: YOUR_TELEGRAM_BOT_NAME
  token: YOUR_TELEGRAM_BOT_TOKEN
```

### Сборка и запуск через Docker | Build and run with Docker

Перед запуском Docker необходимо собрать проект с помощью Gradle:

Before starting Docker, build the project using Gradle:

```sh
gradle build
```

```sh
docker-compose up -d
```

## Использование | Usage

- Пользователь подписывается на категории и ключевые слова через Telegram-бота.  
  Users subscribe to categories and keywords via the Telegram bot.
- Агрегатор собирает новости, фильтрует их и отправляет пользователю в соответствии с подписками.  
  The aggregator collects, filters, and sends news to users based on subscriptions.
- Новости обновляются автоматически с заданным интервалом.  
  News is updated automatically at a set interval.

## TODO

- Добавить поддержку дополнительных источников новостей  
  Add support for additional news sources.





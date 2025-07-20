# Arcathoria API

> Backend REST API for the Arcathoria game

---

## Description

**Arcathoria API** is a multi-module backend application implemented with Java 17 and Spring Boot, following a Domain-Driven Design (DDD) and Hexagonal Architecture. It provides HTTP endpoints to manage players, characters, items, and to simulate battles between characters and monsters.

## Features

- **Player Management (CRUD):** Create, read, update, and delete player accounts.
- **Character Management:** Create and level up characters associated with players.
- **Battle System:** Initiate and resolve battles between characters and monsters using `BattleService`.
- **Item Management:** Handle inventories, item creation, purchasing, and selling.
- **Leaderboard:** Retrieve player rankings, with caching support via Redis.

## Project Structure

```
arcathoria-api/
├── build.gradle            # Root Gradle configuration for all modules
├── settings.gradle         # Subproject definitions
├── compose.yml             # Docker Compose definitions (Postgres, Redis, pgAdmin)
├── bootstrap/              # Bootstrapping module (Spring Boot application)
├── domain/                 # Core business logic (entities, value objects, ports, services)
├── infrastructure/         # Infrastructure adapters (JPA, Redis, messaging)
├── app/                    # Application layer (REST controllers, use cases, DTOs)
└── shared-test-helpers/    # Shared utilities for testing
```

## Requirements

- Java 17 or higher
- Gradle 7 or higher
- Docker & Docker Compose (for local development environment)

## Installation & Running

1. **Clone the repository**
   ```bash
   git clone https://github.com/KrzychuuWEB/arcathoria-api.git
   cd arcathoria-api
   ```

2. **Start supporting services** (PostgreSQL and Redis):
   ```bash
   docker-compose -f compose.yml up -d
   ```

3. **Configure application properties** (`src/main/resources/application-dev.yml`):
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/arcathoria
       username: postgres
       password: postgres
     redis:
       host: localhost
       port: 6379
   ```

4. **Build and run the API**:
   ```bash
   ./gradlew clean bootRun --projects bootstrap
   ```

## API Usage

- **Swagger UI / OpenAPI** is available at: `http://localhost:8080/swagger-ui.html`
- **Sample Endpoints:**
  - `POST /api/players` – Create a new player
  - `GET /api/players/{id}` – Retrieve player details by ID
  - `POST /api/characters` – Create a new character
  - `POST /api/battles` – Start a battle session

## Testing

Run unit and integration tests:
```bash
./gradlew test
```

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

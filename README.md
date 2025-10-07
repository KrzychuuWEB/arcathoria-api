# Arcathoria API

> Backend REST API for the Arcathoria game | Frontend: https://github.com/KrzychuuWEB/arcathoria-client

---

## Description

Web browser game which player takes on the role of the wizzard. At this point the player can login can create a
character and
start PvE combat. I want to develop a new system like equipment, shop, guild, rewards, level, skill, create
custom spells, buffs, debuffs and more... I solo develop game and I want to learn new technologies and get more
experience. Actual version is very basic I focused on combat PvE system. After added combat PvE system I want to finish
refining character and account system and I want focus on the new features.

## Features

- **Account System:** Register account.
- **Auth System:** Login and generate jwt token. Valid jwt token.
- **Character System:** Creation character and select character.
- **Combat System:** Init new PvE combat. Perform melee attack. Turn system. Choose combat side by strategy.
- **Monster System:** Loading monster from monster.json.

## Project Structure

```
arcathoria-api/
├── compose.yml                 # Docker Compose definitions (Postgres, Redis, pgAdmin)
├── bootstrap/                  # Start Spring Boot application
├── domain/                     # Core business logic (aggregate, value objects, ports, services) clean java
├── infrastructure/             # Infrastructure adapters (REST controllers, JPA, Redis, clients adapters)
├── app/                        # Application layer (Usecase, DTO, Exceptions, ports)
├── errors-srping-boot-starter/ # Starter for global exception handling and problem detail factory with messaging
├── shared-kerenl/              # Shared utilities for kernel
├── shared-test-helpers/        # Shared utilities for testing
└── shared-test-infrastucture/  # Shared infrastucture utilities for testing
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

2. **Start supporting services**:
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

## Testing

Run unit and integration tests:

```bash
./gradlew test
```

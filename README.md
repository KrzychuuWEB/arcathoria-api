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

## Coming soon features

- **Level system**
- **Skills system**
- **Reward system**
- **Equipment system**
- **Expedition system**
- **Spells creation system**
- **PvP system**
- **Resistance, buff, debuff system**
- **and more...**

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

## Stack

**Backend:**

- Java 17
- Spring Boot 3
- Spring Security
- Hibernate / Spring Data JPA
- REST API

**Architecture & Design:**

- Domain-Driven Design (DDD)
- Hexagonal Architecture (Ports & Adapters)

**Design Patterns & Principles:**

- SOLID, Clean Code
- Dependency Injection, Strategy, Factory, Facade and more...
- CQRS (basic usage)
- Separation of Concerns, DRY, KISS, YAGNI

**Database & Storage:**

- PostgreSQL
- Redis (cache/session storage)

**Testing & Infrastructure:**

- JUnit 5
- Testcontainers (integration testing)
- Docker / Docker Compose
- Mockito
- E2E module test with separated bounded context
- Tests helpers (fake implementation, tests config)

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
   docker-compose -f compose-prod.yml up -d --build
   ```

3. **Configure application properties** (`.env`):
   ```yaml
    Create file and complete environment (copy from .env-example)
   ```

## Testing

Run unit and integration tests:

```bash
./gradlew test
```

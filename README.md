# Sportify

A multi-sport REST API built with Spring Boot, demonstrating JWT authentication, relational data modeling, and a JSONB-based flexible schema design that supports arbitrarily different sports without per-sport code.

## Status

✅ **Complete** — Full authentication, CRUD API, personalization feature, and the core architectural thesis proven against real PostgreSQL.

## The Problem This Project Solves

Different sports track fundamentally different statistics. A basketball box score has points, rebounds, and assists. A soccer match has goals, cards, and shots on target. A rigid relational schema would need either:

- A separate table per sport (schema sprawl, painful to extend), or
- A single table with dozens of nullable columns (most of which are `NULL` for any given sport)

**Sportify's solution**: store sport-specific stats as a `Map<String, Object>` mapped directly to a PostgreSQL `jsonb` column via Hibernate's `@JdbcTypeCode(SqlTypes.JSON)`. One schema, one endpoint, one service — zero per-sport branching anywhere in the codebase.

This was proven live, not just designed on paper — see [Results](#results) below.

## Tech Stack

- **Spring Boot 4.1.0** / **Java 21** (current, modern generation — not a legacy 3.x setup)
- **Spring Security 7.1.0** — stateless JWT authentication
- **Spring Data JPA** / **Hibernate 7.4.1**
- **PostgreSQL 16** (production-style persistence; H2 for fast/isolated tests)
- **jjwt 0.13.0** — JWT generation/validation
- **JUnit 6** / **Mockito** / **MockMvc** — full test suite

## Features

- **JWT Authentication**: register/login, stateless sessions, BCrypt password hashing
- **Sport / Team / Player / Game CRUD**: full REST API with proper DTO layering (entities never exposed directly)
- **PlayerGameStat with JSONB flexible stats**: the core architectural feature — supports any sport's stat shape through one endpoint
- **Favorite Teams**: authenticated users can favorite/unfavorite teams, tied to their identity via JWT (`@AuthenticationPrincipal`)
- **Real business rule validation**: e.g., a team cannot play itself

## Results

The project's central thesis — one schema, many sports — was verified live against a running instance, then confirmed directly in PostgreSQL via `psql`:

**Basketball** (LeBron James, via `POST /api/player-game-stats`):
```json
{"points": 28, "rebounds": 11, "assists": 7}
```

**Soccer** (Bukayo Saka, same endpoint, same DTO, same database column):
```json
{"goals": 1, "assists": 2, "yellowCards": 0}
```

Confirmed directly in PostgreSQL:
```sql
sportify=# \d player_game_stat
   Column   |  Type  |
------------+--------+
 stats      | jsonb  |
```

Both stat shapes round-tripped correctly through the identical code path — no sport-specific DTOs, no conditional logic, no schema changes required to add a new sport.


## External Data Ingestion

`POST /api/admin/ingestion/league?league=English_Premier_League` pulls real team and player data from [TheSportsDB](https://www.thesportsdb.com)'s free public API and persists it into Sportify's own schema. Idempotent — safe to re-run; existing teams/players are skipped rather than duplicated.

```bash
curl -X POST "http://localhost:8080/api/admin/ingestion/league?league=English_Premier_League" \
  -H "Authorization: Bearer $TOKEN"
# {"teamsCreated":18,"playersCreated":350}
```

## API Overview

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/api/auth/register` | No | Create account, returns JWT |
| POST | `/api/auth/login` | No | Authenticate, returns JWT |
| GET/POST/DELETE | `/api/sports` | Yes | Sport CRUD |
| GET/POST/DELETE | `/api/teams` | Yes | Team CRUD |
| GET/POST/DELETE | `/api/players` | Yes | Player CRUD |
| GET/POST/PATCH/DELETE | `/api/games` | Yes | Game CRUD + score updates |
| GET/POST | `/api/player-game-stats` | Yes | Flexible JSONB stats |
| GET/POST/DELETE | `/api/users/me/favorite-teams` | Yes | Personalization |

All endpoints except `/api/auth/**` require a `Authorization: Bearer <token>` header.

## Setup

### Prerequisites

```bash
brew install openjdk@21 maven postgresql@16
```

Ensure `JAVA_HOME` points at Java 21:
```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:/opt/homebrew/opt/postgresql@16/bin:$PATH"
```

### Database

```bash
brew services start postgresql@16
createdb sportify
```

### Run

```bash
git clone https://github.com/VirajReddy10/sportify.git
cd sportify
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

### Test

```bash
./mvnw test
```

Runs the full suite (unit + integration tests) against an isolated in-memory H2 database — no PostgreSQL dependency for tests, no risk to real data.

## Quick Start: Try It Yourself

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"yourname","email":"you@example.com","password":"password123"}'

# Use the returned token for everything else
TOKEN="<paste the token from the response above>"

# Create a sport
curl -X POST http://localhost:8080/api/sports \
  -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Basketball"}'
```

## Architecture Notes

**Why JWT, not sessions**: a stateless API doesn't need server-side session storage, scales horizontally without sticky sessions, and is the standard pattern for API-first backends. CSRF protection is correctly disabled, since CSRF is a browser-cookie-session attack vector that doesn't apply to bearer-token APIs.

**Why DTOs, not exposing entities directly**: avoids two real problems — lazy-loading exceptions when Jackson tries to serialize a JPA proxy outside its transaction, and infinite recursion in bidirectional relationships. Every response is explicitly shaped by a `record` DTO with a `from(entity)` factory method.

**Why `@Transactional(readOnly = true)` on read methods**: several DTOs access lazy-loaded relationships (e.g., `team.getSport().getName()`). With `spring.jpa.open-in-view=false` (a deliberate choice — `open-in-view=true` is a well-known Spring Boot footgun that keeps DB connections open during view rendering), these lazy accesses need an active transaction, which `@Transactional` provides at the service layer where it belongs.

## Known Limitations / Future Work

- [x] ~~Real external sports data ingestion~~ — done: `POST /api/admin/ingestion/league?league={name}` pulls real teams and players from TheSportsDB's public API
- [ ] OpenAPI/Swagger documentation
- [ ] Database migrations via Flyway (currently `ddl-auto=update`, fine for development, not for production)
- [ ] JWT secret should come from an environment variable / secrets manager, not a properties file
- [ ] 401 vs 403 semantics for unauthenticated requests could be tightened with a custom `AuthenticationEntryPoint`

## Engineering Notes (Real Issues Encountered & Resolved)

This project was built on a genuinely current stack (Spring Boot 4.1, Spring Security 7.1, JUnit 6 — all released within the prior several months), which meant several real API changes had to be discovered and fixed by checking documentation directly, rather than relying on existing tutorials:

- **Spring Initializr's metadata API** reports `bootVersion` with a legacy `.RELEASE` suffix that no longer matches the actual Maven Central artifact (plain `4.1.0`, no suffix) — caught via a failed parent POM resolution.
- **`DaoAuthenticationProvider`'s constructor** takes a `UserDetailsService`, with `PasswordEncoder` set separately via `setPasswordEncoder()` — the inverse of what I initially assumed.
- **`org.springframework.lang.NonNull`** is deprecated in favor of JSpecify's `org.jspecify.annotations.NonNull`, part of Boot 4's null-safety modernization.
- **`@AutoConfigureMockMvc`** moved from `org.springframework.boot.test.autoconfigure.web.servlet` to `org.springframework.boot.webmvc.test.autoconfigure` as part of Boot 4's modularization — a change essentially no existing tutorial reflects yet.

Each was confirmed against official Spring documentation before fixing, rather than guessed twice.

## License

MIT

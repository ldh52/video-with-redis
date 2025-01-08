# Redis 를 활용하여 서비스 속도를 개선하는 틱X/유X브 서비스 개발

## 프로젝트 구조

```
mytv
├── adapter
│     ├── in                      
│     │    ├── api                // Rest API Controller
│     │    │     ├── advice       // ControllerAdvice
│     │    │     ├── attribute    // Header, Parameter Attribute
│     │    │     └── dto          // Request, Response DTO
│     │    └── resolver           // MethodArgumentResolver
│     └── out                     // PersistenceAdapter
│          ├── jpa                // JpaRepository
│          │     ├── channel
│          │     ├── subscribe
│          │     ├── user
│          │     └── video
│          ├── mongo              // MongoRepository
│          │     └── comment
│          └── redis              // RedisRepository
│                ├── channel
│                └── user
├── application                   // Service Application
│     ├── listener                // Subscribe Message Listener
│     ├── port                    // In/Out Port
│     │     ├── in
│     │     └── out
│     └── schedule                // Scheduled Task
├── common                        // Common Utils
├── config                        // Configuration
├── domain                        // Domain
│     ├── channel
│     ├── comment
│     ├── message
│     ├── user
│     └── video
└── exception                      // Custom Exception
```        
---

## 관련 라이브러리
### Embedded Redis[^1]
https://github.com/codemonstur/embedded-redis

### Embedded Mongo for Spring 3.x[^2]
https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo.spring

---

## Local 실행 환경
### MySQL, Redis, MongoDB start
`./tools` directory에서 \
`docker-compose up -d`

### MySQL, Redis, MongoDB stop
`docker-compose down`

### Spring Boot application 실행
`./gradlew bootRun`

## docker 실행 상태에서 DB/Redis 접근
### MySQL
`docker exec -it mytv-mysql bash` \
`mysql -u local -p`

### Redis
`docker exec -it mytv-redis sh` \
`redis-cli`

### MongoDB
`docker exec -it mytv-mongodb sh` \
`mongosh -u local -p local`

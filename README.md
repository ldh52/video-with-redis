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

---

## 참고
- Redis Command : https://redis.io/docs/latest/commands/
- Spring Data RedisTemplate 지원 Redis Command : https://docs.spring.io/spring-data/redis/reference/appendix.html

## 설치시 문제 해결
### MacOS Sonoma SSL 문제
```
library not loaded: /opt/homebrew/opt/openssl@3/lib/libssl.3.dylib
```
- 관련 설명 : https://github.com/codemonstur/embedded-redis?tab=readme-ov-file#ssltls-troubleshooting
- 해결 방법 : openssl 설치 
  - https://github.com/rvm/rvm/issues/5380
  - https://formulae.brew.sh/formula/openssl@3

[^1]: 기존에 많이 쓰이던 [ozimov embedded redis](https://github.com/ozimov/embedded-redis)가 추가 버전업이 없어서 최근에 대체 용도로 많이 쓰이는 라이브러리
[^2]: Spring 3.x 부터 [embedded mongo 가 제거](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes#springmongodbembeddedfeatures-configuration-property-removed)되고 3.x 를 위한 라이브러리 변경됨

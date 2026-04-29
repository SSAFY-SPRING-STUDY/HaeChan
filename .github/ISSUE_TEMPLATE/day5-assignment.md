---
name: "[Day 5] Spring Data JPA를 활용한 DB 영속화"
about: 인메모리 저장소를 제거하고 Spring Data JPA + MySQL로 실제 데이터를 영속화하는 과제
title: "[Day 5] Spring Data JPA를 활용한 DB 영속화"
labels: assignment, day5
assignees: ''
---

## 📌 과제 목표

1. `Spring Data JPA` 및 `MySQL Connector` 의존성을 추가한다
2. `Docker Compose`로 MySQL 8.0 데이터베이스 환경을 구성한다
3. 환경변수(`.env`)로 DB 접속 정보를 관리하고 `application.yml`에서 참조한다
4. `MemberEntity`, `PostEntity`를 JPA 엔티티(`@Entity`)로 전환한다
5. `MemberRepository`, `PostRepository`를 `JpaRepository`로 교체하여 인메모리 저장소를 제거한다
6. `@Transactional`을 적용하여 dirty checking 기반 수정 로직을 올바르게 동작시킨다

---

## 📦 패키지 구조

```
(Day 4와 동일 — 파일 구조 변경 없음)

추가 파일:
├── 🐳 docker-compose.yml     ← MySQL 컨테이너 설정
└── .env.example              ← 환경변수 예시 파일
```

---

## ⚙️ 환경 구성

### `build.gradle` (추가)

```groovy
runtimeOnly 'com.mysql:mysql-connector-j'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```

### `application.yml`

> `application.properties`에서 `application.yml`로 변환하고 아래 설정을 추가한다

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate.ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

### `docker-compose.yml`

```yaml
version: '3.8'

services:
  db:
    image: mysql:8.0
    container_name: mysql_container
    restart: always
    environment:
      MYSQL_DATABASE: ${DB_NAME}
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
    ports:
      - "${DB_PORT}:3306"
    volumes:
      - ./mysql_data:/var/lib/mysql
    networks:
      - backend_network

networks:
  backend_network:
    driver: bridge
```

### `.env.example`

```
DB_HOST=localhost
DB_PORT=3307
DB_NAME=your_db_name_here
DB_USERNAME=your_username_here
DB_PASSWORD=your_password_here
```

> `.env` 파일은 `.gitignore`에 등록하여 커밋하지 않는다

---

## 📋 엔티티 변환 명세

### `MemberEntity` (변경)

| 항목 | 내용 |
|------|------|
| 어노테이션 추가 | `@Entity`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)` |
| `id` 필드 | `@Id`, `@GeneratedValue(strategy = GenerationType.AUTO)` 추가 |
| 제거 항목 | `static AUTO_INCREMENT` 필드 및 관련 로직 → JPA가 자동 관리 |

**변경 전**
```java
@Getter
public class MemberEntity {
    private static long AUTO_INCREMENT = 1L;
    private Long id;
    // ...
    public static MemberEntity create(String username, String password, String nickname) {
        return new MemberEntity(AUTO_INCREMENT++, username, password, nickname);
    }
}
```

**변경 후**
```java
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // ...
    public static MemberEntity create(String username, String password, String nickname) {
        return new MemberEntity(username, password, nickname);
    }
}
```

> JPA는 리플렉션으로 객체를 생성하므로 `protected` 기본 생성자가 반드시 필요하다

### `PostEntity` (변경)

| 항목 | 내용 |
|------|------|
| 어노테이션 추가 | `@NoArgsConstructor(access = AccessLevel.PROTECTED)` |

> `@Entity`, `@Id`, `@GeneratedValue`, `@ManyToOne` 등 JPA 어노테이션은 Day 4에서 이미 추가되어 있다

---

## 📋 레포지토리 변환 명세

### `MemberRepository` (변경)

**변경 전** — `ConcurrentHashMap` 기반 인메모리 저장소

```java
@Repository
public class MemberRepository {
    private static final ConcurrentHashMap<Long, MemberEntity> store = new ConcurrentHashMap<>();

    public MemberEntity save(MemberEntity member) { ... }
    public Optional<MemberEntity> findById(Long memberId) { ... }
    public Optional<MemberEntity> findByUsername(String username) { ... }
}
```

**변경 후** — `JpaRepository` 상속

```java
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByUsername(String username);
}
```

> `save()`, `findById()`, `findAll()`, `delete()` 등 기본 CRUD는 `JpaRepository`가 자동 제공한다  
> 커스텀 쿼리(`findByUsername`)는 메서드 이름 규칙으로 자동 생성된다

### `PostRepository` (변경)

**변경 전** — `ConcurrentHashMap` 기반 인메모리 저장소

```java
@Repository
public class PostRepository {
    private static final ConcurrentHashMap<Long, PostEntity> store = new ConcurrentHashMap<>();

    public PostEntity save(PostEntity post) { ... }
    public Optional<PostEntity> findById(Long id) { ... }
    public List<PostEntity> findAll() { ... }
    public void delete(PostEntity post) { ... }
}
```

**변경 후** — `JpaRepository` 상속

```java
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
```

---

## 📋 서비스 변환 명세

### `PostService` (변경)

| 메서드 | 변경 사항 |
|--------|---------|
| `update(request, id, authorId)` | `@Transactional` 추가 — dirty checking으로 변경 감지 및 자동 저장 |

> `update` 메서드는 `post.update()`로 필드를 변경하지만 `save()`를 호출하지 않는다.  
> JPA dirty checking은 트랜잭션 내에서만 동작하므로 `@Transactional` 없이는 변경이 DB에 반영되지 않는다.

```java
@Transactional
public PostResponse update(PostRequest request, Long id, Long authorId) {
    // ...
    post.update(request.title(), request.content()); // save() 호출 없이 dirty checking으로 자동 반영
    return PostResponse.fromEntity(post);
}
```

---

## 🌐 API 명세

> Day 4와 동일 — API 스펙 변경 없음  
> 내부 저장소만 인메모리 → DB로 교체됨

---

## ✅ 구현 요구사항 체크리스트

**환경 구성**
- [ ] `build.gradle`에 `spring-boot-starter-data-jpa`와 `mysql-connector-j` 의존성이 추가되어 있다
- [ ] `application.yml`에 datasource와 JPA 설정이 있다
- [ ] `docker-compose.yml`로 MySQL 8.0 컨테이너를 실행할 수 있다
- [ ] `.env.example` 파일이 존재하고 `.env`는 `.gitignore`에 등록되어 있다

**엔티티 변환**
- [ ] `MemberEntity`에 `@Entity`와 `@NoArgsConstructor(access = AccessLevel.PROTECTED)`가 선언되어 있다
- [ ] `MemberEntity`의 `id` 필드에 `@Id`, `@GeneratedValue`가 선언되어 있다
- [ ] `MemberEntity`의 `AUTO_INCREMENT` 정적 필드가 제거되었다
- [ ] `PostEntity`에 `@NoArgsConstructor(access = AccessLevel.PROTECTED)`가 선언되어 있다

**레포지토리 변환**
- [ ] `MemberRepository`가 `JpaRepository<MemberEntity, Long>`을 상속한다
- [ ] `PostRepository`가 `JpaRepository<PostEntity, Long>`을 상속한다
- [ ] 인메모리 `ConcurrentHashMap` 저장소가 완전히 제거되었다

**서비스 변환**
- [ ] `PostService.update()`에 `@Transactional`이 적용되어 있다
- [ ] 애플리케이션 실행 후 서버를 재시작해도 데이터가 유지된다

---

## 💡 힌트

- `JpaRepository`를 상속하면 `save()`, `findById()`, `findAll()`, `deleteById()` 등을 별도로 구현하지 않아도 된다
- JPA 엔티티는 리플렉션으로 객체를 생성하므로 `protected` 혹은 `public` 기본 생성자가 반드시 필요하다
  - Lombok: `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
- `@Transactional`이 없으면 dirty checking이 동작하지 않아 `post.update()`를 호출해도 DB에 반영되지 않는다
- `ddl-auto: update`는 엔티티 변경 시 스키마를 자동으로 수정한다 (개발 환경 전용)
- `show-sql: true`와 `format_sql: true`를 함께 설정하면 실행되는 SQL을 콘솔에서 확인할 수 있다
- `.env` 파일을 루트 디렉토리에 생성하면 `docker-compose`와 `IntelliJ`의 EnvFile 플러그인 모두 자동으로 읽어온다
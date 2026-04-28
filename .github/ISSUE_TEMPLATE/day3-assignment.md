---
name: "[Day 3] 회원가입 / 로그인 / 로그아웃 / 내 정보 조회 구현"
about: 세션 기반 인증을 적용하고 회원 도메인을 추가하는 과제
title: "[Day 3] 회원가입 / 로그인 / 로그아웃 / 내 정보 조회 구현"
labels: assignment, day3
assignees: ''
---

## 📌 과제 목표

회원(Member) 도메인과 인증(Auth) 도메인을 추가하여 세션 기반 로그인/로그아웃 기능을 구현한다.  
패키지 구조를 도메인(Domain) 기준으로 재편하고, `Bearer Token` 방식으로 세션 키를 전달한다.

---

## 📦 패키지 구조

```
src/main/java/ssafy/study/ssafystudy/
├── auth/
│   ├── component/
│   │   └── SessionManager.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   └── dto/
│   │       ├── LoginRequest.java
│   │       └── LoginResponse.java
│   ├── service/
│   │   └── AuthService.java
│   └── util/
│       └── AuthTokenUtils.java
├── member/
│   ├── controller/
│   │   ├── MemberController.java
│   │   └── dto/
│   │       ├── MemberRequest.java
│   │       └── MemberResponse.java
│   ├── entity/
│   │   └── MemberEntity.java
│   ├── repository/
│   │   └── MemberRepository.java
│   └── service/
│       └── MemberService.java
└── post/
    └── (Day 2와 동일)
```

---

## 📋 엔티티 명세

### `MemberEntity`

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | `Long` | 자동 증가 식별자 |
| `username` | `String` | 로그인 아이디 |
| `password` | `String` | 비밀번호 |
| `nickname` | `String` | 닉네임 |

| 어노테이션 | 설명 |
|-----------|------|
| `@Getter` | 모든 필드의 getter 자동 생성 |

| 정적 필드 | 타입 | 설명 |
|----------|------|------|
| `AUTO_INCREMENT` | `static long` | 초기값 `1L`, 객체 생성 시 자동 증가 |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `MemberEntity(id, username, password, nickname)` | `Long, String, String, String` | - | `private` 생성자 |
| `create(username, password, nickname)` | `String, String, String` | `MemberEntity` | 정적 팩토리 메서드, ID 자동 할당 |
| `checkPassword(password)` | `String` | `boolean` | 비밀번호 일치 여부 확인 |

---

## 📋 DTO 명세

### `MemberRequest`

> 회원가입 요청 DTO (Java record)

| 필드 | 타입 | 설명 |
|------|------|------|
| `username` | `String` | 로그인 아이디 |
| `password` | `String` | 비밀번호 |
| `nickname` | `String` | 닉네임 |

| 메서드 | 반환 타입 | 설명 |
|--------|----------|------|
| `toEntity()` | `MemberEntity` | `MemberEntity.create()` 호출하여 변환 |

### `MemberResponse`

> 회원 응답 DTO (Java record)

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | `Long` | 회원 ID |
| `username` | `String` | 로그인 아이디 |
| `nickname` | `String` | 닉네임 |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `from(member)` | `MemberEntity` | `MemberResponse` | 정적 팩토리 메서드 |

### `LoginRequest`

> 로그인 요청 DTO (Java record)

| 필드 | 타입 | 설명 |
|------|------|------|
| `username` | `String` | 로그인 아이디 |
| `password` | `String` | 비밀번호 |

### `LoginResponse`

> 로그인 응답 DTO (Java record)

| 필드 | 타입 | 설명 |
|------|------|------|
| `accessToken` | `String` | 세션 키 |
| `tokenType` | `String` | 토큰 타입 (`"Bearer "`) |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `from(accessToken)` | `String` | `LoginResponse` | `tokenType`은 `"Bearer "` 고정 |

---

## 📋 컴포넌트 명세

### `SessionManager`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@Component` |
| 저장소 | `ConcurrentHashMap<String, Long>` (세션키 → 회원ID) |
| 역할 | 세션 생성 / 조회 / 삭제 |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `createSession(memberId)` | `Long` | `String` | UUID로 세션키 생성 후 저장 |
| `removeSession(sessionKey)` | `String` | `void` | 세션키 삭제 |
| `getMemberId(sessionKey)` | `String` | `Long` | 세션키로 회원ID 조회, 없으면 `ResponseStatusException(401)` |

### `AuthTokenUtils`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@NoArgsConstructor(access = AccessLevel.PRIVATE)` |
| 역할 | Bearer Token 파싱 유틸리티 |

| 상수 | 값 |
|------|-----|
| `PREFIX_BEARER` | `"Bearer "` |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `isValidBearerToken(bearerToken)` | `String` | `boolean` | `null`이거나 `"Bearer "`로 시작하지 않으면 `true` (유효하지 않음) |
| `parseBearerToken(bearerToken)` | `String` | `String` | `"Bearer "` 접두사를 제거하고 세션키 반환 |

### `MemberRepository`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@Repository` |
| 저장소 | `ConcurrentHashMap<Long, MemberEntity>` |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `save(member)` | `MemberEntity` | `MemberEntity` | 저장 후 반환 |
| `findByUsername(username)` | `String` | `Optional<MemberEntity>` | username으로 조회 |
| `findById(memberId)` | `Long` | `Optional<MemberEntity>` | ID로 조회 |

### `MemberService`

| 어노테이션 | `@Service`, `@RequiredArgsConstructor` |
|-----------|--------------------------------------|

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `join(request)` | `MemberRequest` | `MemberResponse` | 회원가입 처리 |
| `getMemberInfo(memberId)` | `Long` | `MemberResponse` | 회원 정보 조회, 없으면 `ResponseStatusException(404)` |

### `AuthService`

| 어노테이션 | `@Service`, `@RequiredArgsConstructor` |
|-----------|--------------------------------------|

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `login(request)` | `LoginRequest` | `LoginResponse` | username 조회 → 비밀번호 검증 → 세션 생성 |
| `logout(sessionKey)` | `String` | `void` | 세션 삭제 |

---

## 🌐 API 명세

### POST /api/members — 회원가입

| 항목 | 내용 |
|------|------|
| Method | `POST` |
| URL | `/api/members` |
| Status Code | `201 Created` |

**Request Body**
```json
{
  "username": "user1",
  "password": "password123",
  "nickname": "홍길동"
}
```

**Response Body**
```json
{
  "id": 1,
  "username": "user1",
  "nickname": "홍길동"
}
```

---

### POST /api/auth/login — 로그인

| 항목 | 내용 |
|------|------|
| Method | `POST` |
| URL | `/api/auth/login` |
| Status Code 성공 | `200 OK` |
| Status Code 실패 | `401 Unauthorized` |

**Request Body**
```json
{
  "username": "user1",
  "password": "password123"
}
```

**Response Body**
```json
{
  "accessToken": "uuid-세션키",
  "tokenType": "Bearer "
}
```

**오류 조건**
- `username`에 해당하는 회원이 없는 경우 → `401`
- 비밀번호 불일치 → `401`

---

### POST /api/auth/logout — 로그아웃

| 항목 | 내용 |
|------|------|
| Method | `POST` |
| URL | `/api/auth/logout` |
| Status Code 성공 | `204 No Content` |
| Status Code 실패 | `401 Unauthorized` |

**Request Header**
```
Authorization: Bearer {세션키}
```

---

### GET /api/members/me — 내 정보 조회

| 항목 | 내용 |
|------|------|
| Method | `GET` |
| URL | `/api/members/me` |
| Status Code 성공 | `200 OK` |
| Status Code 실패 | `401 Unauthorized` |

**Request Param**
```
?bearerToken=Bearer {세션키}
```

**Response Body**
```json
{
  "id": 1,
  "username": "user1",
  "nickname": "홍길동"
}
```

---

## 🛠️ 컨트롤러 메서드 명세

### `MemberController`

| 메서드 | HTTP | URL | 파라미터 | 반환 타입 | Status Code |
|--------|------|-----|---------|----------|-------------|
| `join(request)` | POST | `/api/members` | `@RequestBody MemberRequest` | `MemberResponse` | 201 |
| `getMyInfo(bearerToken)` | GET | `/api/members/me` | `@RequestParam String bearerToken` | `MemberResponse` | 200 |

### `AuthController`

| 메서드 | HTTP | URL | 파라미터 | 반환 타입 | Status Code |
|--------|------|-----|---------|----------|-------------|
| `login(request)` | POST | `/api/auth/login` | `@RequestBody LoginRequest` | `LoginResponse` | 200 |
| `logout(bearerToken)` | POST | `/api/auth/logout` | `@RequestHeader("Authorization") String` | `void` | 204 |

---

## ✅ 구현 요구사항 체크리스트

- [ ] 패키지 구조가 도메인(`auth`, `member`, `post`) 기준으로 분리되어 있다
- [ ] `MemberEntity`는 `private` 생성자와 정적 팩토리 메서드 `create()`를 사용한다
- [ ] `MemberRepository`는 `ConcurrentHashMap`을 인메모리 저장소로 사용한다
- [ ] `SessionManager`는 UUID를 세션키로 사용하고 `ConcurrentHashMap`에 저장한다
- [ ] `AuthTokenUtils`는 인스턴스화 불가능한 유틸리티 클래스로 구현한다
- [ ] 로그인 시 username 없음과 비밀번호 불일치 모두 `401` 응답을 반환한다
- [ ] 로그아웃 시 `Authorization` 헤더에서 Bearer Token을 파싱하여 세션을 삭제한다
- [ ] 내 정보 조회 시 세션이 유효하지 않으면 `401` 응답을 반환한다
- [ ] 4개의 API가 모두 정상 동작한다

---

## 💡 힌트

- `ConcurrentHashMap`은 멀티스레드 환경에서 안전한 HashMap이다
- `UUID.randomUUID().toString()`으로 고유한 세션키를 생성한다
- `@NoArgsConstructor(access = AccessLevel.PRIVATE)`로 유틸리티 클래스의 인스턴스 생성을 막는다
- `ResponseStatusException`을 사용하면 HTTP 상태 코드와 메시지를 함께 반환할 수 있다
- `@RequestMapping` 어노테이션을 클래스에 선언하면 하위 메서드의 URL이 합산된다

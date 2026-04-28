---
name: "[Day 4] 공통 응답 DTO / 예외처리 / 인증 기반 게시글 CRUD 리팩토링"
about: 공통 예외 처리 구조를 도입하고 게시글 CRUD에 인증 및 권한 검증을 추가하는 과제
title: "[Day 4] 공통 응답 DTO / 예외처리 / 인증 기반 게시글 CRUD 리팩토링"
labels: assignment, day4
assignees: ''
---

## 📌 과제 목표

1. 공통 응답 DTO(`ApiResponse<T>`)를 구현한다
2. `ErrorCode` enum, `CustomException`, `GlobalExceptionHandler`를 구현하여 예외를 일관되게 처리한다
3. `ResponseStatusException`을 `CustomException`으로 교체한다
4. `PostEntity`의 `author` 필드를 `String`에서 `MemberEntity`로 변경한다
5. 게시글 생성 / 수정 / 삭제 시 로그인 인증 및 작성자 권한 검증을 추가한다

---

## 📦 패키지 구조

```
src/main/java/ssafy/study/ssafystudy/
├── domain/
│   ├── auth/
│   │   ├── component/
│   │   │   └── SessionManager.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   └── dto/
│   │   │       ├── LoginRequest.java
│   │   │       └── LoginResponse.java
│   │   ├── service/
│   │   │   └── AuthService.java
│   │   └── util/
│   │       └── AuthTokenUtils.java
│   ├── member/
│   │   ├── controller/
│   │   │   ├── MemberController.java
│   │   │   └── dto/
│   │   │       ├── MemberRequest.java
│   │   │       └── MemberResponse.java
│   │   ├── entity/
│   │   │   └── MemberEntity.java
│   │   ├── repository/
│   │   │   └── MemberRepository.java
│   │   └── service/
│   │       └── MemberService.java
│   └── post/
│       ├── controller/
│       │   ├── PostController.java
│       │   └── dto/
│       │       ├── PostRequest.java
│       │       └── PostResponse.java
│       ├── entity/
│       │   └── PostEntity.java
│       ├── repository/
│       │   └── PostRepository.java
│       └── service/
│           └── PostService.java
└── global/
    ├── exception/
    │   ├── CustomException.java
    │   ├── GlobalExceptionHandler.java
    │   └── error/
    │       └── ErrorCode.java
    └── response/
        └── ApiResponse.java
```

---

## 📋 공통 응답 DTO 명세

### `ApiResponse<T>`

> 모든 API 응답에 공통으로 사용하는 제네릭 DTO (Java record)

| 필드 | 타입 | 설명 |
|------|------|------|
| `message` | `String` | 응답 메시지 |
| `data` | `T` | 응답 데이터 (없으면 `null`) |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `success(message, data)` | `String, T` | `ApiResponse<T>` | 성공 응답 (데이터 포함) |
| `success(message)` | `String` | `ApiResponse<Void>` | 성공 응답 (데이터 없음) |
| `error(errorCode)` | `ErrorCode` | `ApiResponse<Void>` | 에러 응답 (데이터 없음) |
| `error(errorCode, data)` | `ErrorCode, T` | `ApiResponse<T>` | 에러 응답 (데이터 포함) |

**응답 예시**
```json
{
  "message": "게시글 생성 성공",
  "data": {
    "id": 1,
    "title": "제목"
  }
}
```

---

## 📋 예외 처리 명세

### `ErrorCode`

> HTTP 상태 코드와 메시지를 함께 관리하는 enum

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@Getter`, `@RequiredArgsConstructor` |

| 상수 | HttpStatus | 메시지 |
|------|-----------|-------|
| `INVALID_PARAMETER` | `400 BAD_REQUEST` | `"잘못된 요청 파라미터입니다."` |
| `UNAUTHORIZED` | `401 UNAUTHORIZED` | `"로그인이 필요한 서비스입니다."` |
| `INVALID_PERMISSION` | `403 FORBIDDEN` | `"해당 권한이 없습니다."` |
| `INTERNAL_SERVER_ERROR` | `500 INTERNAL_SERVER_ERROR` | `"서버 내부 오류가 발생했습니다."` |
| `MEMBER_NOT_FOUND` | `404 NOT_FOUND` | `"존재하지 않는 회원입니다."` |
| `INVALID_USERNAME` | `401 UNAUTHORIZED` | `"아이디 또는 비밀번호가 일치하지 않습니다."` |
| `INVALID_PASSWORD` | `401 UNAUTHORIZED` | `"아이디 또는 비밀번호가 일치하지 않습니다."` |
| `INVALID_TOKEN` | `401 UNAUTHORIZED` | `"토큰 정보가 유효하지 않습니다."` |
| `POST_NOT_FOUND` | `404 NOT_FOUND` | `"존재하지 않는 게시글입니다."` |

| 필드 | 타입 | 설명 |
|------|------|------|
| `httpStatus` | `HttpStatus` | HTTP 상태 코드 |
| `message` | `String` | 에러 메시지 |

### `CustomException`

> `RuntimeException`을 상속받는 커스텀 예외 클래스

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@Getter` |
| 상위 클래스 | `RuntimeException` |

| 필드 | 타입 | 설명 |
|------|------|------|
| `errorCode` | `ErrorCode` | 예외에 해당하는 ErrorCode |

| 메서드 | 파라미터 | 설명 |
|--------|---------|------|
| `CustomException(errorCode)` | `ErrorCode` | `super(errorCode.getMessage())` 호출 |

**사용 예시**
```java
throw new CustomException(ErrorCode.POST_NOT_FOUND);
throw new CustomException(ErrorCode.INVALID_TOKEN);
```

### `GlobalExceptionHandler`

> 전역 예외 처리 핸들러

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@Slf4j`, `@RestControllerAdvice` |

| 메서드 | 처리 예외 | 반환 타입 | 동작 |
|--------|---------|----------|------|
| `handleCustomException(e)` | `CustomException` | `ResponseEntity<ApiResponse<Void>>` | `errorCode`의 `httpStatus`로 응답 상태 설정 |
| `handleException(e)` | `Exception` | `ResponseEntity<ApiResponse<Void>>` | `log.error()` 로깅 후 `500` 반환 |

---

## 📋 변경된 엔티티 / DTO 명세

### `PostEntity` (변경)

| 필드 | 타입 | 변경 사항 |
|------|------|---------|
| `author` | `MemberEntity` | `String` → `MemberEntity` |

| 메서드 | 파라미터 | 설명 |
|--------|---------|------|
| `create(title, content, author)` | `String, String, MemberEntity` | 정적 팩토리 메서드 |
| `update(title, content)` | `String, String` | 제목·내용 수정 |

### `PostRequest` (변경)

| 필드 | 변경 사항 |
|------|---------|
| `author` 필드 제거 | 작성자 정보는 세션에서 가져옴 |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `toEntity(author)` | `MemberEntity` | `PostEntity` | `author`를 파라미터로 받도록 변경 |

### `PostResponse` (변경)

| 필드 | 타입 | 변경 사항 |
|------|------|---------|
| `memberResponse` | `MemberResponse` | `author(String)` → `memberResponse(MemberResponse)` |

---

## 📋 컴포넌트 명세 (변경)

### `PostService` (변경)

| 메서드 | 파라미터 | 반환 타입 | 변경 사항 |
|--------|---------|----------|---------|
| `create(request, authorId)` | `PostRequest, Long` | `PostResponse` | `authorId` 파라미터 추가, 회원 존재 확인 후 생성 |
| `getPostById(id)` | `Long` | `PostResponse` | `CustomException(POST_NOT_FOUND)` 으로 교체 |
| `update(request, id, authorId)` | `PostRequest, Long, Long` | `PostResponse` | `authorId` 파라미터 추가, 권한 검증 로직 추가 |
| `delete(id, authorId)` | `Long, Long` | `void` | `authorId` 파라미터 추가, 권한 검증 로직 추가 |

**권한 검증 로직 (수정 / 삭제 공통)**
1. `memberRepository.findById(authorId)` → 없으면 `CustomException(MEMBER_NOT_FOUND)`
2. `postRepository.findById(id)` → 없으면 `CustomException(POST_NOT_FOUND)`
3. `post.getAuthor().getId().equals(author.getId())` → 다르면 `CustomException(INVALID_PERMISSION)`

### `PostController` (변경)

| 메서드 | HTTP | 파라미터 추가 | 변경 사항 |
|--------|------|------------|---------|
| `createPost` | POST | `@RequestHeader("Authorization") String bearerToken` | 토큰 검증 후 `authorId` 추출하여 서비스 전달 |
| `updatePost` | PUT | `@RequestHeader("Authorization") String bearerToken` | 토큰 검증 후 `authorId` 추출하여 서비스 전달 |
| `deletePost` | DELETE | `@RequestHeader("Authorization") String bearerToken` | 토큰 검증 후 `authorId` 추출하여 서비스 전달 |

**공통 인증 흐름 (생성 / 수정 / 삭제)**
```
1. AuthTokenUtils.isValidBearerToken(bearerToken) → 유효하지 않으면 CustomException(INVALID_TOKEN)
2. AuthTokenUtils.parseBearerToken(bearerToken) → sessionKey 추출
3. sessionManager.getMemberId(sessionKey) → authorId 추출
4. postService.create/update/delete(request, id, authorId) 호출
```

---

## 🌐 API 명세 (변경)

### POST /api/posts — 게시글 생성 (변경)

| 항목 | 내용 |
|------|------|
| Method | `POST` |
| URL | `/api/posts` |
| Status Code 성공 | `201 Created` |
| Status Code 실패 | `401 Unauthorized` |

**Request Header**
```
Authorization: Bearer {세션키}
```

**Request Body** (author 필드 제거됨)
```json
{
  "title": "제목",
  "content": "내용"
}
```

**Response Body** (author가 MemberResponse로 변경됨)
```json
{
  "id": 1,
  "title": "제목",
  "content": "내용",
  "memberResponse": {
    "id": 1,
    "username": "user1",
    "nickname": "홍길동"
  }
}
```

---

### PUT /api/posts/{id} — 게시글 수정 (변경)

| 항목 | 내용 |
|------|------|
| Method | `PUT` |
| URL | `/api/posts/{id}` |
| Status Code 성공 | `200 OK` |
| Status Code 실패 | `401 Unauthorized`, `403 Forbidden`, `404 Not Found` |

**Request Header**
```
Authorization: Bearer {세션키}
```

**오류 조건**
- 토큰이 유효하지 않은 경우 → `401` (`INVALID_TOKEN`)
- 게시글이 없는 경우 → `404` (`POST_NOT_FOUND`)
- 작성자가 아닌 경우 → `403` (`INVALID_PERMISSION`)

---

### DELETE /api/posts/{id} — 게시글 삭제 (변경)

| 항목 | 내용 |
|------|------|
| Method | `DELETE` |
| URL | `/api/posts/{id}` |
| Status Code 성공 | `204 No Content` |
| Status Code 실패 | `401 Unauthorized`, `403 Forbidden`, `404 Not Found` |

**Request Header**
```
Authorization: Bearer {세션키}
```

---

## ✅ 구현 요구사항 체크리스트

**공통 응답 / 예외**
- [ ] `ApiResponse<T>` record가 구현되어 있고 4개의 정적 팩토리 메서드가 있다
- [ ] `ErrorCode` enum에 9개의 에러 코드가 정의되어 있다
- [ ] `CustomException`은 `RuntimeException`을 상속하고 `ErrorCode`를 필드로 가진다
- [ ] `GlobalExceptionHandler`는 `@RestControllerAdvice`로 선언되어 있다
- [ ] `CustomException` 발생 시 `errorCode`의 HTTP 상태 코드로 응답한다
- [ ] `Exception` 발생 시 로그를 남기고 `500` 응답을 반환한다

**기존 코드 리팩토링**
- [ ] 기존 `ResponseStatusException` 사용 코드가 모두 `CustomException`으로 교체되었다
- [ ] `PostEntity.author` 타입이 `String`에서 `MemberEntity`로 변경되었다
- [ ] `PostRequest`에서 `author` 필드가 제거되고 `toEntity(MemberEntity)`로 변경되었다
- [ ] `PostResponse.memberResponse`는 `MemberResponse` 타입이다

**인증 기반 CRUD**
- [ ] 게시글 생성 시 `Authorization` 헤더에서 세션키를 추출하여 작성자를 설정한다
- [ ] 게시글 수정 시 요청자가 작성자가 아니면 `403` 응답을 반환한다
- [ ] 게시글 삭제 시 요청자가 작성자가 아니면 `403` 응답을 반환한다
- [ ] 조회 API(`GET`)는 인증 없이 접근 가능하다

---

## 💡 힌트

- `@RestControllerAdvice`는 `@ControllerAdvice` + `@ResponseBody`의 조합으로, 모든 Controller에서 발생한 예외를 처리한다
- `ResponseEntity.status(errorCode.getHttpStatus()).body(...)` 패턴으로 HTTP 상태 코드와 바디를 함께 반환한다
- 제네릭 record의 정적 메서드에는 `<T>`를 별도로 선언해야 한다: `public static <T> ApiResponse<T> success(...)`
- `@Slf4j` 어노테이션을 사용하면 `log.error()`, `log.info()` 등 로거를 자동으로 생성한다
- `PostRepository`에 `delete(PostEntity post)` 메서드를 추가하면 객체를 직접 삭제할 수 있다

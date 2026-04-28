---
name: "[Day 2] Post CRUD API 구현 (인메모리)"
about: Lombok을 활용하여 게시글 CRUD REST API를 인메모리 방식으로 구현하는 과제
title: "[Day 2] Post CRUD API 구현 (인메모리)"
labels: assignment, day2
assignees: ''
---

## 📌 과제 목표

Lombok을 활용하여 게시글(Post)의 생성 / 조회 / 수정 / 삭제(CRUD) REST API를 인메모리 방식으로 구현한다.  
Controller → Service → Repository → Entity 계층 흐름을 이해한다.

---

## 📦 패키지 구조

```
src/main/java/ssafy/study/ssafystudy/
└── post/
    ├── controller/
    │   ├── PostController.java
    │   └── dto/
    │       ├── PostRequest.java
    │       └── PostResponse.java
    ├── entity/
    │   └── PostEntity.java
    ├── repository/
    │   └── PostRepository.java
    └── service/
        └── PostService.java
```

---

## 🔧 의존성

`build.gradle`에 아래 의존성을 추가한다.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```

---

## 📋 엔티티 명세

### `PostEntity`

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | `Long` | 자동 증가 식별자 |
| `title` | `String` | 게시글 제목 |
| `content` | `String` | 게시글 내용 |
| `author` | `String` | 작성자 이름 |

| 어노테이션 | 설명 |
|-----------|------|
| `@Getter` | 모든 필드의 getter 자동 생성 |

| 정적 필드 | 타입 | 설명 |
|----------|------|------|
| `AUTO_INCREMENT_ID` | `static long` | 초기값 `1L`, 객체 생성 시 자동 증가 |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `PostEntity(title, content, author)` | `String, String, String` | - | 생성자, id 자동 할당 |
| `update(title, content)` | `String, String` | `void` | 제목·내용 수정 |

---

## 📋 DTO 명세

### `PostRequest`

> 게시글 생성/수정 요청 DTO

| 필드 | 타입 | 설명 |
|------|------|------|
| `title` | `String` | 게시글 제목 |
| `content` | `String` | 게시글 내용 |
| `author` | `String` | 작성자 이름 |

- 생성자, getter를 직접 구현한다 (Lombok 미사용)

### `PostResponse`

> 게시글 응답 DTO

| 필드 | 타입 | 설명 |
|------|------|------|
| `id` | `Long` | 게시글 ID |
| `title` | `String` | 게시글 제목 |
| `content` | `String` | 게시글 내용 |
| `author` | `String` | 작성자 이름 |

- Java `record`로 선언한다
- 정적 팩토리 메서드 `from(PostEntity)`를 제공한다

---

## 📋 컴포넌트 명세

### `PostRepository`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@Repository` |
| 저장소 | `List<PostEntity>` (ArrayList) |
| 역할 | 인메모리 CRUD 처리 |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `save(postEntity)` | `PostEntity` | `PostEntity` | 저장 후 반환 |
| `findById(postId)` | `Long` | `Optional<PostEntity>` | ID로 단건 조회 |
| `findAll()` | - | `List<PostEntity>` | 전체 조회 |
| `deleteById(postId)` | `Long` | `void` | ID로 삭제 |

### `PostService`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@Service`, `@RequiredArgsConstructor` |
| 역할 | 비즈니스 로직 처리 |

| 메서드 | 파라미터 | 반환 타입 | 설명 |
|--------|---------|----------|------|
| `save(request)` | `PostRequest` | `PostResponse` | 게시글 생성 |
| `getAllPosts()` | - | `List<PostResponse>` | 전체 게시글 조회 |
| `findById(postId)` | `Long` | `PostResponse` | 단건 게시글 조회, 없으면 `RuntimeException` |
| `update(request, postId)` | `PostRequest, Long` | `PostResponse` | 게시글 수정, 없으면 `RuntimeException` |
| `delete(postId)` | `Long` | `void` | 게시글 삭제 |

### `PostController`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@RestController`, `@RequiredArgsConstructor` |
| 역할 | HTTP 요청 처리 및 응답 반환 |

---

## 🌐 API 명세

### POST /api/posts — 게시글 생성

| 항목 | 내용 |
|------|------|
| Method | `POST` |
| URL | `/api/posts` |
| Status Code | `201 Created` |

**Request Body**
```json
{
  "title": "제목",
  "content": "내용",
  "author": "작성자"
}
```

**Response Body**
```json
{
  "id": 1,
  "title": "제목",
  "content": "내용",
  "author": "작성자"
}
```

---

### GET /api/posts — 전체 게시글 조회

| 항목 | 내용 |
|------|------|
| Method | `GET` |
| URL | `/api/posts` |
| Status Code | `200 OK` |

**Response Body**
```json
[
  {
    "id": 1,
    "title": "제목",
    "content": "내용",
    "author": "작성자"
  }
]
```

---

### GET /api/posts/{postId} — 단건 게시글 조회

| 항목 | 내용 |
|------|------|
| Method | `GET` |
| URL | `/api/posts/{postId}` |
| Status Code 성공 | `200 OK` |
| Status Code 실패 | `404 Not Found` |

**Path Variable**

| 이름 | 타입 | 설명 |
|------|------|------|
| `postId` | `Long` | 게시글 ID |

---

### PUT /api/posts/{postId} — 게시글 수정

| 항목 | 내용 |
|------|------|
| Method | `PUT` |
| URL | `/api/posts/{postId}` |
| Status Code 성공 | `200 OK` |
| Status Code 실패 | `404 Not Found` |

**Request Body**
```json
{
  "title": "수정된 제목",
  "content": "수정된 내용",
  "author": "작성자"
}
```

---

### DELETE /api/posts/{postId} — 게시글 삭제

| 항목 | 내용 |
|------|------|
| Method | `DELETE` |
| URL | `/api/posts/{postId}` |
| Status Code 성공 | `204 No Content` |
| Status Code 실패 | `404 Not Found` |

---

## 🛠️ 컨트롤러 메서드 명세

| 메서드 | HTTP | URL | 파라미터 | 반환 타입 | Status Code |
|--------|------|-----|---------|----------|-------------|
| `createPost(request)` | POST | `/api/posts` | `@RequestBody PostRequest` | `PostResponse` | 201 |
| `getAllPosts()` | GET | `/api/posts` | - | `List<PostResponse>` | 200 |
| `getPostById(postId)` | GET | `/api/posts/{postId}` | `@PathVariable Long` | `ResponseEntity<PostResponse>` | 200 / 404 |
| `updatePost(postId, request)` | PUT | `/api/posts/{postId}` | `@PathVariable Long, @RequestBody PostRequest` | `ResponseEntity<PostResponse>` | 200 / 404 |
| `deletePost(postId)` | DELETE | `/api/posts/{postId}` | `@PathVariable Long` | `ResponseEntity<Void>` | 204 / 404 |

---

## ✅ 구현 요구사항 체크리스트

- [ ] Lombok(`@Getter`, `@RequiredArgsConstructor`) 의존성이 추가되어 있다
- [ ] `PostEntity`는 `static` 필드로 ID를 자동 증가시킨다
- [ ] `PostRequest`는 생성자와 getter를 직접 구현한다
- [ ] `PostResponse`는 Java `record`로 구현하고 `from(PostEntity)` 정적 팩토리 메서드를 제공한다
- [ ] `PostRepository`는 `ArrayList`를 인메모리 저장소로 사용한다
- [ ] `PostService`는 `@RequiredArgsConstructor`로 생성자 주입을 받는다
- [ ] 단건 조회 / 수정 시 게시글이 없으면 `RuntimeException`이 발생한다
- [ ] 컨트롤러에서 예외 발생 시 `404 Not Found` 응답을 반환한다
- [ ] 5개의 API가 모두 정상 동작한다

---

## 💡 힌트

- Java `record`는 불변(immutable) 데이터 클래스를 간결하게 선언하는 방법이다
- `Optional.orElseThrow()`를 활용하면 예외 처리를 간결하게 할 수 있다
- `List.stream().map(...).toList()`로 Entity 리스트를 DTO 리스트로 변환한다
- `@ResponseStatus(HttpStatus.CREATED)`를 메서드에 선언하면 응답 코드를 지정할 수 있다
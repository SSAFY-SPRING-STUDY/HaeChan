---
name: "[Day 1] Spring Boot 기초 - Hello API 구현"
about: Spring Boot 프로젝트 생성 및 첫 번째 REST API 구현 과제
title: "[Day 1] Spring Boot 기초 - Hello API 구현"
labels: assignment, day1
assignees: ''
---

## 📌 과제 목표

Spring Boot 프로젝트를 생성하고 Controller-Service 계층 구조를 이해하며, 첫 번째 REST API를 구현한다.

---

## 📦 패키지 구조

```
src/main/java/ssafy/study/ssafystudy/
├── HelloController.java
└── HelloService.java
```

---

## 🔧 의존성

`build.gradle`에 아래 의존성을 추가한다.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    // 필요한 경우 추가
}
```

---

## 📋 컴포넌트 명세

### `HelloController`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@RestController` |
| 역할 | HTTP 요청을 받아 `HelloService`에 위임하고 응답을 반환 |
| 의존성 주입 방식 | 생성자 주입 (`@Autowired`) |

### `HelloService`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@Service` |
| 역할 | 비즈니스 로직 처리 및 응답 문자열 반환 |

---

## 🌐 API 명세

### GET /hello

| 항목 | 내용 |
|------|------|
| Method | `GET` |
| URL | `/hello` |
| Request Body | 없음 |
| Response | `String` |
| 응답 예시 | `"Hello Spring Boot!"` |

---

## 🛠️ 메서드 명세

### `HelloController`

#### `hello()`

| 항목 | 내용 |
|------|------|
| 어노테이션 | `@GetMapping("/hello")` |
| 반환 타입 | `String` |
| 파라미터 | 없음 |
| 동작 | `helloService.hi()` 호출 후 결과 반환 |

### `HelloService`

#### `hi()`

| 항목 | 내용 |
|------|------|
| 반환 타입 | `String` |
| 파라미터 | 없음 |
| 동작 | `"Hello Spring Boot!"` 문자열 반환 |

---

## ✅ 구현 요구사항 체크리스트

- [ ] Spring Boot 프로젝트가 정상적으로 생성되어 실행된다
- [ ] `HelloService`에 `@Service` 어노테이션이 적용되어 있다
- [ ] `HelloController`에 `@RestController` 어노테이션이 적용되어 있다
- [ ] `HelloController`는 생성자 주입 방식으로 `HelloService`를 주입받는다
- [ ] `GET /hello` 요청 시 `"Hello Spring Boot!"` 문자열이 반환된다

---

## 💡 힌트

- `@RestController`는 `@Controller` + `@ResponseBody`의 조합이다
- Spring의 의존성 주입(DI) 방식 3가지(필드, 수정자, 생성자)의 차이를 이해하고 생성자 주입을 사용할 것
- `@Autowired`는 생성자가 하나일 경우 생략 가능하다 (Spring 4.3+)
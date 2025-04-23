<h1 align="center">🖼️ K-Webtoon Backend</h1>
<p align="center"><strong>AI 기반 웹툰 추천 시스템, 큐레이툰의 백엔드</strong></p>
<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue?logo=java">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.3-green?logo=springboot">
  <img src="https://img.shields.io/badge/PostgreSQL-Vector%20Search-blue?logo=postgresql">
  <img src="https://img.shields.io/badge/AI-추천%20모델-lightgrey?logo=python">
</p>

---

## ✨ 프로젝트 소개

**K-Webtoon (큐레이툰)** 은 사용자의 취향을 정밀하게 파악하여  
**AI 기반의 맞춤형 웹툰을 추천**하는 스마트한 플랫폼입니다.  
본 저장소는 서비스의 핵심인 백엔드 시스템을 다루며,  
**강력한 인증 시스템, 추천 알고리즘, 사용자 데이터 분석** 기능을 포함합니다.

---

## 🚀 주요 기능

### 🔐 사용자 인증 & 계정 관리
- JWT 기반 인증
- 이메일/비밀번호 로그인
- OAuth2 소셜 로그인 (Google, Kakao)
- 계정 생성 / 수정 / 비활성화

---

### 🧠 추천 시스템 (AI 기반)
- **콘텐츠 기반 추천**: 태그, 장르, 그림체 등 분석
- **협업 필터링**: 사용자 간 유사 행동 기반 추천
- **인기도 기반 추천**: 평점, 좋아요, 조회수 반영
- **코사인 유사도** 기반 웹툰 유사도 분석

---

### 🤖 Flask AI 서버 연동
- RESTful API 통신
- 엔드포인트:
  - `POST /api/connector/sendM` → 유사 웹툰 추천
  - `POST /api/connector/sendC` → 콘텐츠 기반 추천
  - `POST /api/connector/sendL_if` → 사용자 기반 통합 추천

---

### 📚 웹툰 데이터 관리
- CRUD 지원 (등록, 수정, 삭제, 조회)
- 웹툰 메타데이터 관리
- PGVector를 활용한 벡터 임베딩 저장 및 검색

---

### 📊 사용자 활동 추적
- 평점 및 리뷰 등록
- 즐겨찾기, 좋아요 기능
- 사용자 행동 로그 기록

---

### ⚙️ 관리자 기능
- 전체 사용자 및 웹툰 관리
- 추천 통계 확인
- 시스템 상태 모니터링

---

## 🛠️ 기술 스택

| 범주 | 기술 |
|------|------|
| **언어** | Java 17 |
| **프레임워크** | Spring Boot 3.4.3, Spring Security, JPA |
| **인증** | JWT, OAuth2 (Google, Kakao) |
| **DB** | PostgreSQL, MySQL, PGVector |
| **AI 연동** | Python Flask 서버 |
| **도구** | Gradle, Swagger/OpenAPI, JUnit 5 |

---

## 🧩 시스템 아키텍처

```text
[Client]
   |
   v
[Spring Boot Backend] <--> [Flask AI Server]
       |
       v
[PostgreSQL / MySQL]
```

---

## 📘 API 문서

- **Swagger UI 제공**
- 접속 URL:
  ```
  http://<서버주소>/swagger-ui.html
  ```

---

## ⚙️ 환경 변수 (.env or config)
```bash
# JWT
JWT_SECRET=your_jwt_secret_key

# Google OAuth
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# Kakao OAuth
KAKAO_CLIENT_ID=your_kakao_client_id
KAKAO_CLIENT_SECRET=your_kakao_client_secret
```

---

## 🔧 빌드 및 실행

```bash
# 프로젝트 빌드
./gradlew build

# 실행
java -jar build/libs/k-webtoons-0.0.1-SNAPSHOT.jar
```

---

## 📄 라이선스

```
© 2023–2025 큐레이툰(K-Webtoon) 개발팀. All rights reserved.
```

---

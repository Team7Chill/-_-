# Chillme

## **👨‍💻 Team**

- 팀명: Chill조
- 소개: 이 조 . . Chill하다 . . .
- 팀원 및 역할 분담

| **이름** | **역할** | **주요 담당 업무**                                                                               |
|--------|--------|--------------------------------------------------------------------------------------------|
| 최경진    | 팀장     | - 로그인/로그아웃 Refresh Token 재발급 API JJWT , Spring Security 기반 인증인가                            |
| 안요한    | 팀원     | - 댓글 API (CRUD, 페이지네이션) / BaseEntity (LocalDateTime, Soft Delete) / 공통 응답 객체 (ApiResponse) |
| 안지현    | 팀원     | - 회원가입/회원조회/회원수정/회원삭제(소프트 딜리트 방식)                                                          |
| 곽현민    | 팀원     | - 태스크 API 설계 / 태스크생성 / 태스크조회 / 태스크수정 / 태스크삭제                                               |
| 우지운    | 팀원     | - 로그 조회 / 로그 자동 저장 API                                                                     |

## **🛠 사용 기술**

| **제목**      | **내용**                                                       |
|-------------|--------------------------------------------------------------|
| **언어**      | Java 17                                                      |
| **프레임워크**   | Spring Boot 3.5.0                                            |
| **개발 환경**   | IntelliJ IDEA                                                |
| **빌드 도구**   | Gradle 8.5                                                   |
| **데이터베이스**  | MySQL 8.0 이상 (JDBC Driver)                                   |
| **ORM**     | Spring Data JPA (Hibernate 6.6.13.Final), QueryDSL 5.1.0     |
| **보안**      | Spring Security, JWT (jjwt 0.11.5), 비밀번호 암호화 (BCrypt 0.10.2) |
| **라이브러리**   | Lombok                                                       |
| **API 테스트** | Postman                                                      |
| **DB 설계**   | ERD Cloud                                                    |

## **💻 개발 도구**

- IntelliJ IDEA
- Git
- Postman

## **📃 프로젝트 설계**

- API 명세서
  - [https://www.notion.so/teamsparta/Chill-2112dc3ef514807b92c3fb7f8abdadf8?p=2172dc3ef5148086b8e1dae7f9e5e145&pm=s](https://www.notion.so/06-19-TODO-2172dc3ef5148086b8e1dae7f9e5e145?pvs=21)
- 와이어 프레임

  https://taskflow-ten-tan.vercel.app/

  ID: admin

  PASSWORD: password

- DB 설계

  [https://www.notion.so/teamsparta/Chill-2112dc3ef514807b92c3fb7f8abdadf8?p=2172dc3ef5148086b8e1dae7f9e5e145&pm=s](https://www.notion.so/06-19-TODO-2172dc3ef5148086b8e1dae7f9e5e145?pvs=21)


## **📁 폴더 구조**

```bash
com.example.project
│
├── global                           ← 공통 설정 및 보안 (담당: 최경진)
│   ├── config
│   ├── exception
│   ├── security
│   └── util
│
├── domain
│   ├── auth                         ← 로그인/로그아웃 (담당: 최경진)
│   │   ├── controller
│   │   │   └── dto                  // 요청/응답 DTO
│   │   ├── service
│   │   │   └── dto                  // 비즈니스 내부 전달 DTO (Optional)
│   │   └── domain
│   │       ├── model               // RefreshToken, AuthUser 등
│   │       ├── repository
│   │       └── dto                 // 인증 관련 공용 DTO
│   │
│   ├── user                         ← 회원 관리 (담당: 안지현)
│   │   ├── controller
│   │   │   └── dto
│   │   ├── service
│   │   │   └── dto
│   │   └── domain
│   │       ├── model               // User, Profile, Authority 등
│   │       ├── repository
│   │       └── dto
│   │
│   ├── task                         ← 태스크 (담당: 곽현민)
│   │   ├── controller
│   │   │   └── dto
│   │   ├── service
│   │   │   └── dto
│   │   └── domain
│   │       ├── model
│   │       ├── repository
│   │       └── dto
│   │
│   ├── comment                      ← 댓글 (담당: 안요한)
│   │   ├── controller
│   │   │   └── dto
│   │   ├── service
│   │   │   └── dto
│   │   └── domain
│   │       ├── model
│   │       ├── repository
│   │       └── dto
│   │
│   ├── dashboard                    ← 대시보드 (담당: 김민진)
│   │   ├── controller
│   │   │   └── dto
│   │   ├── service
│   │   │   └── dto
│   │   └── domain
│   │       ├── model
│   │       └── dto
│   │
│   └── log                          ← 활동 로그 (담당: 우지운)
│       ├── controller
│       │   └── dto
│       ├── service
│       │   └── dto
│       └── domain
│           ├── model
│           ├── repository
│           └── dto
│
└── Application.java
```

## 📊 API 통계

### **API Count Summary**

```
- CommentsController.java
  ├── GET APIs:     2
  ├── POST APIs:    1
  ├── PATCH APIs:   1
  └── DELETE APIs:  1

- AuthController.java
  ├── POST APIs:    3

- UserController.java
  ├── GET APIs:     1
  ├── POST APIs:    2

- DashboardController.java
  ├── GET APIs:     2

- TaskController.java
  ├── GET APIs:     3
  ├── POST APIs:    1
  ├── PATCH APIs:   2
  └── DELETE APIs:  1

- LogController.java
  ├── GET APIs:     1

```

### 📈 **API Statistics**

| 메서드    | 개수     |
|--------|--------|
| GET    | 9      |
| POST   | 7      |
| PATCH  | 3      |
| DELETE | 2      |
| **총합** | **21** |

---

## **🚀 사용법**

- 환경변수 설정하기

  **0. 현재 설정**
    
  ---

  현재 설정에 따라 환경 변수 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` , `SECRET_KEY` 를 설정해야 하는 상황

    ```
    spring:
      datasource:
        url: ${DB_URL}
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
        
    jwt:
      secret:
        key: ${SECRET_KEY}
    ```

  **1. 환경변수 설정하기**

  ![envGuide1.png.png](https://github.com/Hyunmin-Kwak-hub/Newsfeed/raw/main/readme/envGuide1.png)

  **2. 옵션 추가하기**

  `빌드 및 실행` 항목의 옵션 수정(M) 선택

  아래 내용 체크 표시

    ```
    환경변수
    ```

  1. 환경 변수(E) 의 파일모양 📄 선택

  ![envGuide2.png](https://github.com/Hyunmin-Kwak-hub/Newsfeed/raw/main/readme/envGuide2.png)

  **2. 환경변수 설정하기**

  필요한 환경변수 추가 후 적용

- env 설정 방법

  ## `.env` 파일 작성 가이드

    ```
    env
    # JWT 설정
    JWT_SECRET_KEY=
    
    # 데이터베이스 설정
    DB_URL=jdbc:
    DB_USER=
    DB_PASSWORD=
    ```

  ### 각 항목 설명

  - `JWT_SECRET_KEY` : JWT 토큰 서명에 사용하는 비밀 키를 입력하세요. (예: 랜덤 문자열)
  - `DB_URL` : 데이터베이스 접속 URL을 입력하세요. (예: `jdbc:mysql://localhost:3306/your_db_name`)
  - `DB_USER` : 데이터베이스 사용자 이름을 입력하세요.
  - `DB_PASSWORD` : 데이터베이스 비밀번호를 입력하세요.

  ### 주의 사항

  - `.env` 파일은 민감 정보가 포함되어 있으므로 `.gitignore`에 반드시 추가해 GitHub 등 원격 저장소에 업로드하지 마세요.
  - 대신 `.env.example` 파일을 만들어 빈 값 또는 예시값으로 커밋하여 팀원과 공유하세요.

  ### 개발 환경에서 사용

  - `.env` 파일에 적은 환경변수는 애플리케이션 실행 시 자동으로 로드되도록 설정해야 합니다.
  - 스프링 부트 같은 프레임워크는 기본적으로 `application.properties`나 `application.yml`을 주로 사용하므로 `.env` 사용 시 별도 라이브러리 설정이 필요할 수 있습니다.

## 🌟기능 구현

### 최경진

### 로그인 API

- **URL**: `POST <http://localhost:8080/api/login`>
- **기능 설명**:
- 클라이언트로부터 이메일/비밀번호를 받아 인증 수행
- 인증 성공 시 Access Token은 응답 헤더에, Refresh Token은 쿠키로 전달
- 로그인 성공 메시지 반환

### 로그아웃 API

- **URL**: `POST <http://localhost:8080/api/logout`>
- **기능 설명**:
- 클라이언트로부터 전달받은 Access Token과 Refresh Token을 무효화 처리
- Refresh Token 쿠키 삭제

### 액세스 토큰 재발급 API

- **URL**: `POST /api/refresh-token`
- **기능설명**:
  -클라이언트가 보낸 유효한 **Refresh Token**을 이용해 **새로운 Access Token**을 발급합니다.
- 동시에 Refresh Token도 새로 발급해 **쿠키에 재설정**합니다.

### 곽현민

### 태스크 API

- **태스크 작성**
  - `URL: POST <http://localhost:8080/api/tasks>`
  - 기능
    - Access Token 사용하여 인증 수행
    - `@AuthenticationPrincipal` 어노테이션을 통해 사용자 정보를 받아와서 생성자 지정
    - RequestBody에서 받은 태스크 속성들을 데이터에 저장
    - 태스크 생성 메시지 반환
- **태스크 전체 조회**
  - URL: `GET <http://localhost:8080/api/tasks>`
  - 기능
    - Access Token 사용하여 인증 수행
    - 태스크 테이블의 데이터를 페이징 조건에 맞게 조회
    - 태스크 조회 성공 메시지 및 태스크 정보 반환
- **태스크 검색 조회**
  - URL: `GET <http://localhost:8080/api/tasks/search>`
  - 기능
    - Access Token 사용하여 인증 수행
    - 태스크 테이블의 데이터를 검색 조건(제목, 내용, 상태)과 페이징 조건에 맞게 조회
    - 태스크 조회 성공 메시지 및 검색된 태스크 정보 반환
- **태스크 단건 조회**
  - URL: `GET <http://localhost:8080/api/tasks/{taskId}>`
  - 기능
    - Access Token 사용하여 인증 수행
    - 태스크 식별자가 {taskId}인 태스크의 정보를 조회
    - 태스크 조회 성공 메시지 및 태스크 정보 반환
- **태스크 수정**
  - URL: `PATCH <http://localhost:8080/api/tasks/{taskId}>`
  - 기능
    - Access Token 사용하여 인증 수행
    - 태스크 식별자가 {taskId}인 태스크의 정보를 조회
    - 조회한 태스크의 정보를 RequestBody에서 받은 속성 정보로 수정 및 저장
    - 태스크 수정 성공 메시지 및 수정된 태스크 정보 반환
- **태스크 진행 상태 수정**
  - URL: `PATCH <http://localhost:8080/api/tasks/{taskId}/status>`
  - 기능
    - Access Token 사용하여 인증 수행
    - 태스크 식별자가 {taskId}인 태스크의 정보를 조회
    - 조회한 태스크의 속성 중 상태 정보를 RequestBody에서 받은 상태 속성 정보로 수정 및 저장
    - 태스크 수정 성공 메시지 및 수정된 태스크 정보 반환
- **태스크 삭제**
  - URL: `DELETE <http://localhost:8080/api/tasks/{taskId}>`
  - 기능
    - Access Token 사용하여 인증 수행
    - 태스크 식별자가 {taskId}인 태스크의 정보를 조회
    - 조회한 태스크를 SoftDelete 삭제
    - 태스크 삭제 성공 메시지 반환

### 우지운

### 로그 조회 API

- **URL** :`GET <http://localhost:8080/api/logs`
- **페이징**: `PageRequest` 기반, 페이지 번호 및 사이즈 지정 가능
- **정렬**: 생성일 기준 내림차순 정렬
- **검색 조건 사용 (QueryDSL 동적 쿼리)**
- **응답 형식**: `ApiResponse<PageResponse<LogResponseDto>>`

### 로그 자동 생성 기능 (AOP 기반)

- **핵심 기능** : Spring AOP의 `@Around`를 활용하여 특정 서비스 메서드 실행 후 로그 자동 저장
- **적용 대상** :
  - `service` 패키지 하위 클래스만 적용
  - 무한 루프 방지를 위해 `LogService`는 AOP 대상에서 제외
- **활동 유형 매핑**: `LoggingType` Enum을 통해 URI 및 Method 조합에 따라 자동 설정
- **예외 처리**: GET 요청은 로그 생성을 생략 (읽기 요청 제외)

### 김민진

### 전체 태스크 통계 API

- **URL** : `GET http://localhost:8080/api/dashboards`
- **기능**
  - 전체 태스크 통계 정보 제공
  - JPQL 쿼리문을 사용한 전체 데이터, 상태별 데이터 집계
  - 수집한 데이터를 바탕으로 완료율 계산 로직 처리
  - `@AuthenticationPrincipal`을 통해 로그인 된 사용자 식별
  - 로그인 누락 또는 인증 실패 시 `UnauthorizedException` 처리

### 오늘의 태스크 요약 API

- **URL** : `GET http://localhost:8080/api/dashboards/summary`
- **기능**
  - 사용자의 오늘의 태스크 정보 제공
  - JPQL 쿼리문을 사용한 사용자별 데이터 집계(우선순위 정렬)
  - `@AuthenticationPrincipal`을 통해 로그인 된 사용자 식별
  - 로그인 누락 또는 인증 실패 시 `UnauthorizedException` 처리

### 안지현

**[ 회원 가입 ]**

회원가입 API [`POST /api/auth/register`]

- 요청 DTO: username, email, password, name 등
- 입력값 유효성 검증 (`@NotBlank`, `@Email`, `@Size`)
  - username: 4~20자, 영문/숫자
  - password: 8자 이상, 영문/숫자/특수문자 조합
  - email: 형식 체크
  - name: 2~50자
- Bean Validation (JSR-303/380) 적용
- 유효성 실패 시 `MethodArgumentNotValidException` 처리

**[ 회원 수정 ]**

회원조회 API [`GET /api/users/me`]

- 요청 DTO: username, name 등
- 현재 로그인 유저 정보는 `@AuthenticationPrincipal`로 받음
- 입력값 유효성 검증 (`@NotBlank`, `@Size`)
  - username: 4~20자
  - name: 2~50자
- 중복된 username → `409 Conflict`
- 존재하지 않는 유저 → `404 Not Found`
- 유효성 실패 시 → `400 Bad Request` (`MethodArgumentNotValidException`)
- 서비스에서 유저 조회 후 더티 체킹으로 필드 값 변경
- `@Transactional` 내에서 변경 감지 → 자동 반영

**[ 회원 탈퇴 ]**

회원탈퇴 API [ `POST /api/auth/withdraw` ]

- @AuthenticationPrincipal을 이용한 현재 로그인된 사용자 정보 접근
- 비밀번호 불일치 시 → `400 Bad Request`
- 회원 정보 조회
- soft-delete 처리 (`isDeleted = true`, `deletedAt = now()`)
  -> dirty-cheking
- 이메일 재사용 방지 목적
- 동작 확인을 위한 TC 작성

### 안요한

- **특정 태스크에 댓글 작성**
  - `URL: POST <http://localhost:8080/api/tasks/{taskId}/comments>`
  - **기능**
    - RequestBody에서 전달 받은 댓글 속성 Data 저장
    - `@AuthenticationPrincipal` 통한 사용자 식별
    - 댓글 작성 성공 상태값(success: true), 메시지 반환
- **특정 태스크의 댓글 전체 조회**
  - URL: `GET <http://localhost:8080/api/tasks/{taskId}/comments>`
  - **기능**
    - 특정 태스크 댓글 전체 조회
    - 페이지네이션 - 생성일 기준 10개씩 내림차순 정렬
    - 댓글 전체 조회 성공 상태값(success: true), 메시지 반환
- **댓글 단건 조회**
  - URL: `GET <http://localhost:8080/api/tasks/{taskId}/comments/{commentId}>`
  - **기능**
    - 특정 태스크 댓글 단건 조회
    - 댓글 다건 조회 성공 상태값(success: true), 메시지 반환
- **댓글 내용 수정**
  - URL: `PATCH <http://localhost:8080/api/tasks/{taskId}/comments/{commentId}>`
  - **기능**
    - RequestBody에서 전달 받은 댓글 속성 Data 갱신
    - `@AuthenticationPrincipal` 통한 사용자 식별
    - 댓글 수정 성공 상태값(success: true), 메시지 반환
- **댓글 삭제**
  - URL:  `DELETE <http://localhost:8080/api/tasks/{taskId}/comments/{commentId}>`
  - **기능**
    - 특정 태스크 특정 댓글 삭제
    - Soft Delete 방식
    - `@AuthenticationPrincipal` 통한 사용자 식별
    - 댓글 삭제 성공 상태값(success: true), 메시지 반환
- BaseEntity
  - **기능**
    - 각 도메인에 LocalDateTime, isDeleted, deletedAt 필드 제공
    - Soft Delete 기능
- ApiResponse.class (공통 응답 객체)
  - **기능**
    - API 동작 성공 시 일관된 형식으로 Response 반환

## **🔍 새로 배운 내용**

- 최경진

  @AuthenticationPrincipal 가이드 링크 https://che01.tistory.com/76

  Spring Security - SecurityContextHolder와 JWT 인증 활용법 ****https://che01.tistory.com/74

- 안지현

  [dirty-checking 가이드](https://ouo-v.tistory.com/34)

  [http status 상태 코드 장표](https://ouo-v.tistory.com/32)

- 우지운
  - [QueryDSL 사용이유와 사용법 가이드](https://velog.io/@wcw7373/06180433)
  - [Filter vs Interceptor vs AOP](https://velog.io/@wcw7373/06160932)
- 김민진
  - [GlobalExceptionHandler, CommonErrorResponse](https://gsemily11.tistory.com/13)
  - [CheckedException과 UncheckedException 비교](https://gsemily11.tistory.com/14)
- 안요한

  https://yohansdev2.tistory.com/26

- 곽현민
  - 팀프로젝트를 위한 git 구성
    - 링크: https://kwakkwakee99.tistory.com/6
  - JPQL 학습 및 사용
    - 링크: https://kwakkwakee99.tistory.com/7

## **🧰 문제 해결 (트러블 슈팅)**

- 최경진

  JWT 필터 트러블슈팅 ****링크 https://che01.tistory.com/75

  필터 URL 인증 제외 관련 트러블 슈팅 https://che01.tistory.com/78

- 우지운

  [AOP 순환참조 문제](https://velog.io/@wcw7373/06191046)

- 곽현민
  - Git Token 기반 인증 및 ssh 설정 가이드
    - 링크: https://kwakkwakee99.tistory.com/5
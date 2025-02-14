# 🗓️ Spring Boot 기반 캘린더 공유 및 일정 관리 애플리케이션

## 📌 프로젝트 소개
이 애플리케이션은 Spring Boot 3.x 기반의 **사용자 일정 및 캘린더 관리 웹 애플리케이션**입니다.  
사용자는 자신의 캘린더를 생성하고, 이벤트를 추가할 수 있으며, 다른 사용자와 캘린더를 공유하여 협업할 수 있습니다.  

✅ **주요 기능**  
- **사용자 인증 및 관리**
  - 회원가입, 로그인, 로그아웃 지원 (Spring Security 기반)
  - 로그인 상태 유지 (Session 기반 인증)
- **캘린더 관리**
  - 캘린더 생성, 수정, 삭제
  - 캘린더 목록 조회 (내 캘린더 / 공유받은 캘린더)
- **이벤트 관리**
  - 이벤트 생성, 수정, 삭제
  - 캘린더별 이벤트 조회 및 기간 검색
- **캘린더 공유 기능**
  - 특정 사용자에게 캘린더 공유 가능 (읽기, 쓰기, 수정 권한 설정)
  - 공유된 캘린더에서 권한에 따라 접근 제한
- **RESTful API 제공**
  - RESTful API 기반 데이터 연동 (Spring Data JPA + QueryDSL)
  - Swagger 문서 자동 생성

---

## 🚀 기술 스택
| 기술  | 설명 |
|-------|------|
| **Backend** | Java 17, Spring Boot 3.x, Spring Security, Spring Data JPA, QueryDSL |
| **Database** | H2 Database (개발환경) / MySQL (운영환경) |
| **Frontend** | Thymeleaf, Bootstrap, JavaScript (ES6) |
| **API Documentation** | Swagger (Springdoc OpenAPI) |

---

## 🔧 프로젝트 설치 및 실행 방법

### 1️⃣ **프로젝트 클론**
```bash
git clone https://github.com/your-github-username/calendar-app.git
cd calendar-app
```

### 2️⃣ **환경 설정**
개발이나 테스트 용도로 가볍고 편리한 H2 Database 를 사용했습니다. 또한, 웹 화면을 제공합니다.

**✅ H2 Database 설치 및 실행 방법**
1. [H2 Database 홈페이지](https://www.h2database.com/html/main.html) 에서 OS에 맞는 최신 버전 설치.
2. 설치된 경로(~/h2/bin)로 이동 후, h2.sh 혹은 h2.bat 를 실행.
3. 웹 화면이 제공되는데, JDBC URL 에 `jdbc:h2:~/calendar` 입력.(최초 한번)
4. `~/calendar.mv.db` 파일 생성 확인.
5. 이후 부터는 다음과 같이 접속 `jdbc:h2:tcp://localhost/~/calendar`
  + cf. application.yml 파일에 설정된 db 접속 경로와 생성된 `.mv.db` 파일의 경로를 맞춰 주세요.

MySQL 사용 시, application.yml 파일의 Database 접속 정보를 로컬환경에 맞게 수정해주세요.

### 3️⃣ **프로젝트 빌드**
``` bash
./gradlew clean build
```

### 4️⃣ **애플리케이션 실행**
``` bash
java -jar build/libs/calendar-app-0.0.1-SNAPSHOT.jar
```

✅ 서버가 실행되면 아래 URL에서 애플리케이션에 접속할 수 있습니다.
- http://localhost:8080

✅ Swagger를 통해 API 문서를 확인할 수 있습니다.
- http://localhost:8080/swagger-ui/index.html

---

## 📌 프로젝트에서 사용된 라이브러리 및 사용 이유

**Spring boot (3.x)**
- Spring 환경을 자동 설정하여 빠른 개발 가능 (@SpringBootApplication)
- 내장 Tomcat 제공으로 별도의 웹 서버 설정 없이 실행 가능
- YAML 기반 설정(application.yml)으로 환경 설정 관리 용이
- Spring Security, JPA 등의 스타터 패키지를 통해 간편한 통합 가능

**Spring Security**
- 사용자 인증 (로그인/로그아웃) 및 권한 관리 (OAuth2, JWT, Session 지원)
- API 보안 강화 (엔드포인트 접근 제어)
- CSRF, CORS, XSS, 세션 고정 공격 방어
- Custom UserDetailsService 구현 가능 (회원 정보 관리)

**Spring Data JPA**
- JPARepository 인터페이스만으로 기본적인 CRUD 제공 (save(), findById(), delete())
- JPQL & 네이티브 쿼리를 사용할 필요 없이 메서드 네이밍만으로 쿼리 생성 가능 (findByUsername())
- 트랜잭션 관리가 자동화되어 데이터 일관성 유지 가능
- H2, MySQL, PostgreSQL 등 다양한 DB 지원

**QueryDSL**
- JPQL보다 가독성이 뛰어나고 타입 안정성이 보장됨
- 복잡한 동적 쿼리를 손쉽게 구현 가능
- JPA와 함께 사용 시 성능 최적화 가능
- Predicate API를 활용하여 조건문 기반 검색 가능 (예: 사용자 검색, 필터링)

**Thymeleaf**
- 템플릿 엔진이므로 JSP보다 가독성이 뛰어나며, HTML과 자연스럽게 결합 가능
- 서버에서 데이터를 렌더링하여 전달하므로 보안성이 높음
- Spring MVC와 쉽게 통합 가능 (@Controller, Model)
- 반복문, 조건문 등 템플릿 문법 제공

**Lombok**
- @Getter, @Setter, @ToString 사용으로 코드 간결화 가능
- @NoArgsConstructor, @AllArgsConstructor, @Builder 등을 통해 객체 생성 간소화
- JPA 엔티티에서 @Entity와 함께 사용 시 불필요한 코드를 줄일 수 있음

**Swagger (springdoc-openapi)**
- 자동으로 API 문서 생성 (/swagger-ui/index.html)
- API 테스트를 웹 UI에서 바로 실행 가능
- 클라이언트 및 프론트엔드 개발자와 API 공유 용이
- API 응답 형식, 파라미터 등을 쉽게 확인 가능

**Bootstrap**
- Thymeleaf와 함께 사용하여 UI를 손쉽게 구성 가능
- 반응형 디자인 지원 (모바일, 태블릿, PC 최적화)
- 이미 스타일이 적용된 UI 컴포넌트 제공 (버튼, 폼, 네비게이션 바 등)
- CDN을 활용하여 추가적인 설치 없이 사용 가능

---

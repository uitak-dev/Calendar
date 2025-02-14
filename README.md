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


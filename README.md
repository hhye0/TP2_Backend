# ReTalk
누구나 자유롭게 참여하는 독서 토론 플랫폼 <br>

##### @김민정 : Meeting, Meeting_member, DiscussionSchedule, Chat 기능 담당
##### @정혜영 : User, BookReview, Notice, Admin 기능 담당
##### @최지우 : 실시간 채팅 기능 담당

## BackEnd
#### BackEnd 초기 실행 방법

```
#1. mariaDB 접속
 mysql-u root -p
 
#2. 비번 입력 후 데이터베이스 보기
 MariaDB [(none)]> show databases;
 
#3. 목록에 mysql이 있다면 선택
 MariaDB [(none)]> use mysql;
 
#4. bookclub_db 생성 및 설정
 MariaDB [mysql]> create database bookclub_db; 
 MariaDB [mysql]> CREATE USER 'bookclub'@'%' IDENTIFIED BY 'bookclub';
 MariaDB [mysql]> GRANT ALL PRIVILEGES ON bookclub_db.* TO 'bookclub'@'%';
 MariaDB [mysql]> flush privileges; 
 MariaDB [mysql]> select user, host from user;
 MariaDB [mysql]> exit;
 
#5. bookclub 사용자 계정으로 접속한다.
 mysql-u bookclub -p
 bookclub 입력
 MariaDB [mysql]> use boot_db;
 
# DB 저장 가능
```

#### 만약 DB와 사용자 이름이 다르다면?
##### application.properties에 들어가 { } 변경
```
spring.datasource.url=jdbc:mariadb://localhost:3306/{DB 명}
spring.datasource.username={사용자 이름}
spring.datasource.password={비밀번호}
```
<hr>

### 주요 기능 설명
#### 회원 기능
1. 회원가입, 로그인 (JWT 기반 인증)
2. 사용자 닉네임 수정
3. 내 정보 조회 및 수정
4. 사용자와 관리자 구분
#### 모임 기능
1. 모임 생성/ 수정/ 삭제 가능
2. 모임 참여/ 탈퇴 가능
3. 모임 상세 조회
#### 모임 내부 (일정, 채팅, 공지사항) 기능
1. 모임 일정이 정해 질 시 스케줄 등록 가능
2. 모임 참여 등록 시 호스트 수락/거절 가능
3. 모임별 공지사항 작성 및 조회
4. Sendbird API 기반 채팅 채널 자동 생성
5. 참여자 자동 초대, 강퇴 시 자동 제거
6. 관리자 채팅 on/off 기능
#### 책 리뷰
1. 알라딘 API 기반 책 제목 검색
2. 사용자가 책 제목 검색 시 자동으로 정보(author, cover, isbn)가 넘어옴
3. 리뷰 작성 시 책 제목과 정보, 리뷰 등이 자동 저장
4. 사용자 리뷰 수정 및 삭제 가능
#### 인증 & 보안
1. @CurrentUser 커스텀 어노테이션으로 로그인 유저 주입
2. 관리자(ADMIN), 일반 사용자(USER) 권한 분리
3. .env 파일 관리

<hr>

### 기술 스택
| 항목         | 내용                     |
| ---------- |------------------------|
| Language   | Java 17                |
| Framework  | Spring Boot 3.x        |
| Build Tool | Gradle                 |
| DB         | MySQL                  |
| ORM        | Spring Data JPA        |
| 보안         | Spring Security + JWT  |
| 실시간 채팅     | Sendbird Open API      |
| API 호출     | RestTemplate           |
| 환경 변수 관리   | dotenv (.env 파일 사용)    |
| 인증 방식      | JWT (토큰 기반 인증/인가)      |
| API 테스트 도구 | Postman                |

<hr>

### 전체 구조

```
com.teamproject.TP_backend
├── config                          # 전역 설정 클래스
│   ├── security                    # 보안 관련 컴포넌트
│   │   ├── CustomUserDetails              # UserDetails 구현체
│   │   ├── JwtAuthenticationFilter        # JWT 인증 필터 (요청 시 토큰 검증)
│   │   ├── JwtUtil                        # JWT 토큰 생성 및 검증 유틸
│   │   ├── CurrentUser                    # 인증 유저 주입용 커스텀 어노테이션
│   │   ├── CurrentUserArgumentResolver    # @CurrentUser 파라미터 주입 처리
│   │   └── SecurityConfig                 # Spring Security 전체 설정 클래스
│   ├── AppConfig                   # 공용 Bean 등록 (예: RestTemplate 등)
│   └── WebConfig                   # MVC 설정, CORS, ArgumentResolver 등
│
├── controller                      # REST API 엔드포인트 정의
│   ├── AuthController              # 회원가입, 로그인 처리
│   ├── BookController              # 책 검색 (예: 알라딘 API)
│   ├── ChatController              # 모임 내 채팅 모니터링/관리
│   ├── DiscussionScheduleController # 토론 스케줄 관리
│   ├── MeetingController           # 모임 생성, 조회, 수정
│   ├── MeetingMemberController     # 모임 신청/승인/거절
│   ├── NoticeController            # 공지사항 CRUD
│   ├── UserController              # 유저 정보 조회/수정
│   ├── AdminController             # 관리자 전용 API (모든 모임, 채팅 차단 등)
│   └── TestController              # 테스트용 API
│
├── dto                             # 클라이언트와 주고받는 데이터 객체
│   ├── ApplicantDTO                # 신청자 정보 전달용 DTO
│   ├── BookDTO                     # 책 정보 DTO
│   ├── BookReviewRequestDTO        # 리뷰 등록 요청 DTO
│   ├── BookReviewResponseDTO       # 리뷰 조회 응답 DTO
│   ├── BookReviewUpdateDTO         # 리뷰 수정 요청 DTO
│   ├── DiscussionScheduleCreateDTO # 토론 일정 등록 요청 DTO
│   ├── DiscussionScheduleDTO       # 토론 일정 응답 DTO
│   ├── LoginRequestDTO             # 로그인 요청 DTO (email, password)
│   ├── MeetingDTO                  # 모임 정보 요청/응답 DTO (통합 사용)
│   ├── NoticeRequestDTO            # 공지 등록/수정 요청 DTO
│   ├── NoticeResponseDTO           # 공지 조회 응답 DTO
│   ├── ParticipantDTO              # 참가자 목록 응답 DTO
│   ├── SignupRequestDTO            # 회원가입 요청 DTO
│   ├── UserDTO                     # 유저 프로필 응답 DTO
│   └── UserUpdateRequestDTO        # 유저 정보 수정 요청 DTO
│
├── domain                          # 도메인 모델 및 enum
│   ├── entity                      # JPA 엔티티 클래스들
│   │   ├── User                    # 유저 정보
│   │   ├── Meeting                 # 독서모임
│   │   ├── MeetingMember          # 모임 신청/승인 상태
│   │   ├── BookReview             # 책 리뷰
│   │   ├── DiscussionSchedule     # 토론 일정
│   │   └── Notice                 # 공지사항
│   └── enums                       # 열거형 클래스
│       ├── UserRole               # ROLE_USER / ROLE_ADMIN
│       ├── GroupRole              # HOST / PARTICIPANT
│       └── ParticipationStatus    # 신청됨 / 승인됨 / 거절됨
│
├── repository                      # JPA Repository 인터페이스
│   ├── BookReviewRepository        # BookReview 엔티티 DB 접근
│   ├── DiscussionScheduleRepository # DiscussionSchedule 엔티티 DB 접근
│   ├── MeetingRepository           # Meeting 엔티티 DB 접근
│   ├── MeetingMemberRepository     # MeetingMember 엔티티 DB 접근
│   ├── NoticeRepository            # Notice 엔티티 DB 접근
│   └── UserRepository              # User 엔티티 DB 접근
│
├── service                         # 비즈니스 로직 처리
│   ├── AdminService                # 관리자용 기능 (모임 전체 조회 등)
│   ├── BookReviewService           # 리뷰 등록, 수정, 삭제
│   ├── BookSearchService           # 외부 API 연동 (책 검색용)
│   ├── ChatService                 # 채팅 모니터링/제어
│   ├── CustomUserDetailsService    # 사용자 인증에 필요한 UserDetails 반환
│   ├── DiscussionScheduleService   # 토론 일정 관리
│   ├── MeetingService              # 모임 CRUD 및 호스트 관련 로직
│   ├── MeetingMemberService        # 모임 신청/승인/거절
│   ├── NoticeService               # 공지 CRUD 처리
│   └── UserService                 # 유저 정보 수정, 조회
│
├── exception                       # 전역 및 커스텀 예외 클래스들
│   ├── GlobalExceptionHandler      # @ControllerAdvice 예외 핸들러
│   ├── InvalidJwtException         # 잘못된 JWT 예외
│   ├── PastDateScheduleException   # 지난 날짜로 토론 일정을 생성했을 때
│   ├── ReviewNotFoundException     # 리뷰가 없을 때
│   ├── UnauthorizedAccessException # 권한 없음
│   └── UserAlreadyExistsException  # 이미 가입된 유저
│
├── runner                          # 애플리케이션 시작 시 실행 로직
│   └── TestDataInitializer         # 테스트용 더미 데이터 삽입
│
├── resources
│   ├── static                      # 정적 리소스 (CSS, JS 등)
│   ├── templates                   # 템플릿 파일 (Thymeleaf 등 사용 시)
│   ├── application.properties      # Spring Boot 설정 파일
│   └── schema.sql                  # 초기 DB 스키마 정의
│
└── TpBackendApplication.java       # Spring Boot 메인 클래스
```
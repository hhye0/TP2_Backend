# ReTalk
누구나 자유롭게 참여하는 독서 토론 플랫폼 <br>
### Member
#### 김민정 : Meeting, Meeting_member, DiscussionSchedule, Chat 기능 담당
#### 정혜영 : User, BookReview, Notice, Admin 기능 담당
#### 최지우 : 실시간 채팅 기능 담당

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
##### application.properties에 들어가
```
spring.datasource.url=jdbc:mariadb://localhost:3306/{DB 명}
spring.datasource.username={사용자 이름}
spring.datasource.password={비밀번호}
```
##### 변경

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
4. 
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



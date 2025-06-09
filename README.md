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
-- meetings 테이블 is_active 기본값 추가
ALTER TABLE meetings MODIFY COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- users 테이블 email 중복 방지
--ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE (email);
------ 테이블 삭제 -------------------------------------------------------------
DROP TABLE SEMI_MEMBER;				-- 12번

DROP TABLE SEMI_BOARD;				-- 10번
DROP TABLE SEMI_BOARD_IMG;			-- 9번
DROP TABLE SEMI_BOARD_TYPE;			-- 11번
DROP TABLE SEMI_COMMENT_BOARD;		-- 8번

DROP TABLE SEMI_RECIPE;				-- 6번
DROP TABLE SEMI_RECIPE_IMG;			-- 5번
DROP TABLE SEMI_RECIPE_INGREDIENT;	-- 4번
DROP TABLE SEMI_INGREDIENT_SPEC;	-- 7번
DROP TABLE SEMI_COMMENT_RECIPE;		-- 3번

DROP TABLE SEMI_BOARD_LIKE;			-- 2번
DROP TABLE SEMI_RECIPE_LIKE;		-- 1번
------ 시퀀스 삭제 -------------------------------------------------------------
DROP SEQUENCE SEQ_SEMI_MEMBER;

DROP SEQUENCE SEQ_SEMI_BOARD;
DROP SEQUENCE SEQ_SEMI_BOARD_IMG;
DROP SEQUENCE SEQ_SEMI_BOARD_TYPE;
DROP SEQUENCE SEQ_SEMI_COMMENT_BOARD;

DROP SEQUENCE SEQ_SEMI_RECIPE;
DROP SEQUENCE SEQ_SEMI_RECIPE_IMG;
DROP SEQUENCE SEQ_SEMI_RECIPE_INGREDIENT;
DROP SEQUENCE SEQ_SEMI_INGREDIENT_SPEC;
DROP SEQUENCE SEQ_SEMI_COMMENT_RECIPE;

------ 시퀀스 생성 -------------------------------------------------------------
CREATE SEQUENCE SEQ_SEMI_MEMBER NOCACHE;

CREATE SEQUENCE SEQ_SEMI_BOARD NOCACHE;
CREATE SEQUENCE SEQ_SEMI_BOARD_IMG NOCACHE;
CREATE SEQUENCE SEQ_SEMI_BOARD_TYPE NOCACHE;
CREATE SEQUENCE SEQ_SEMI_COMMENT_BOARD NOCACHE;

CREATE SEQUENCE SEQ_SEMI_RECIPE NOCACHE;
CREATE SEQUENCE SEQ_SEMI_RECIPE_IMG NOCACHE;
CREATE SEQUENCE SEQ_SEMI_RECIPE_INGREDIENT NOCACHE;
CREATE SEQUENCE SEQ_SEMI_INGREDIENT_SPEC NOCACHE;
CREATE SEQUENCE SEQ_SEMI_COMMENT_RECIPE NOCACHE;


----- 회원가입, 비밀번호 찾기 시 이메일, 인증키 저장테이블 -----

CREATE TABLE "SEMI_TB_AUTH_KEY"(
	"KEY_NO"    NUMBER PRIMARY KEY,
	"EMAIL"	    NVARCHAR2(50) NOT NULL,
	"AUTH_KEY"  CHAR(6)	NOT NULL,
	"CREATE_TIME" DATE DEFAULT SYSDATE NOT NULL
);

COMMENT ON COLUMN "SEMI_TB_AUTH_KEY"."KEY_NO"      IS '인증키 구분 번호(시퀀스)';
COMMENT ON COLUMN "SEMI_TB_AUTH_KEY"."EMAIL"       IS '인증 이메일';
COMMENT ON COLUMN "SEMI_TB_AUTH_KEY"."AUTH_KEY"    IS '인증 번호';
COMMENT ON COLUMN "SEMI_TB_AUTH_KEY"."CREATE_TIME" IS '인증 번호 생성 시간';

SELECT * FROM "SEMI_TB_AUTH_KEY";

CREATE SEQUENCE SEQ_SEMI_TB_AUTH_KEY NOCACHE; -- 인증키 구분 번호 시퀀스



------ 테이블 생성 -------------------------------------------------------------

CREATE TABLE "SEMI_MEMBER" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"MEMBER_EMAIL"	NVARCHAR2(50)		NOT NULL,
	"MEMBER_PW"	NVARCHAR2(100)		NOT NULL,
	"MEMBER_NAME"	NVARCHAR2(10)		NOT NULL,
	"MEMBER_NICKNAME"	NVARCHAR2(10)		NOT NULL,
	"MEMBER_TEL"	CHAR(11)		NOT NULL,
	"MEMBER_ADDRESS"	NVARCHAR2(300)		NULL,
	"PROFILE_IMG"	VARCHAR2(300)		NULL,
	"ENROLL_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MEMBER_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"AUTHORITY"	NUMBER	DEFAULT 1	NOT NULL
);

COMMENT ON COLUMN "SEMI_MEMBER"."MEMBER_NO" IS '회원번호(PK)';

COMMENT ON COLUMN "SEMI_MEMBER"."MEMBER_EMAIL" IS '회원이메일';

COMMENT ON COLUMN "SEMI_MEMBER"."MEMBER_PW" IS '회원비밀번호';

COMMENT ON COLUMN "SEMI_MEMBER"."MEMBER_NAME" IS '회원 이름';

COMMENT ON COLUMN "SEMI_MEMBER"."MEMBER_NICKNAME" IS '회원 닉네임';

COMMENT ON COLUMN "SEMI_MEMBER"."MEMBER_TEL" IS '회원전화번호';

COMMENT ON COLUMN "SEMI_MEMBER"."MEMBER_ADDRESS" IS '회원주소';

COMMENT ON COLUMN "SEMI_MEMBER"."PROFILE_IMG" IS '프로필이미지';

COMMENT ON COLUMN "SEMI_MEMBER"."ENROLL_DATE" IS '회원가입일';

COMMENT ON COLUMN "SEMI_MEMBER"."MEMBER_DEL_FL" IS '탈퇴여부(Y/N)';

COMMENT ON COLUMN "SEMI_MEMBER"."AUTHORITY" IS '권한(1:일반, 2:관리자)';

CREATE TABLE "SEMI_BOARD" (
	"BOARD_NO"	NUMBER		NOT NULL,
	"BOARD_TITLE"	NVARCHAR2(100)		NOT NULL,
	"BOARD_CONTENT"	NVARCHAR2(2000)		NOT NULL,
	"BOARD_WRITE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"BOARD_UPDATE_DATE"	DATE		NULL,
	"BOARD_READ_COUNT"	NUMBER	DEFAULT 0	NOT NULL,
	"BOARD_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL,
	"BOARD_CODE"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_BOARD"."BOARD_NO" IS '일반게시글 번호(PK)';

COMMENT ON COLUMN "SEMI_BOARD"."BOARD_TITLE" IS '게시글 제목';

COMMENT ON COLUMN "SEMI_BOARD"."BOARD_CONTENT" IS '게시글 내용';

COMMENT ON COLUMN "SEMI_BOARD"."BOARD_WRITE_DATE" IS '게시글 작성일';

COMMENT ON COLUMN "SEMI_BOARD"."BOARD_UPDATE_DATE" IS '게시글 마지막 수정일';

COMMENT ON COLUMN "SEMI_BOARD"."BOARD_READ_COUNT" IS '조회수';

COMMENT ON COLUMN "SEMI_BOARD"."BOARD_DEL_FL" IS '게시글 삭제여부(Y/N)';

COMMENT ON COLUMN "SEMI_BOARD"."MEMBER_NO" IS '회원번호(PK)';

COMMENT ON COLUMN "SEMI_BOARD"."BOARD_CODE" IS '일반게시판 종류 코드번호(PK)';

CREATE TABLE "SEMI_BOARD_TYPE" (
	"BOARD_CODE"	NUMBER		NOT NULL,
	"BOARD_NAME"	NVARCHAR2(20)		NOT NULL
);

COMMENT ON COLUMN "SEMI_BOARD_TYPE"."BOARD_CODE" IS '일반게시판 종류 코드번호(PK)';

COMMENT ON COLUMN "SEMI_BOARD_TYPE"."BOARD_NAME" IS '게시판 이름';

CREATE TABLE "SEMI_BOARD_IMG" (
	"BOARD_IMG_NO"	NUMBER		NOT NULL,
	"BOARD_IMG_PATH"	VARCHAR2(200)		NOT NULL,
	"BOARD_IMG_ORIGINAL_NAME"	NVARCHAR2(50)		NOT NULL,
	"BOARD_IMG_RENAME"	NVARCHAR2(50)		NOT NULL,
	"BOARD_IMG_ORDER"	NUMBER		NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_BOARD_IMG"."BOARD_IMG_NO" IS '게시글 이미지번호(PK)';

COMMENT ON COLUMN "SEMI_BOARD_IMG"."BOARD_IMG_PATH" IS '게시글 이미지 요청 경로';

COMMENT ON COLUMN "SEMI_BOARD_IMG"."BOARD_IMG_ORIGINAL_NAME" IS '게시글 이미지 원본명';

COMMENT ON COLUMN "SEMI_BOARD_IMG"."BOARD_IMG_RENAME" IS '게시글 이미지 변경명';

COMMENT ON COLUMN "SEMI_BOARD_IMG"."BOARD_IMG_ORDER" IS '게시글 이미지 순서';

COMMENT ON COLUMN "SEMI_BOARD_IMG"."BOARD_NO" IS '일반게시글 번호(PK)';

CREATE TABLE "SEMI_RECIPE_IMG" (
	"RECIPE_IMG_NO"	NUMBER		NOT NULL,
	"RECIPE_IMG_PATH"	VARCHAR2(200)		NOT NULL,
	"RECIPE_IMG_ORIGINAL_NAME"	NVARCHAR2(50)		NOT NULL,
	"RECIPE_IMG_RENAME"	NVARCHAR2(50)		NOT NULL,
	"RECIPE_IMG_ORDER"	NUMBER		NULL,
	"RECIPE_VIDEO_URL"	NVARCHAR2(200)		NULL,
	"RECIPE_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_RECIPE_IMG"."RECIPE_IMG_NO" IS '레시피 이미지 번호(PK)';

COMMENT ON COLUMN "SEMI_RECIPE_IMG"."RECIPE_IMG_PATH" IS '레시피 이미지 요청 경로';

COMMENT ON COLUMN "SEMI_RECIPE_IMG"."RECIPE_IMG_ORIGINAL_NAME" IS '레시피 이미지 원본명';

COMMENT ON COLUMN "SEMI_RECIPE_IMG"."RECIPE_IMG_RENAME" IS '레시피 이미지 변경명';

COMMENT ON COLUMN "SEMI_RECIPE_IMG"."RECIPE_IMG_ORDER" IS '레시피 이미지 순서';

COMMENT ON COLUMN "SEMI_RECIPE_IMG"."RECIPE_VIDEO_URL" IS '레시피 영상 URL';

COMMENT ON COLUMN "SEMI_RECIPE_IMG"."RECIPE_NO" IS '레시피번호(PK)';

CREATE TABLE "SEMI_INGREDIENT_SPEC" (
	"INGREDIENT_NO"	NUMBER		NOT NULL,
	"INGREDIENT_NAME"	NVARCHAR2(50)		NOT NULL,
	"INGREDIENT_TYPE"	NUMBER		NOT NULL,
	"PRICE_PER_INGRED"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_INGREDIENT_SPEC"."INGREDIENT_NO" IS '재료 번호(PK)';

COMMENT ON COLUMN "SEMI_INGREDIENT_SPEC"."INGREDIENT_NAME" IS '재료명';

COMMENT ON COLUMN "SEMI_INGREDIENT_SPEC"."INGREDIENT_TYPE" IS '재료가격(원데이터)';

COMMENT ON COLUMN "SEMI_INGREDIENT_SPEC"."PRICE_PER_INGRED" IS '단위당가격(g/원)';

CREATE TABLE "SEMI_RECIPE" (
	"RECIPE_NO"	NUMBER		NOT NULL,
	"RECIPE_NAME"	NVARCHAR2(100)		NOT NULL,
	"RECIPE_SUBTITLE"	NVARCHAR2(50)		NOT NULL,
	"RECIPE_CONTENT"	NVARCHAR2(2000)		NULL,
	"RECIPE_WRITE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"RECIPE_READ_COUNT"	NUMBER	DEFAULT 0	NOT NULL,
	"RECIPE_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_RECIPE"."RECIPE_NO" IS '레시피번호(PK)';

COMMENT ON COLUMN "SEMI_RECIPE"."RECIPE_NAME" IS '레시피 이름';

COMMENT ON COLUMN "SEMI_RECIPE"."RECIPE_SUBTITLE" IS '레시피 꾸밈말';

COMMENT ON COLUMN "SEMI_RECIPE"."RECIPE_CONTENT" IS '레시피 내용';

COMMENT ON COLUMN "SEMI_RECIPE"."RECIPE_WRITE_DATE" IS '레시피 작성일';

COMMENT ON COLUMN "SEMI_RECIPE"."RECIPE_READ_COUNT" IS '레시피 조회수';

COMMENT ON COLUMN "SEMI_RECIPE"."RECIPE_DEL_FL" IS '레시피 삭제여부(Y/N)';

COMMENT ON COLUMN "SEMI_RECIPE"."MEMBER_NO" IS '회원번호(PK)';

CREATE TABLE "SEMI_RECIPE_INGREDIENT" (
	"RECIPE_ING_NO"	NUMBER		NOT NULL,
	"INGRED_VOLUME"	NUMBER		NOT NULL,
	"INGREDIENT_NO"	NUMBER		NOT NULL,
	"RECIPE_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_RECIPE_INGREDIENT"."RECIPE_ING_NO" IS '레시피별 요리재료 번호(PK)';

COMMENT ON COLUMN "SEMI_RECIPE_INGREDIENT"."INGRED_VOLUME" IS '재료용량(각 재료당 필요용량)';

COMMENT ON COLUMN "SEMI_RECIPE_INGREDIENT"."INGREDIENT_NO" IS '재료 번호(PK)';

COMMENT ON COLUMN "SEMI_RECIPE_INGREDIENT"."RECIPE_NO" IS '레시피번호(PK)';

CREATE TABLE "SEMI_BOARD_LIKE" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_BOARD_LIKE"."MEMBER_NO" IS '회원번호(PK)';

COMMENT ON COLUMN "SEMI_BOARD_LIKE"."BOARD_NO" IS '일반게시글 번호(PK)';

CREATE TABLE "SEMI_COMMENT_BOARD" (
	"COMMENT_NO_BOARD"	NUMBER		NOT NULL,
	"COMMENT_CONTENT_BOARD"	NVARCHAR2(500)		NOT NULL,
	"COMMENT_DEL_FL_BOARD"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"COMMENT_WRITE_DATE_BOARD"	DATE	DEFAULT SYSDATE	NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL,
	"PARENT_COMMENT_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_COMMENT_BOARD"."COMMENT_NO_BOARD" IS '댓글번호(PK)';

COMMENT ON COLUMN "SEMI_COMMENT_BOARD"."COMMENT_CONTENT_BOARD" IS '댓글 내용';

COMMENT ON COLUMN "SEMI_COMMENT_BOARD"."COMMENT_DEL_FL_BOARD" IS '댓글 삭제여부(Y/N)';

COMMENT ON COLUMN "SEMI_COMMENT_BOARD"."COMMENT_WRITE_DATE_BOARD" IS '댓글작성일';

COMMENT ON COLUMN "SEMI_COMMENT_BOARD"."BOARD_NO" IS '일반게시글 번호(PK)';

COMMENT ON COLUMN "SEMI_COMMENT_BOARD"."MEMBER_NO" IS '회원번호(PK)';

COMMENT ON COLUMN "SEMI_COMMENT_BOARD"."PARENT_COMMENT_NO" IS '부모 댓글번호(FK)';

CREATE TABLE "SEMI_COMMENT_RECIPE" (
	"COMMENT_NO_RECIPE"	NUMBER		NOT NULL,
	"COMMENT_CONTENT_RECIPE"	NVARCHAR2(500)		NOT NULL,
	"COMMENT_DEL_FL_RECIPE"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"COMMENT_WRITE_DATE_RECIPE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"RECIPE_NO"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL,
	"COMMENT_NO2"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_COMMENT_RECIPE"."COMMENT_NO_RECIPE" IS '댓글번호(PK)';

COMMENT ON COLUMN "SEMI_COMMENT_RECIPE"."COMMENT_CONTENT_RECIPE" IS '댓글 내용';

COMMENT ON COLUMN "SEMI_COMMENT_RECIPE"."COMMENT_DEL_FL_RECIPE" IS '댓글 삭제여부(Y/N)';

COMMENT ON COLUMN "SEMI_COMMENT_RECIPE"."COMMENT_WRITE_DATE_RECIPE" IS '댓글작성일';

COMMENT ON COLUMN "SEMI_COMMENT_RECIPE"."RECIPE_NO" IS '레시피번호(PK)';

COMMENT ON COLUMN "SEMI_COMMENT_RECIPE"."MEMBER_NO" IS '회원번호(PK)';

COMMENT ON COLUMN "SEMI_COMMENT_RECIPE"."COMMENT_NO2" IS '댓글번호(PK)';

CREATE TABLE "SEMI_RECIPE_LIKE" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"RECIPE_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "SEMI_RECIPE_LIKE"."MEMBER_NO" IS '회원번호(PK)';

COMMENT ON COLUMN "SEMI_RECIPE_LIKE"."RECIPE_NO" IS '레시피번호(PK)';


---- FK -------------------------------------------------------

ALTER TABLE "SEMI_MEMBER" ADD CONSTRAINT "PK_SEMI_MEMBER" PRIMARY KEY (
	"MEMBER_NO"
);

ALTER TABLE "SEMI_BOARD" ADD CONSTRAINT "PK_SEMI_BOARD" PRIMARY KEY (
	"BOARD_NO"
);

ALTER TABLE "SEMI_BOARD_TYPE" ADD CONSTRAINT "PK_SEMI_BOARD_TYPE" PRIMARY KEY (
	"BOARD_CODE"
);

ALTER TABLE "SEMI_BOARD_IMG" ADD CONSTRAINT "PK_SEMI_BOARD_IMG" PRIMARY KEY (
	"BOARD_IMG_NO"
);

ALTER TABLE "SEMI_RECIPE_IMG" ADD CONSTRAINT "PK_SEMI_RECIPE_IMG" PRIMARY KEY (
	"RECIPE_IMG_NO"
);

ALTER TABLE "SEMI_INGREDIENT_SPEC" ADD CONSTRAINT "PK_SEMI_INGREDIENT_SPEC" PRIMARY KEY (
	"INGREDIENT_NO"
);

ALTER TABLE "SEMI_RECIPE" ADD CONSTRAINT "PK_SEMI_RECIPE" PRIMARY KEY (
	"RECIPE_NO"
);

ALTER TABLE "SEMI_RECIPE_INGREDIENT" ADD CONSTRAINT "PK_SEMI_RECIPE_INGREDIENT" PRIMARY KEY (
	"RECIPE_ING_NO"
);

ALTER TABLE "SEMI_BOARD_LIKE" ADD CONSTRAINT "PK_SEMI_BOARD_LIKE" PRIMARY KEY (
	"MEMBER_NO",
	"BOARD_NO"
);

ALTER TABLE "SEMI_COMMENT_BOARD" ADD CONSTRAINT "PK_SEMI_COMMENT_BOARD" PRIMARY KEY (
	"COMMENT_NO_BOARD"
);

ALTER TABLE "SEMI_COMMENT_RECIPE" ADD CONSTRAINT "PK_SEMI_COMMENT_RECIPE" PRIMARY KEY (
	"COMMENT_NO_RECIPE"
);

ALTER TABLE "SEMI_RECIPE_LIKE" ADD CONSTRAINT "PK_SEMI_RECIPE_LIKE" PRIMARY KEY (
	"MEMBER_NO",
	"RECIPE_NO"
);

ALTER TABLE "SEMI_BOARD" ADD CONSTRAINT "FK_SEMI_MEMBER_TO_SEMI_BOARD_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "SEMI_MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "SEMI_BOARD" ADD CONSTRAINT "FK_SEMI_BOARD_TYPE_TO_SEMI_BOARD_1" FOREIGN KEY (
	"BOARD_CODE"
)
REFERENCES "SEMI_BOARD_TYPE" (
	"BOARD_CODE"
);

ALTER TABLE "SEMI_BOARD_IMG" ADD CONSTRAINT "FK_SEMI_BOARD_TO_SEMI_BOARD_IMG_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "SEMI_BOARD" (
	"BOARD_NO"
);

ALTER TABLE "SEMI_RECIPE_IMG" ADD CONSTRAINT "FK_SEMI_RECIPE_TO_SEMI_RECIPE_IMG_1" FOREIGN KEY (
	"RECIPE_NO"
)
REFERENCES "SEMI_RECIPE" (
	"RECIPE_NO"
);

ALTER TABLE "SEMI_RECIPE" ADD CONSTRAINT "FK_SEMI_MEMBER_TO_SEMI_RECIPE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "SEMI_MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "SEMI_RECIPE_INGREDIENT" ADD CONSTRAINT "FK_SEMI_INGREDIENT_SPEC_TO_SEMI_RECIPE_INGREDIENT_1" FOREIGN KEY (
	"INGREDIENT_NO"
)
REFERENCES "SEMI_INGREDIENT_SPEC" (
	"INGREDIENT_NO"
);

ALTER TABLE "SEMI_RECIPE_INGREDIENT" ADD CONSTRAINT "FK_SEMI_RECIPE_TO_SEMI_RECIPE_INGREDIENT_1" FOREIGN KEY (
	"RECIPE_NO"
)
REFERENCES "SEMI_RECIPE" (
	"RECIPE_NO"
);

ALTER TABLE "SEMI_BOARD_LIKE" ADD CONSTRAINT "FK_SEMI_MEMBER_TO_SEMI_BOARD_LIKE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "SEMI_MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "SEMI_BOARD_LIKE" ADD CONSTRAINT "FK_SEMI_BOARD_TO_SEMI_BOARD_LIKE_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "SEMI_BOARD" (
	"BOARD_NO"
);

ALTER TABLE "SEMI_COMMENT_BOARD" ADD CONSTRAINT "FK_SEMI_BOARD_TO_SEMI_COMMENT_BOARD_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "SEMI_BOARD" (
	"BOARD_NO"
);

ALTER TABLE "SEMI_COMMENT_BOARD" ADD CONSTRAINT "FK_SEMI_MEMBER_TO_SEMI_COMMENT_BOARD_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "SEMI_MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "SEMI_COMMENT_BOARD" ADD CONSTRAINT "FK_SEMI_COMMENT_BOARD_TO_SEMI_COMMENT_BOARD_1" FOREIGN KEY (
	"PARENT_COMMENT_NO"
)
REFERENCES "SEMI_COMMENT_BOARD" (
	"COMMENT_NO_BOARD"
);

ALTER TABLE "SEMI_COMMENT_RECIPE" ADD CONSTRAINT "FK_SEMI_RECIPE_TO_SEMI_COMMENT_RECIPE_1" FOREIGN KEY (
	"RECIPE_NO"
)
REFERENCES "SEMI_RECIPE" (
	"RECIPE_NO"
);

ALTER TABLE "SEMI_COMMENT_RECIPE" ADD CONSTRAINT "FK_SEMI_MEMBER_TO_SEMI_COMMENT_RECIPE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "SEMI_MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "SEMI_COMMENT_RECIPE" ADD CONSTRAINT "FK_SEMI_COMMENT_RECIPE_TO_SEMI_COMMENT_RECIPE_1" FOREIGN KEY (
	"COMMENT_NO2"
)
REFERENCES "SEMI_COMMENT_RECIPE" (
	"COMMENT_NO_RECIPE"
);

ALTER TABLE "SEMI_RECIPE_LIKE" ADD CONSTRAINT "FK_SEMI_MEMBER_TO_SEMI_RECIPE_LIKE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "SEMI_MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "SEMI_RECIPE_LIKE" ADD CONSTRAINT "FK_SEMI_RECIPE_TO_SEMI_RECIPE_LIKE_1" FOREIGN KEY (
	"RECIPE_NO"
)
REFERENCES "SEMI_RECIPE" (
	"RECIPE_NO"
);





------ 테스트용 SQL 구문 ----------------------------------------------------

------ SEMI_MEMBER (FK 없음) ------
SELECT * FROM SEMI_MEMBER;

INSERT INTO SEMI_MEMBER
VALUES (SEQ_SEMI_MEMBER.NEXTVAL, 'user01@kh.or.kr', '암호화된 비밀번호', '홍길동', '유저일', '01011112222', NULL, NULL, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO SEMI_MEMBER
VALUES (SEQ_SEMI_MEMBER.NEXTVAL, 'user02@kh.or.kr', '암호화된 비밀번호', '홍홍홍', '유저이', '01022223333', NULL, NULL, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO SEMI_MEMBER
VALUES (SEQ_SEMI_MEMBER.NEXTVAL, 'user03@kh.or.kr', '암호화된 비밀번호', '길길길', '유저삼', '01012345678', NULL, NULL, DEFAULT, DEFAULT, DEFAULT);


UPDATE "SEMI_MEMBER"
SET MEMBER_PW = '$2a$10$Lzs1pn4JQOQ8CU04ctwEo.FzjjHpU/QhHsh6j0/08sYcQC/tk6CjG'
WHERE MEMBER_NO = 1;

UPDATE "SEMI_MEMBER"
SET MEMBER_PW = '$2a$10$TaMzJR38rIyiExkck3In9.zFt52peCTsR99v4argSszlV5Vqo8xDO'
WHERE MEMBER_NO = 2;

UPDATE "SEMI_MEMBER"
SET MEMBER_PW = '$2a$10$sQV78hOpCT3e9BZ8QCaDM.qocmSXHi9QLZQ5NP0H.1qMosKSVLR0e'
WHERE MEMBER_NO = 3;

SELECT * FROM SEMI_MEMBER;

COMMIT;

----- 일반게시판 종류 (FK 없음) -----
SELECT * FROM SEMI_BOARD_TYPE;

INSERT INTO SEMI_BOARD_TYPE
VALUES (SEQ_SEMI_BOARD_TYPE.NEXTVAL, '공지게시판');

INSERT INTO SEMI_BOARD_TYPE
VALUES (SEQ_SEMI_BOARD_TYPE.NEXTVAL, '자유게시판');

INSERT INTO SEMI_BOARD_TYPE
VALUES (SEQ_SEMI_BOARD_TYPE.NEXTVAL, '공동 구매 모집');

INSERT INTO SEMI_BOARD_TYPE
VALUES (SEQ_SEMI_BOARD_TYPE.NEXTVAL, '자취요리 레시피 공유');

SELECT * FROM SEMI_BOARD_TYPE;

COMMIT;


----- 일반게시글 (FK MEMBER_NO 8행, BOARD_CODE 9행) -----
SELECT * FROM SEMI_BOARD;

INSERT INTO SEMI_BOARD
VALUES(SEQ_SEMI_BOARD.NEXTVAL, '게시글 제목1', '게시글 내용1', DEFAULT, NULL, DEFAULT, DEFAULT, 1, 2);

INSERT INTO SEMI_BOARD
VALUES(SEQ_SEMI_BOARD.NEXTVAL, '게시글 제목2', '게시글 내용2', DEFAULT, NULL, DEFAULT, DEFAULT, 1, 1);

INSERT INTO SEMI_BOARD
VALUES(SEQ_SEMI_BOARD.NEXTVAL, '게시글 제목3', '게시글 내용3', DEFAULT, NULL, DEFAULT, DEFAULT, 2, 1);

INSERT INTO SEMI_BOARD
VALUES(SEQ_SEMI_BOARD.NEXTVAL, '게시글 제목4', '게시글 내용4', DEFAULT, NULL, DEFAULT, DEFAULT, 1, 3);

INSERT INTO SEMI_BOARD
VALUES(SEQ_SEMI_BOARD.NEXTVAL, '게시글 제목5', '게시글 내용5', DEFAULT, NULL, DEFAULT, DEFAULT, 1, 1);

INSERT INTO SEMI_BOARD
VALUES(SEQ_SEMI_BOARD.NEXTVAL, '게시글 제목6', '게시글 내용6', DEFAULT, NULL, DEFAULT, DEFAULT, 2, 2);

SELECT * FROM SEMI_BOARD;

COMMIT;

----- 게시글 이미지(일단 생략) -----

----- 게시글 좋아요(일단 생략) -----



------- 이하 레시피 관련-------------------------------------------------------------------------------------

----- 재료 특징 (FK 없음) -----
SELECT * FROM SEMI_INGREDIENT_SPEC;

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '당근', 10000, 2000);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '공기밥', 3000, 333);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '계란', 10000, 500);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '닭', 10000, 1500);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '돼지', 15000, 1100);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '소', 10000, 3000);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '간장', 40000, 100);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '설탕', 5000, 40);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '소금', 4000, 34);

INSERT INTO SEMI_INGREDIENT_SPEC
VALUES (SEQ_SEMI_INGREDIENT_SPEC.NEXTVAL, '라면', 4800, 960);

SELECT * FROM SEMI_INGREDIENT_SPEC;

COMMIT;


----- 레시피  -----
-- (레시피번호, 레시피이름, 레시피 꾸밈말, 레시피 내용, 레시피 작성일, 레시피 조회수, 레시피 삭제여부, 회원번호)
SELECT * FROM SEMI_RECIPE;

INSERT INTO SEMI_RECIPE
VALUES (SEQ_SEMI_RECIPE.NEXTVAL, '간장계란밥', '꾸밈말1', '간장계란밥 내용', DEFAULT, DEFAULT, DEFAULT, 1);

INSERT INTO SEMI_RECIPE
VALUES (SEQ_SEMI_RECIPE.NEXTVAL, '라면밥', '꾸밈말2', '라면밥 내용', DEFAULT, DEFAULT, DEFAULT, 1);

INSERT INTO SEMI_RECIPE
VALUES (SEQ_SEMI_RECIPE.NEXTVAL, '닭갈비 볶음밥', '꾸밈말3', '닭갈비 볶음밥 내용', DEFAULT, DEFAULT, DEFAULT, 2);

SELECT * FROM SEMI_RECIPE;

COMMIT;

----- 레시피별 요리재료 (--> 바꿔야 함) -----
--- (레시피명, 재료용량, 재료번호, 레시피번호)
SELECT * FROM SEMI_RECIPE_INGREDIENT;

------ 레시피 1번 닭갈비
INSERT INTO SEMI_RECIPE_INGREDIENT
VALUES (SEQ_SEMI_RECIPE_INGREDIENT.NEXTVAL, 3, 1, 1);

INSERT INTO SEMI_RECIPE_INGREDIENT
VALUES (SEQ_SEMI_RECIPE_INGREDIENT.NEXTVAL, 4, 2, 1);

INSERT INTO SEMI_RECIPE_INGREDIENT
VALUES (SEQ_SEMI_RECIPE_INGREDIENT.NEXTVAL, 2, 6, 1);

------ 레시피 2번 라면밥
INSERT INTO SEMI_RECIPE_INGREDIENT
VALUES (SEQ_SEMI_RECIPE_INGREDIENT.NEXTVAL, 1, 2, 2);

INSERT INTO SEMI_RECIPE_INGREDIENT
VALUES (SEQ_SEMI_RECIPE_INGREDIENT.NEXTVAL, 1, 3, 2);

INSERT INTO SEMI_RECIPE_INGREDIENT
VALUES (SEQ_SEMI_RECIPE_INGREDIENT.NEXTVAL, 1, 10, 2);

------ 레시피 3번 닭갈비볶음밥

SELECT * FROM SEMI_RECIPE_INGREDIENT;

COMMIT;

----- 레시피 게시판 이미지 (일단 생략) -----

----- 레시피 좋아요(일단 생략) -----






------ 이하 mapper.xml 에서 수행 하기전 테스트하는 용도 -------------------------------------------

----- 레시피별 요리재료 TABLE에서 RECIPE_NO = 1(RECIPE_NAME = 간장계란밥) 의 재료 및 필요용량, 가격 가져오기
SELECT sri.RECIPE_NO, sr.RECIPE_NAME "요리명",
	sis.INGREDIENT_NAME "재료명", sri.INGRED_VOLUME "필요용량", sis.PRICE_PER_INGRED "단위당 가격",
	sr.MEMBER_NO, sm.MEMBER_NAME "회원명"
FROM SEMI_RECIPE_INGREDIENT sri
JOIN SEMI_RECIPE sr ON(sr.RECIPE_NO = sri.RECIPE_NO)
JOIN SEMI_INGREDIENT_SPEC sis ON(sri.INGREDIENT_NO = sis.INGREDIENT_NO)
JOIN SEMI_MEMBER sm ON(sm.MEMBER_NO = sr.MEMBER_NO)
WHERE sr.RECIPE_NAME = '간장계란밥';

SELECT sri.RECIPE_NO, sr.RECIPE_NAME "요리명",
	sis.INGREDIENT_NAME "재료명", sri.INGRED_VOLUME "필요용량", sis.PRICE_PER_INGRED "단위당 가격",
	sr.MEMBER_NO, sm.MEMBER_NAME "회원명"
FROM SEMI_RECIPE_INGREDIENT sri
JOIN SEMI_RECIPE sr ON(sr.RECIPE_NO = sri.RECIPE_NO)
JOIN SEMI_INGREDIENT_SPEC sis ON(sri.INGREDIENT_NO = sis.INGREDIENT_NO)
JOIN SEMI_MEMBER sm ON(sm.MEMBER_NO = sr.MEMBER_NO)
WHERE sr.RECIPE_NAME = '라면밥';


DROP DATABASE IF EXISTS movie;
DROP USER IF EXISTS madang@localhost;

create user madang@localhost identified WITH mysql_native_password  by 'madang';
create database movie;
grant all privileges on movie.* to madang@localhost with grant option;
commit;

USE movie;

CREATE TABLE Movies (
  movie_id INTEGER NOT NULL,
  movie_name VARCHAR(30) NOT NULL,
  screen_time VARCHAR(10) NULL,
  screen_rank VARCHAR(10) NULL,
  director_name VARCHAR(30) NOT NULL,
  actor_name VARCHAR(30) NULL,
  jenre VARCHAR(10) NULL,
  introduction VARCHAR(50) NULL,
  opendate DATE NULL,
  PRIMARY KEY (movie_id)
);
# 영화번호, 영화명, 상영시간, 상영등급, 감독명, 배우명, 장르, 영화소개 및 개봉일 정보를 저장한다. 모든 영화는 1개 이상에서 4개 이하의 상영일정을 가진다.

CREATE TABLE Customers (
  cust_id INTEGER NOT NULL,
  cust_name VARCHAR(30) NULL,
  phone VARCHAR(30) NULL,
  email VARCHAR(30) NULL,
  PRIMARY KEY (cust_id)
);
# 회원아이디, 고객명, 휴대폰번호 및 전자메일주소를 저장

CREATE TABLE Rooms (
  room_id INTEGER NOT NULL,
  seat_count INTEGER NULL,
  available VARCHAR(1) NULL,
  PRIMARY KEY (room_id)
);
# 상영관번호, 좌석수 및 상영관사용여부를 저장한다. 각 상영관에는 1개 이상의 상영일정을 배정

CREATE TABLE Seats (
  seat_id INTEGER NOT NULL,
  room_id INTEGER NOT NULL,
  available VARCHAR(1) NULL,
  PRIMARY KEY (seat_id, room_id),
  FOREIGN KEY (room_id) REFERENCES Rooms(room_id)
);
# 좌석번호, 상영관번호 및 좌석사용여부를 저장


CREATE TABLE Reservations (
  reservation_id INTEGER NOT NULL,
  payment_way VARCHAR(30) NULL,
  payment_state VARCHAR(1) NULL,
  payment_price INTEGER NULL,
  cust_id INTEGER NOT NULL,
  payment_date DATE NULL,
  PRIMARY KEY (reservation_id),
  FOREIGN KEY (cust_id) REFERENCES Customers(cust_id)
);
# 예매번호, 결제방법, 결제상태, 결제금액, 회원아이디 및  결제일자를 저장

CREATE TABLE Schedules (
  schedule_id INTEGER NOT NULL,
  movie_id INTEGER NOT NULL,
  room_id INTEGER NOT NULL,
  screendate DATE NULL,
  screenday VARCHAR(30) NULL,
  screencount VARCHAR(30) NULL,
  screentime VARCHAR(30) NULL,
  PRIMARY KEY (schedule_id),
  FOREIGN KEY (movie_id) REFERENCES Movies(movie_id),
  FOREIGN KEY (room_id) REFERENCES Rooms(room_id)
);
# 상영일정번호, 영화번호, 상영관번호, 상영시작일, 상영요일, 상영회차 및 상영시작시간 정보를 저장
 
CREATE TABLE Tickets (
  ticket_id INTEGER NOT NULL,
  schedule_id INTEGER NOT NULL,
  room_id INTEGER NOT NULL,
  seat_id INTEGER NOT NULL,
  reservation_id INTEGER NOT NULL,
  available VARCHAR(1) NULL,
  base_price INTEGER NULL,
  sale_price INTEGER NULL,
  PRIMARY KEY (ticket_id),
  FOREIGN KEY (schedule_id) REFERENCES Schedules(schedule_id),
  FOREIGN KEY (room_id) REFERENCES Schedules(room_id),
  FOREIGN KEY (seat_id) REFERENCES Seats(seat_id),
  FOREIGN KEY (reservation_id) REFERENCES Reservations(reservation_id)
);
# 티켓번호, 상영일정번호, 상영관번호, 좌석번호, 예매번호, 발권여부, 표준가격 및 판매가격 정보를 저장한다. 각 티켓은 1개의 좌석과 연결

INSERT INTO Movies VALUES(1, '명량', '128분', '15세', '김한민', '최민식', '액션', '임진왜란 이순신 장군의 명량대첩', STR_TO_DATE('2021-01-01','%Y-%m-%d'));
INSERT INTO Movies VALUES(2, '7번방의 선물', '127분', '15세', '이환경', '류승룡', '코미디', '교도소에서의 감동실화', STR_TO_DATE('2021-02-23','%Y-%m-%d'));
INSERT INTO Movies VALUES(3, '모가디슈', '121분', '15세', '류승완', '김윤석', '액션', '소말리아 내전 당시 대한민국과 북한 대사관들의 이야기', STR_TO_DATE('2021-03-17','%Y-%m-%d'));
INSERT INTO Movies VALUES(4, '기적', '117분', '12세', '이장훈', '박정민', '드라마', '시골에 기차역을 만드는 기적', STR_TO_DATE('2021-04-07','%Y-%m-%d'));
INSERT INTO Movies VALUES(5, '브로커', '129분', '12세', '고레에다히로카즈', '송강호', '드라마', '아이브로커들과 아이엄마의 여정', STR_TO_DATE('2021-05-15','%Y-%m-%d'));
INSERT INTO Movies VALUES(6, '세시봉', '122분', '15세', '김현석', '정우', '로맨스', '가수 세시봉의 이야기', STR_TO_DATE('2021-07-19','%Y-%m-%d'));
INSERT INTO Movies VALUES(7, '1987', '129분', '15세', '장준환', '김윤석', '드라마', '1987년에 고위관직들의 비리를 담은 영화', STR_TO_DATE('2021-08-14','%Y-%m-%d'));
INSERT INTO Movies VALUES(8, '베테랑', '123분', '15세', '류승완', '황정민', '액션', '베테랑 형사와 재벌의 대결', STR_TO_DATE('2021-09-23','%Y-%m-%d'));
INSERT INTO Movies VALUES(9, '신과함께-죄와벌', '139분', '12세', '김용화', '하정우', '판타지', '사후세계의 이야기', STR_TO_DATE('2021-10-30','%Y-%m-%d'));
INSERT INTO Movies VALUES(10, '부산행', '118분', '15세', '연상호', '공유', '액션', '부산으로 향하던 ktx안에서 벌어지는 좀비이야기', STR_TO_DATE('2021-12-27','%Y-%m-%d'));

# 영화번호, 영화명, 상영시간, 상영등급, 감독명, 배우명, 장르, 영화소개 및 개봉일 정보를 저장한다. 모든 영화는 1개 이상에서 4개 이하의 상영일정을 가진다.



INSERT INTO Customers VALUES(17011517, '함기봉', '010-2553-0657', 'kb721a@naver.com');
INSERT INTO Customers VALUES(20010459, '신예린', '010-4167-9615', 'nir2y@naver.com');
INSERT INTO Customers VALUES(18123456, '남주혁', '010-1234-2222', 'jhnam@naver.com');
INSERT INTO Customers VALUES(20142536, '김우빈', '010-1965-4751', 'wbk12@naver.com');
INSERT INTO Customers VALUES(22745896, '김태리', '010-4251-7458', 'tari21@naver.com');
INSERT INTO Customers VALUES(19217526, '이성경', '010-7415-4123', 'heybiblee@naver.com');
INSERT INTO Customers VALUES(19001458, '김지원', '010-4777-1251', 'geewonii@naver.com');
INSERT INTO Customers VALUES(19475125, '손석구', '010-4426-9652', '9kuku@naver.com');
INSERT INTO Customers VALUES(22145236, '이효리', '010-2263-3851', 'leehyoree@naver.com');
INSERT INTO Customers VALUES(16105236, '유재석', '010-7451-1425', 'useok@naver.com');



INSERT INTO Rooms VALUES(1, 10, 'N');
INSERT INTO Rooms VALUES(2, 10, 'N');
INSERT INTO Rooms VALUES(3, 15, 'N');
INSERT INTO Rooms VALUES(4, 15, 'N');
INSERT INTO Rooms VALUES(5, 15, 'N');
INSERT INTO Rooms VALUES(6, 15, 'N');
INSERT INTO Rooms VALUES(7, 20, 'N');
INSERT INTO Rooms VALUES(8, 20, 'N');
INSERT INTO Rooms VALUES(9, 20, 'N');
INSERT INTO Rooms VALUES(10, 20, 'N');


INSERT INTO Seats VALUES(1, 1, 'N');
INSERT INTO Seats VALUES(2, 1, 'N');
INSERT INTO Seats VALUES(3, 1, 'Y');
INSERT INTO Seats VALUES(4, 1, 'Y');
INSERT INTO Seats VALUES(5, 1, 'Y');
INSERT INTO Seats VALUES(6, 1, 'Y');
INSERT INTO Seats VALUES(7, 1, 'Y');
INSERT INTO Seats VALUES(8, 1, 'Y');
INSERT INTO Seats VALUES(9, 1, 'Y');
INSERT INTO Seats VALUES(10, 1, 'Y');


INSERT INTO Seats VALUES(1, 2, 'Y');
INSERT INTO Seats VALUES(2, 2, 'Y');
INSERT INTO Seats VALUES(3, 2, 'Y');
INSERT INTO Seats VALUES(4, 2, 'Y');
INSERT INTO Seats VALUES(5, 2, 'Y');
INSERT INTO Seats VALUES(6, 2, 'Y');
INSERT INTO Seats VALUES(7, 2, 'Y');
INSERT INTO Seats VALUES(8, 2, 'Y');
INSERT INTO Seats VALUES(9, 2, 'N');
INSERT INTO Seats VALUES(10, 2, 'Y');



INSERT INTO Seats VALUES(1, 3, 'Y');
INSERT INTO Seats VALUES(2, 3, 'Y');
INSERT INTO Seats VALUES(3, 3, 'N');
INSERT INTO Seats VALUES(4, 3, 'Y');
INSERT INTO Seats VALUES(5, 3, 'Y');
INSERT INTO Seats VALUES(6, 3, 'Y');
INSERT INTO Seats VALUES(7, 3, 'Y');
INSERT INTO Seats VALUES(8, 3, 'Y');
INSERT INTO Seats VALUES(9, 3, 'Y');
INSERT INTO Seats VALUES(10, 3, 'N');
INSERT INTO Seats VALUES(11, 3, 'Y');
INSERT INTO Seats VALUES(12, 3, 'Y');
INSERT INTO Seats VALUES(13, 3, 'Y');
INSERT INTO Seats VALUES(14, 3, 'Y');
INSERT INTO Seats VALUES(15, 3, 'Y');



INSERT INTO Seats VALUES(1, 4, 'Y');
INSERT INTO Seats VALUES(2, 4, 'Y');
INSERT INTO Seats VALUES(3, 4, 'Y');
INSERT INTO Seats VALUES(4, 4, 'Y');
INSERT INTO Seats VALUES(5, 4, 'Y');
INSERT INTO Seats VALUES(6, 4, 'Y');
INSERT INTO Seats VALUES(7, 4, 'Y');
INSERT INTO Seats VALUES(8, 4, 'Y');
INSERT INTO Seats VALUES(9, 4, 'Y');
INSERT INTO Seats VALUES(10, 4, 'Y');
INSERT INTO Seats VALUES(11,4 , 'Y');
INSERT INTO Seats VALUES(12, 4, 'Y');
INSERT INTO Seats VALUES(13, 4, 'Y');
INSERT INTO Seats VALUES(14, 4, 'Y');
INSERT INTO Seats VALUES(15, 4, 'N');


INSERT INTO Seats VALUES(1, 5, 'Y');
INSERT INTO Seats VALUES(2, 5, 'Y');
INSERT INTO Seats VALUES(3, 5, 'Y');
INSERT INTO Seats VALUES(4, 5, 'Y');
INSERT INTO Seats VALUES(5, 5, 'Y');
INSERT INTO Seats VALUES(6, 5, 'Y');
INSERT INTO Seats VALUES(7, 5, 'Y');
INSERT INTO Seats VALUES(8, 5, 'Y');
INSERT INTO Seats VALUES(9, 5, 'Y');
INSERT INTO Seats VALUES(10, 5, 'Y');
INSERT INTO Seats VALUES(11, 5, 'Y');
INSERT INTO Seats VALUES(12, 5, 'Y');
INSERT INTO Seats VALUES(13, 5, 'Y');
INSERT INTO Seats VALUES(14, 5, 'Y');
INSERT INTO Seats VALUES(15, 5, 'N');


INSERT INTO Seats VALUES(1, 6, 'Y');
INSERT INTO Seats VALUES(2, 6, 'Y');
INSERT INTO Seats VALUES(3, 6, 'Y');
INSERT INTO Seats VALUES(4, 6, 'Y');
INSERT INTO Seats VALUES(5, 6, 'Y');
INSERT INTO Seats VALUES(6, 6, 'Y');
INSERT INTO Seats VALUES(7, 6, 'Y');
INSERT INTO Seats VALUES(8, 6, 'Y');
INSERT INTO Seats VALUES(9, 6, 'Y');
INSERT INTO Seats VALUES(10, 6, 'Y');
INSERT INTO Seats VALUES(11, 6, 'Y');
INSERT INTO Seats VALUES(12, 6, 'Y');
INSERT INTO Seats VALUES(13, 6, 'Y');
INSERT INTO Seats VALUES(14, 6, 'Y');
INSERT INTO Seats VALUES(15, 6, 'N');


INSERT INTO Seats VALUES(1, 7, 'Y');
INSERT INTO Seats VALUES(2, 7, 'Y');
INSERT INTO Seats VALUES(3, 7, 'Y');
INSERT INTO Seats VALUES(4, 7, 'Y');
INSERT INTO Seats VALUES(5, 7, 'Y');
INSERT INTO Seats VALUES(6, 7, 'Y');
INSERT INTO Seats VALUES(7, 7, 'Y');
INSERT INTO Seats VALUES(8, 7, 'Y');
INSERT INTO Seats VALUES(9, 7, 'Y');
INSERT INTO Seats VALUES(10, 7, 'Y');
INSERT INTO Seats VALUES(11, 7, 'Y');
INSERT INTO Seats VALUES(12, 7, 'Y');
INSERT INTO Seats VALUES(13, 7, 'Y');
INSERT INTO Seats VALUES(14, 7, 'N');
INSERT INTO Seats VALUES(15, 7, 'Y');
INSERT INTO Seats VALUES(16, 7, 'Y');
INSERT INTO Seats VALUES(17, 7, 'Y');
INSERT INTO Seats VALUES(18, 7, 'Y');
INSERT INTO Seats VALUES(19, 7, 'Y');
INSERT INTO Seats VALUES(20, 7, 'Y');



INSERT INTO Seats VALUES(1, 8, 'N');
INSERT INTO Seats VALUES(2, 8, 'Y');
INSERT INTO Seats VALUES(3, 8, 'Y');
INSERT INTO Seats VALUES(4, 8, 'Y');
INSERT INTO Seats VALUES(5, 8, 'Y');
INSERT INTO Seats VALUES(6, 8, 'Y');
INSERT INTO Seats VALUES(7, 8, 'Y');
INSERT INTO Seats VALUES(8, 8, 'Y');
INSERT INTO Seats VALUES(9, 8, 'Y');
INSERT INTO Seats VALUES(10, 8, 'Y');
INSERT INTO Seats VALUES(11, 8, 'Y');
INSERT INTO Seats VALUES(12, 8, 'Y');
INSERT INTO Seats VALUES(13, 8, 'Y');
INSERT INTO Seats VALUES(14, 8, 'Y');
INSERT INTO Seats VALUES(15, 8, 'Y');
INSERT INTO Seats VALUES(16, 8, 'Y');
INSERT INTO Seats VALUES(17, 8, 'Y');
INSERT INTO Seats VALUES(18, 8, 'Y');
INSERT INTO Seats VALUES(19, 8, 'N');
INSERT INTO Seats VALUES(20, 8, 'Y');


INSERT INTO Seats VALUES(1, 9, 'Y');
INSERT INTO Seats VALUES(2, 9, 'Y');
INSERT INTO Seats VALUES(3, 9, 'Y');
INSERT INTO Seats VALUES(4, 9, 'Y');
INSERT INTO Seats VALUES(5, 9, 'Y');
INSERT INTO Seats VALUES(6, 9, 'Y');
INSERT INTO Seats VALUES(7, 9, 'Y');
INSERT INTO Seats VALUES(8, 9, 'Y');
INSERT INTO Seats VALUES(9, 9, 'Y');
INSERT INTO Seats VALUES(10, 9, 'Y');
INSERT INTO Seats VALUES(11, 9, 'Y');
INSERT INTO Seats VALUES(12, 9, 'Y');
INSERT INTO Seats VALUES(13, 9, 'Y');
INSERT INTO Seats VALUES(14, 9, 'Y');
INSERT INTO Seats VALUES(15, 9, 'Y');
INSERT INTO Seats VALUES(16, 9, 'Y');
INSERT INTO Seats VALUES(17, 9, 'Y');
INSERT INTO Seats VALUES(18, 9, 'Y');
INSERT INTO Seats VALUES(19, 9, 'Y');
INSERT INTO Seats VALUES(20, 9, 'Y');


INSERT INTO Seats VALUES(1, 10, 'Y');
INSERT INTO Seats VALUES(2, 10, 'Y');
INSERT INTO Seats VALUES(3, 10, 'Y');
INSERT INTO Seats VALUES(4, 10, 'Y');
INSERT INTO Seats VALUES(5, 10, 'Y');
INSERT INTO Seats VALUES(6, 10, 'Y');
INSERT INTO Seats VALUES(7, 10, 'Y');
INSERT INTO Seats VALUES(8, 10, 'Y');
INSERT INTO Seats VALUES(9, 10, 'Y');
INSERT INTO Seats VALUES(10, 10, 'Y');
INSERT INTO Seats VALUES(11, 10, 'Y');
INSERT INTO Seats VALUES(12, 10, 'Y');
INSERT INTO Seats VALUES(13, 10, 'Y');
INSERT INTO Seats VALUES(14, 10, 'Y');
INSERT INTO Seats VALUES(15, 10, 'Y');
INSERT INTO Seats VALUES(16, 10, 'Y');
INSERT INTO Seats VALUES(17, 10, 'Y');
INSERT INTO Seats VALUES(18, 10, 'Y');
INSERT INTO Seats VALUES(19, 10, 'Y');
INSERT INTO Seats VALUES(20, 10, 'Y');


INSERT INTO Reservations VALUES(1, '현금', 'Y', 14000, 17011517, STR_TO_DATE('2021-01-01','%Y-%m-%d'));
INSERT INTO Reservations VALUES(2, '카드', 'Y', 14000, 17011517, STR_TO_DATE('2021-07-20','%Y-%m-%d'));
INSERT INTO Reservations VALUES(3, '현금', 'Y', 12000, 18123456, STR_TO_DATE('2021-04-11','%Y-%m-%d'));
INSERT INTO Reservations VALUES(4, '카드', 'Y', 13000, 19001458, STR_TO_DATE('2021-05-22','%Y-%m-%d'));
INSERT INTO Reservations VALUES(5, '카드', 'Y', 13000, 20010459, STR_TO_DATE('2021-08-27','%Y-%m-%d'));
INSERT INTO Reservations VALUES(6, '카드', 'Y', 12000, 19001458, STR_TO_DATE('2021-10-31','%Y-%m-%d'));
INSERT INTO Reservations VALUES(7, '카드', 'N', 10000, 22745896, STR_TO_DATE('2021-09-23','%Y-%m-%d'));
INSERT INTO Reservations VALUES(8, '카드', 'N', 12000, 19001458, STR_TO_DATE('2021-12-30','%Y-%m-%d'));
INSERT INTO Reservations VALUES(9, '카드', 'N', 12000, 22745896, STR_TO_DATE('2021-02-24','%Y-%m-%d'));
INSERT INTO Reservations VALUES(10, '카드', 'N', 12000, 19001458, STR_TO_DATE('2021-03-17','%Y-%m-%d'));
# 예매번호, 결제방법, 결제상태, 결제금액, 회원아이디 및  결제일자를 저장



INSERT INTO Schedules VALUES(1, 1, 1, STR_TO_DATE('2021-01-01','%Y-%m-%d'), '금요일', 1, '10:00AM');
INSERT INTO Schedules VALUES(2, 6, 3, STR_TO_DATE('2021-07-19','%Y-%m-%d'), '월요일', 1, '12:00AM');
INSERT INTO Schedules VALUES(3, 4, 5, STR_TO_DATE('2021-04-07','%Y-%m-%d'), '수요일', 1, '11:30AM');
INSERT INTO Schedules VALUES(4, 5, 7, STR_TO_DATE('2021-05-15','%Y-%m-%d'), '토요일', 1, '09:00AM');
INSERT INTO Schedules VALUES(5, 7, 2, STR_TO_DATE('2021-08-14','%Y-%m-%d'), '토요일', 1, '11:00PM');
INSERT INTO Schedules VALUES(6, 9, 4, STR_TO_DATE('2021-10-30','%Y-%m-%d'), '토요일', 1, '03:00PM');
INSERT INTO Schedules VALUES(7, 8, 6, STR_TO_DATE('2021-09-23','%Y-%m-%d'), '목요일', 1, '07:00PM');
INSERT INTO Schedules VALUES(8, 10, 8, STR_TO_DATE('2021-12-27','%Y-%m-%d'), '월요일', 1, '09:00AM');
INSERT INTO Schedules VALUES(9, 2, 1, STR_TO_DATE('2021-02-23','%Y-%m-%d'), '화요일', 1, '10:00PM');
INSERT INTO Schedules VALUES(10, 5, 3, STR_TO_DATE('2021-05-15','%Y-%m-%d'), '토요일', 2, '06:00PM');
INSERT INTO Schedules VALUES(11, 1, 4, STR_TO_DATE('2021-06-09','%Y-%m-%d'), '수요일', 2, '10:00PM');
INSERT INTO Schedules VALUES(12, 2, 5, STR_TO_DATE('2021-06-16','%Y-%m-%d'), '수요일', 2, '09:00AM');
INSERT INTO Schedules VALUES(13, 3, 1, STR_TO_DATE('2021-06-23','%Y-%m-%d'), '수요일', 2, '07:00PM');
INSERT INTO Schedules VALUES(14, 4, 2, STR_TO_DATE('2021-07-30','%Y-%m-%d'), '수요일', 2, '03:00PM');
INSERT INTO Schedules VALUES(15, 5, 9, STR_TO_DATE('2021-08-06','%Y-%m-%d'), '수요일', 2, '02:00PM');
INSERT INTO Schedules VALUES(16, 6, 8, STR_TO_DATE('2021-09-13','%Y-%m-%d'), '수요일', 2, '01:00PM');
INSERT INTO Schedules VALUES(17, 7, 7, STR_TO_DATE('2021-08-14','%Y-%m-%d'), '목요일', 2, '04:00PM');
INSERT INTO Schedules VALUES(18, 8, 6, STR_TO_DATE('2021-07-17','%Y-%m-%d'), '수요일', 2, '06:00PM');
INSERT INTO Schedules VALUES(19, 9, 5, STR_TO_DATE('2021-06-27','%Y-%m-%d'), '화요일', 2, '11:00PM');
INSERT INTO Schedules VALUES(20, 10, 4, STR_TO_DATE('2021-11-17','%Y-%m-%d'), '금요일', 2, '11:00AM');

# 상영일정번호, 영화번호, 상영관번호, 상영시작일, 상영요일, 상영회차 및 상영시작시간 정보를 저장



INSERT INTO Tickets VALUES(1, 1, 1, 1, 1, 'Y', 14000, 14000);
INSERT INTO Tickets VALUES(2, 2, 3, 10, 2, 'Y', 14000, 14000);
INSERT INTO Tickets VALUES(3, 3, 5, 15, 3, 'Y', 14000, 12000);
INSERT INTO Tickets VALUES(4, 4, 7, 14, 4, 'Y', 15000, 13000);
INSERT INTO Tickets VALUES(5, 5, 2, 9, 5, 'Y', 15000, 13000);
INSERT INTO Tickets VALUES(6, 6, 4, 15, 6, 'Y', 14000, 12000);
INSERT INTO Tickets VALUES(7, 7, 6, 20, 7, 'N', 10000, 10000);
INSERT INTO Tickets VALUES(8, 8, 8, 19, 8, 'N', 14000, 10000);
INSERT INTO Tickets VALUES(9, 9, 1, 2, 9, 'N', 14000, 12000);
INSERT INTO Tickets VALUES(10,10, 3, 3, 10, 'N', 14000, 12000);


# 티켓번호, 상영일정번호, 상영관번호, 좌석번호, 예매번호, 발권여부, 표준가격 및 판매가격 정보를 저장한다. 각 티켓은 1개의 좌석과 연결






package teamproject;

import java.sql.*;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;



class DBManager extends JFrame {
	private ResultSet rs;
	private Statement stmt;
	private Connection connection;
	private PreparedStatement ps;
	private String now;
	private int base_price;
	
	public DBManager() {
		connection = teamproject.loadDB();
		now = "2021-06-01"; //현재 시각
		base_price = 14000; //표준 금액
		try {
			stmt = connection.createStatement();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/*관리자*/
	/*초기화*/
	public void initTable() {
		try {
			stmt.executeUpdate("DROP TABLE if exists Tickets;"); 
			stmt.executeUpdate("DROP TABLE if exists Schedules;"); 
			stmt.executeUpdate("DROP TABLE if exists Schedules;");		 
			stmt.executeUpdate("DROP TABLE if exists Seats;");
			stmt.executeUpdate("DROP TABLE if exists Rooms;");
			stmt.executeUpdate("DROP TABLE if exists Reservations;");
			stmt.executeUpdate("DROP TABLE if exists Customers;");
			stmt.executeUpdate("DROP TABLE if exists Movies;");
			
			stmt.executeUpdate("CREATE TABLE Movies (\r\n" + 
					"  movie_id INTEGER NOT NULL,\r\n" + 
					"  movie_name VARCHAR(30) NOT NULL,\r\n" + 
					"  screen_time VARCHAR(10) NULL,\r\n" + 
					"  screen_rank VARCHAR(10) NULL,\r\n" + 
					"  director_name VARCHAR(30) NOT NULL,\r\n" + 
					"  actor_name VARCHAR(30) NULL,\r\n" + 
					"  jenre VARCHAR(10) NULL,\r\n" + 
					"  introduction VARCHAR(50) NULL,\r\n" + 
					"  opendate DATE NULL,\r\n" + 
					"  PRIMARY KEY (movie_id)\r\n" + 
					");");
			
			stmt.executeUpdate("CREATE TABLE Customers (\r\n" + 
					"  cust_id INTEGER NOT NULL,\r\n" + 
					"  cust_name VARCHAR(30) NULL,\r\n" + 
					"  phone VARCHAR(30) NULL,\r\n" + 
					"  email VARCHAR(30) NULL,\r\n" + 
					"  PRIMARY KEY (cust_id)\r\n" + 
					");");
			
			stmt.executeUpdate("CREATE TABLE Rooms (\r\n" + 
					"  room_id INTEGER NOT NULL,\r\n" + 
					"  seat_count INTEGER NULL,\r\n" + 
					"  available VARCHAR(1) NULL,\r\n" + 
					"  PRIMARY KEY (room_id)\r\n" + 
					");");
			
			stmt.executeUpdate("CREATE TABLE Seats (\r\n" + 
					"  seat_id INTEGER NOT NULL,\r\n" + 
					"  room_id INTEGER NOT NULL,\r\n" + 
					"  available VARCHAR(1) NULL,\r\n" + 
					"  PRIMARY KEY (seat_id, room_id),\r\n" + 
					"  FOREIGN KEY (room_id) REFERENCES Rooms(room_id)\r\n" + 
					");");
			
	
			stmt.executeUpdate("CREATE TABLE Reservations (\r\n" + 
					"  reservation_id INTEGER NOT NULL,\r\n" + 
					"  payment_way VARCHAR(30) NULL,\r\n" + 
					"  payment_state VARCHAR(1) NULL,\r\n" + 
					"  payment_price INTEGER NULL,\r\n" + 
					"  cust_id INTEGER NOT NULL,\r\n" + 
					"  payment_date DATE NULL,\r\n" + 
					"  PRIMARY KEY (reservation_id),\r\n" + 
					"  FOREIGN KEY (cust_id) REFERENCES Customers(cust_id)\r\n" + 
					");");
			
			stmt.executeUpdate("CREATE TABLE Schedules (\r\n" + 
					"  schedule_id INTEGER NOT NULL,\r\n" + 
					"  movie_id INTEGER NOT NULL,\r\n" + 
					"  room_id INTEGER NOT NULL,\r\n" + 
					"  screendate DATE NULL,\r\n" + 
					"  screenday VARCHAR(30) NULL,\r\n" + 
					"  screencount VARCHAR(30) NULL,\r\n" + 
					"  screentime VARCHAR(30) NULL,\r\n" + 
					"  PRIMARY KEY (schedule_id),\r\n" + 
					"  FOREIGN KEY (movie_id) REFERENCES Movies(movie_id),\r\n" + 
					"  FOREIGN KEY (room_id) REFERENCES Rooms(room_id)\r\n" + 
					");");
			
			
			stmt.executeUpdate("CREATE TABLE Tickets (\r\n" + 
					"  ticket_id INTEGER NOT NULL,\r\n" + 
					"  schedule_id INTEGER NOT NULL,\r\n" + 
					"  room_id INTEGER NOT NULL,\r\n" + 
					"  seat_id INTEGER NOT NULL,\r\n" + 
					"  reservation_id INTEGER NOT NULL,\r\n" + 
					"  available VARCHAR(1) NULL,\r\n" + 
					"  base_price INTEGER NULL,\r\n" + 
					"  sale_price INTEGER NULL,\r\n" + 
					"  PRIMARY KEY (ticket_id),\r\n" + 
					"  FOREIGN KEY (schedule_id) REFERENCES Schedules(schedule_id),\r\n" + 
					"  FOREIGN KEY (room_id) REFERENCES Schedules(room_id),\r\n" + 
					"  FOREIGN KEY (seat_id) REFERENCES Seats(seat_id),\r\n" + 
					"  FOREIGN KEY (reservation_id) REFERENCES Reservations(reservation_id)\r\n" + 
					");");
			
			stmt.executeUpdate("INSERT INTO Movies VALUES(1, '명량', '128분', '15세', '김한민', '최민식', '액션', '임진왜란 이순신 장군의 명량대첩', STR_TO_DATE('2021-01-01','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(2, '7번방의 선물', '127분', '15세', '이환경', '류승룡', '코미디', '교도소에서의 감동실화', STR_TO_DATE('2021-02-23','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(3, '모가디슈', '121분', '15세', '류승완', '김윤석', '액션', '소말리아 내전 당시 대한민국과 북한 대사관들의 이야기', STR_TO_DATE('2021-03-17','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(4, '기적', '117분', '12세', '이장훈', '박정민', '드라마', '시골에 기차역을 만드는 기적', STR_TO_DATE('2021-04-07','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(5, '브로커', '129분', '12세', '고레에다히로카즈', '송강호', '드라마', '아이브로커들과 아이엄마의 여정', STR_TO_DATE('2021-05-15','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(6, '세시봉', '122분', '15세', '김현석', '정우', '로맨스', '가수 세시봉의 이야기', STR_TO_DATE('2021-07-19','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(7, '1987', '129분', '15세', '장준환', '김윤석', '드라마', '1987년에 고위관직들의 비리를 담은 영화', STR_TO_DATE('2021-08-14','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(8, '베테랑', '123분', '15세', '류승완', '황정민', '액션', '베테랑 형사와 재벌의 대결', STR_TO_DATE('2021-09-23','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(9, '신과함께-죄와벌', '139분', '12세', '김용화', '하정우', '판타지', '사후세계의 이야기', STR_TO_DATE('2021-10-30','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Movies VALUES(10, '부산행', '118분', '15세', '연상호', '공유', '액션', '부산으로 향하던 ktx안에서 벌어지는 좀비이야기', STR_TO_DATE('2021-12-27','%Y-%m-%d'));");
	      
	        stmt.executeUpdate("INSERT INTO Customers VALUES(17011517, '함기봉', '010-2553-0657', 'kb721a@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(20010459, '신예린', '010-4167-9615', 'nir2y@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(18123456, '남주혁', '010-1234-2222', 'jhnam@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(20142536, '김우빈', '010-1965-4751', 'wbk12@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(22745896, '김태리', '010-4251-7458', 'tari21@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(19217526, '이성경', '010-7415-4123', 'heybiblee@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(19001458, '김지원', '010-4777-1251', 'geewonii@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(19475125, '손석구', '010-4426-9652', '9kuku@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(22145236, '이효리', '010-2263-3851', 'leehyoree@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(16105236, '유재석', '010-7451-1425', 'useok@naver.com');");
	        stmt.executeUpdate("INSERT INTO Customers VALUES(12345678, '홍길동', '010-1234-5678', 'gildong@naver.com');");
			
			
			
			stmt.executeUpdate("INSERT INTO Rooms VALUES(1, 10, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(2, 10, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(3, 15, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(4, 15, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(5, 15, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(6, 15, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(7, 20, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(8, 20, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(9, 20, 'N');");
			stmt.executeUpdate("INSERT INTO Rooms VALUES(10, 20, 'N');");

			

			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 1, 'N');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 1, 'N');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 1, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 1, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 1, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 1, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 1, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 1, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 1, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 1, 'Y');");

			
			
			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 2, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 2, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 2, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 2, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 2, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 2, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 2, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 2, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 2, 'N');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 2, 'Y');");

			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 3, 'N');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 3, 'N');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(11, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(12, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(13, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(14, 3, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(15, 3, 'Y');");

			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(11,4 , 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(12, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(13, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(14, 4, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(15, 4, 'N');");


			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(11, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(12, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(13, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(14, 5, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(15, 5, 'N');");


			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(11, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(12, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(13, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(14, 6, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(15, 6, 'N');");


			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(11, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(12, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(13, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(14, 7, 'N');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(15, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(16, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(17, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(18, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(19, 7, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(20, 7, 'Y');");

			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 8, 'N');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(11, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(12, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(13, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(14, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(15, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(16, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(17, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(18, 8, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(19, 8, 'N');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(20, 8, 'Y');");


			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(11, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(12, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(13, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(14, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(15, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(16, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(17, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(18, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(19, 9, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(20, 9, 'Y');");


			stmt.executeUpdate("INSERT INTO Seats VALUES(1, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(2, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(3, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(4, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(5, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(6, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(7, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(8, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(9, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(10, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(11, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(12, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(13, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(14, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(15, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(16, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(17, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(18, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(19, 10, 'Y');");
			stmt.executeUpdate("INSERT INTO Seats VALUES(20, 10, 'Y');");
			
			stmt.executeUpdate("INSERT INTO Reservations VALUES(1, '현금', 'Y', 14000, 17011517, STR_TO_DATE('2021-01-01','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(2, '카드', 'Y', 14000, 17011517, STR_TO_DATE('2021-07-20','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(3, '현금', 'Y', 12000, 18123456, STR_TO_DATE('2021-04-11','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(4, '카드', 'Y', 14000, 19001458, STR_TO_DATE('2021-05-22','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(5,'카드', 'Y', 14000, 20010459, STR_TO_DATE('2021-08-27','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(6,'카드', 'Y', 12000, 19001458, STR_TO_DATE('2021-10-31','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(7,'카드', 'N', 14000, 22745896, STR_TO_DATE('2021-09-23','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(8,'카드', 'N', 12000, 19001458, STR_TO_DATE('2021-12-30','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(9,'카드', 'N', 12000, 22745896, STR_TO_DATE('2021-02-24','%Y-%m-%d'));");
	        stmt.executeUpdate("INSERT INTO Reservations VALUES(10,'카드', 'N', 12000, 19001458, STR_TO_DATE('2021-03-17','%Y-%m-%d'));");
			
			
			stmt.executeUpdate("INSERT INTO Schedules VALUES(1, 1, 1, STR_TO_DATE('2021-01-01','%Y-%m-%d'), '금요일', 1, '10:00AM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(2, 6, 3, STR_TO_DATE('2021-07-19','%Y-%m-%d'), '월요일', 1, '12:00AM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(3, 4, 5, STR_TO_DATE('2021-04-07','%Y-%m-%d'), '수요일', 1, '11:30AM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(4, 5, 7, STR_TO_DATE('2021-05-15','%Y-%m-%d'), '토요일', 1, '09:00AM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(5, 7, 2, STR_TO_DATE('2021-08-14','%Y-%m-%d'), '토요일', 1, '11:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(6, 9, 4, STR_TO_DATE('2021-10-30','%Y-%m-%d'), '토요일', 1, '03:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(7, 8, 6, STR_TO_DATE('2021-09-23','%Y-%m-%d'), '목요일', 1, '07:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(8, 10, 8, STR_TO_DATE('2021-12-27','%Y-%m-%d'), '월요일', 1, '09:00AM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(9, 2, 1, STR_TO_DATE('2021-02-23','%Y-%m-%d'), '화요일', 1, '10:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(10, 3, 3, STR_TO_DATE('2021-06-02','%Y-%m-%d'), '수요일', 1, '08:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(11, 1, 4, STR_TO_DATE('2021-06-09','%Y-%m-%d'), '수요일', 2, '10:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(12, 2, 5, STR_TO_DATE('2021-06-16','%Y-%m-%d'), '수요일', 2, '09:00AM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(13, 3, 1, STR_TO_DATE('2021-06-23','%Y-%m-%d'), '수요일', 2, '07:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(14, 4, 2, STR_TO_DATE('2021-07-30','%Y-%m-%d'), '수요일', 2, '03:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(15, 5, 9, STR_TO_DATE('2021-08-06','%Y-%m-%d'), '수요일', 2, '02:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(16, 6, 8, STR_TO_DATE('2021-09-13','%Y-%m-%d'), '수요일', 2, '01:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(17, 7, 7, STR_TO_DATE('2021-08-14','%Y-%m-%d'), '목요일', 2, '04:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(18, 8, 6, STR_TO_DATE('2021-07-17','%Y-%m-%d'), '수요일', 2, '06:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(19, 9, 5, STR_TO_DATE('2021-06-27','%Y-%m-%d'), '화요일', 2, '11:00PM');");
			stmt.executeUpdate("INSERT INTO Schedules VALUES(20, 10, 4, STR_TO_DATE('2021-11-17','%Y-%m-%d'), '금요일', 2, '11:00AM');");

			
	
			
			stmt.executeUpdate("INSERT INTO Tickets VALUES(1, 1, 1, 1, 1, 'Y', 14000, 14000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(2, 2, 3, 10, 2, 'Y', 14000, 14000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(3, 3, 5, 15, 3, 'Y', 14000, 12000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(4, 4, 7, 14, 4, 'Y', 14000, 14000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(5, 5, 2, 9, 5, 'Y', 14000, 14000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(6, 6, 4, 15, 6, 'Y', 14000, 12000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(7, 7, 6, 20, 7, 'N', 14000, 14000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(8, 8, 8, 19, 8, 'N', 14000, 12000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(9, 9, 1, 2, 9, 'N', 14000, 12000);");
			stmt.executeUpdate("INSERT INTO Tickets VALUES(10,10, 3, 3, 10, 'N', 14000, 12000);");
		}
		catch (SQLException e) {
			//e.printStackTrace();
		}
	}
	
	/*테이블에 대한 입력/삭제/변경 기능*/
	public boolean updateTable(String query) {
		try {
			stmt.executeUpdate(query);
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}
	
	/*테이블 보기*/
	public String[][] printOneTable(String name) {
		String[] table_name = new String[] {"Movies", "Customers", "Rooms", "Reservations", "Schedules", "Seats", "Tickets"};
		int[] table_row_size = new int[] { 9, 4, 3, 6, 7, 3, 8 };
		int row_size = 0;
		int col_size = 0;
		int j=0;
		String[][] table; 
		
		for(int i=0; i<7; i++) {
			if(table_name[i] == name) {
				row_size = table_row_size[i];
				break;
			}
		}
		
		try {
			String temp = null;
			
			rs = stmt.executeQuery("select count(*) from " +name);
			while(rs.next()) {
				temp = rs.getString(1);
			}
			col_size = Integer.parseInt(temp);
			
			rs = stmt.executeQuery("select * from "+name);
			
			table = new String[col_size][row_size];
			
			while(rs.next()) {
				
				for(int i=0; i<row_size; i++) {
					temp = rs.getString(i+1);
					table[j][i] = temp;
				}
				j++;
				
			}
			return table;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*고객*/
	/*영화 조회*/
	public ArrayList<ArrayList<String>> movieSearch(String information) {
		
		String query="SELECT * FROM movies ";
		String [] information2 = information.split(",");
		String [] movieTable=new String[9];
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
		try {
			rs=stmt.executeQuery(query);
			String temp="Where ";
			String total="";
			while(rs.next()) {
				int cnt=0,signal=0;
				for(int i=0; i<information2.length; i++) {
					if(rs.getString(2).equals(information2[i])) {
						if(signal==0) {
							temp+="movie_name = ";
							signal=1;
						}
						else {
							temp+="and movie_name = ";
						}
						temp+="\""+information2[i]+"\"";
						cnt+=1;
					
					}

					else if(rs.getString(5).equals(information2[i])) {
						if(signal==0) {
							temp+="director_name= ";
							signal=1;
						}
						else {
							temp+="and director_name = ";
						}
						temp+="\""+information2[i]+"\"";
						cnt+=1;


					}
					else if(rs.getString(6).equals(information2[i])) {
						if(signal==0) {
							temp+="actor_name = ";
							signal=1;
						}
						else {
							temp+="and actor_name = ";
						}
						temp+="\""+information2[i]+"\"";
						cnt+=1;

					}
					else if(rs.getString(7).equals(information2[i])) {
						if(signal==0) {
							temp+="jenre = ";
							signal=1;
						}
						else {
							temp+="and jenre = ";
						}
						temp+="\""+information2[i]+"\"";
						cnt+=1;
					}

				}
				if(cnt==information2.length) {
					total=query+temp;
					break;
				}
				else {
					total=null;
				}
				
			}
			if(total!=null) {

				rs=stmt.executeQuery(total);
				while(rs.next()) {
					ArrayList<String>data = new ArrayList<String>();
					for(int i=0; i<9; i++) {
						data.add(rs.getString(i+1));
					}
					datas.add(data);
				}				
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	
	
	/*에매기능 실질적으로 실행하는 메소드*/
	public void Reservation(String cust_id, String schedule_id, int movie_id, String room_id, String seat_id, String way, String state, boolean membership, boolean ticket) {
		try {
			String temp = null;
			int reservation_id = 0;
			int ticket_id = 0;
			int sale_price = base_price;
			if(membership) sale_price = base_price - 2000;
			String available = "N";
			if(ticket) available = "Y";
			
			rs = stmt.executeQuery("select max(reservation_id) from reservations");
			while(rs.next()) {
				temp = rs.getString(1); //현재 예약 수
			} 
			reservation_id = Integer.decode(temp).intValue() + 1;
			
			rs = stmt.executeQuery("select max(ticket_id) from tickets");
			
			while(rs.next()) {
				temp = rs.getString(1); //현재 티켓 수
			}
			ticket_id = Integer.decode(temp).intValue() + 1;
			
			
			temp = "insert into Reservations values(" + reservation_id + ", '" + way + "', '" + 
							state + "', " + sale_price + ", " + cust_id + ", STR_TO_DATE('" + now + "','%Y-%m-%d')"  + ");";
			stmt.executeUpdate(temp);
			
			temp = "insert into Tickets values(" + ticket_id + ", " + schedule_id + ", " + room_id + ", " +
						seat_id + ", " + reservation_id + ", '" + available + "', " + base_price + ", " + sale_price + ");";
			
			stmt.executeUpdate(temp);
			
			temp = "update seats set available = 'N' where seat_id = " + seat_id + " and room_id = " + room_id + ";";
			
			stmt.executeUpdate(temp);
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*예매한 영화에 대한 영화 정보 출력*/
	public ArrayList<ArrayList<String>> reserInfo(String custid) {
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
		String movie_name = null;
		String screendate = null;
		String room_id = null;
		String seat_id = null;
		String sale_price = null;
		ResultSet rs1, rs2;
		
		try {
			Statement stmt1 = connection.createStatement();
			Statement stmt2 = connection.createStatement();
			
			String query="";
			query+="select schedule_id, room_id, seat_id, sale_price from tickets where reservation_id in (select reservation_id from reservations where cust_id = "
					+ custid + ");";
			
			rs=stmt.executeQuery(query);
			while(rs.next()) {
				ArrayList<String> data = new ArrayList<String>();
				for(int i=2; i<=4; i++) {
					data.add(rs.getString(i));
				}
				query = "select movie_id, screendate from schedules where schedule_id = " + rs.getString(1);
				rs1 = stmt1.executeQuery(query);
				while(rs1.next()){
					data.add(0, rs1.getString(2));
					query = "select movie_name from movies where movie_id = " + rs1.getString(1);	
					rs2 = stmt2.executeQuery(query);
					while(rs2.next()) {
						data.add(0, rs2.getString(1));
					}
				}
				datas.add(data);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return datas;
	}
	public ArrayList<ArrayList<String>> schedule_reserInfo(int movie_id) {
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

		String schedulequery="";
		schedulequery+="select * from schedules where movie_id = ";
		schedulequery+=(movie_id+"");
		try {

			rs=stmt.executeQuery(schedulequery);

			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<7; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return datas;
	}
	public ArrayList<ArrayList<String>> schedule_reserInfo_2(String custid) {
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

		String schedulequery="";
		schedulequery+="select * from schedules where schedule_id IN (select schedule_id from tickets where reservation_id IN (select reservation_id from reservations where reservations.cust_id=";
		schedulequery+=(custid+"))");
		try {

			rs=stmt.executeQuery(schedulequery);

			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<7; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return datas;
	}
	public ArrayList<ArrayList<String>> schedule_reserInfo_3(String moviename) {
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

		String schedulequery="";
		schedulequery+="select * from schedules where movie_id=(select movie_id from movies where movie_name= ";
		schedulequery+=("\""+moviename+"\")");
		try {

			rs=stmt.executeQuery(schedulequery);

			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<7; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);

			}
			return datas;

		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	public ArrayList<ArrayList<String>> room_reserInfo_2(String custid) {
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

		String roomquery="";
		roomquery+=		"select * from rooms where room_id IN (select room_id from tickets where reservation_id IN (select reservation_id from reservations where reservations.cust_id=";
		roomquery+=(custid+"))");
		try {

			rs=stmt.executeQuery(roomquery);

			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<3; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);
				
			}
			return datas;
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
	}
	public ArrayList<ArrayList<String>> ticket_reserInfo_2(String custid) {
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

		String ticketquery="";
		ticketquery+="select * from tickets where  reservation_id IN (select reservation_id from reservations where reservations.cust_id=";
		ticketquery+=(custid+")");
		try {

			rs=stmt.executeQuery(ticketquery);

			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<8; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);
			}
			return datas;
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	public ArrayList<ArrayList<String>> reserve_show(int custid) {
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

		String query=("select * from reservations where cust_id=");
		query+=(custid+"");
		try {
			
			rs=stmt.executeQuery(query);
			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<6; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);
			}

			return datas;

		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getReservationID(String custid) {
		ArrayList<String> data = new ArrayList<String>();
		try {
			String temp = "select reservation_id from reservations where cust_id=" + custid + ";";
			rs = stmt.executeQuery(temp);
			while(rs.next()) {
				temp = rs.getString(1);
				data.add(temp);
			}
			return data;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<ArrayList<String>> seat_show(String room_id){
		try {
			String query="";
			ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

			query="select * from seats where room_id=";
			query+=room_id;
			rs=stmt.executeQuery(query);
			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<3; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);
			}
			return datas;

		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public ArrayList<ArrayList<String>> seat_show2(String room_id){
		try {
			String query="";
			ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

			query="select * from seats where room_id=";
			query+=room_id;
			
			rs=stmt.executeQuery(query);
			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<3; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);
			}
			return datas;

		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<ArrayList<String>> seat_show3(int row, String table_info){
	      try {
	         String query="";
	         ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

	         query="select * from seats where room_id=(select room_id from schedules where  schedule_id = ";
	         query+=table_info+")";
	         
	         rs=stmt.executeQuery(query);
	         while(rs.next()) {
	            ArrayList<String>data = new ArrayList<String>();
	            for(int i=0; i<3; i++) {
	               data.add(rs.getString(i+1));
	            }
	            datas.add(data);
	         }
	         return datas;

	      }
	      catch(SQLException e) {
	         e.printStackTrace();
	         return null;
	      }
	}
	

	public  ArrayList<ArrayList<String>> movieTable() {
		try {
			String query="";
			ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();

			query="select * from movies";
			
			rs=stmt.executeQuery(query);
			while(rs.next()) {
				ArrayList<String>data = new ArrayList<String>();
				for(int i=0; i<9; i++) {
					data.add(rs.getString(i+1));
				}
				datas.add(data);
			}
			return datas;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*예매한 영화 삭제 기능*/
	public int delete(String reservation_id, String movie_name, String room_id, String seat_id) {
		int result=0;
		String query="";
		try {
			query = "update seats set available = 'Y' where room_id = " + room_id + " and seat_id = " + seat_id;
			stmt.executeUpdate(query);
			query = "";
			query+="delete from tickets where room_id= ";
			query+=(room_id);
			query+=" AND seat_id = ";
			query+=(seat_id);
			query+=" AND schedule_id in (select schedule_id from schedules where movie_id in (select movie_id from movies where movie_id in (select movie_id from movies where movie_name = ";
			query+=("\""+movie_name+"\")))");
		
			ps=connection.prepareStatement(query);
			result=ps.executeUpdate();
			
			query="";	
			query+="delete from reservations where reservation_id = " + reservation_id;
			ps=connection.prepareStatement(query);
			result=ps.executeUpdate();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	
	/*관리자인지, 아닌지에 대한 boolean 반환 메소드*/
	public boolean login(String id, String password) {
		if((id.equals("17011517") || id.equals("20010459") || id.equals("1234")) && password.equals("1234")) {
			return true;
		}
		else return false;
	}
	/*회원인지, 아닌지에 대한 boolean 반환 메소드*/
	public boolean isMember(String id) {
		try {
			rs = stmt.executeQuery("select count(*) from Customers where cust_id =" + id);
			while(rs.next()) {
				if(Integer.decode(rs.getString(1)).intValue()==0) return false;
				else return true;
			}
			return false;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public String movie_id_search(int row, String table_info){
		try {
			String query="select movie_id from movies where ";
			query+= table_info;
			rs=stmt.executeQuery(query);
			String temp=null;
			while(rs.next()) {
				temp = rs.getString(1);	
			}
			return  temp;

		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean addMember(String id, String name, String phone, String email) {
		try {
			stmt.executeUpdate("INSERT INTO Customers VALUES(" + id + ", '" + name + "', '" + phone + "', '" + email + "')");
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String[][] getReservationinfo(String id) {
		String temp;
		String[][] table;
		
		Statement stmt2;
		ResultSet rs2;
		
		int col_size = 0;
		int row_size = 5;
		int j = 0;
		
		try {
			stmt2 = connection.createStatement();
			
			rs = stmt.executeQuery("select count(*) from reservations where cust_id = " + id);
			
			while(rs.next()) {
				temp = rs.getString(1);
				col_size = Integer.decode(temp).intValue();
			}
			
			table = new String[col_size][row_size];
			
			rs = stmt.executeQuery("select reservation_id from reservations where cust_id = " + id);
			
			while(rs.next()) {
				temp = rs.getString(1); //reservation_id
				
				rs2 = stmt2.executeQuery("select room_id, seat_id, base_price from tickets where reservation_id = " + temp);
				
				while(rs2.next()) {
					temp = rs2.getString(1); //room_id
					table[j][2] = rs2.getString(1); //room_id
					table[j][3] = rs2.getString(2); //seat_id
					table[j][4] = rs2.getString(3); //base_price
				}
				rs2 = stmt2.executeQuery("select movie_id, screenday from schedules where room_id = " + temp);
				
				while(rs2.next()) {
					temp = rs2.getString(1); //movie_id
					table[j][1] = rs2.getString(2); //screenday
				}
				rs2 = stmt2.executeQuery("select movie_name from movies where movie_id = " + temp);
				
				while(rs2.next()){
					table[j][0] = rs2.getString(1); //movie_name
					
				}
				
				j++;
			}
			return table;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String[][] getticketinfo(String room_id, String seat_id) {
		String[][] data = new String[3][];
		String schedule_id = null;
	
		try {
			rs = stmt.executeQuery("select * from tickets where room_id = " + room_id + " and seat_id = " + seat_id);
			while(rs.next()) {
				data[2] = new String[8];
				for(int i=1; i<=8; i++) {
					data[2][i-1] = rs.getString(i);
				}
				schedule_id = rs.getString(2);
			}
			
			rs = stmt.executeQuery("select * from schedules where schedule_id = " + schedule_id);
			while(rs.next()) {
				data[0] = new String[7];
				for(int i=1; i<=7; i++) {
					data[0][i-1] = rs.getString(i);
				}
			}
			rs = stmt.executeQuery("select * from rooms where room_id = " + room_id);
			while(rs.next()) {
				data[1] = new String[3];
				for(int i=1; i<=3; i++) {
					data[1][i-1] = rs.getString(i);
				}
			}
			return data;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public boolean changeReservation(String reservation_id, String schedule_id, String room_id, String seat_id) {
		try {
			String temp;
			String ticket_id = null;
			String preroomid = null;
			String preseatid = null;
			
			temp = "select ticket_id from tickets where reservation_id = "+ reservation_id;
			
			rs = stmt.executeQuery(temp);
			
			while(rs.next()) {
				ticket_id = rs.getString(1);
			}
			
			temp = "select room_id, seat_id from tickets where reservation_id = " + reservation_id;
			rs = stmt.executeQuery(temp);
			
			while(rs.next()) {
				preroomid = rs.getString(1);
				preseatid = rs.getString(2);
			}
			
			temp = "update seats set available = 'Y' where seat_id = " + preseatid + " and  room_id = " + preroomid +";";  
			//System.out.println(temp);
			stmt.executeUpdate(temp);
			
			temp = "update tickets set schedule_id = " + schedule_id + ", room_id = " + room_id + ", seat_id = " + seat_id + " where ticket_id =" + ticket_id;
			//System.out.println(temp);
			stmt.executeUpdate(temp);
			
			temp = "update seats set available = 'N' where seat_id = " + seat_id + " and room_id = " + room_id +";";
			//System.out.println(temp);
			stmt.executeUpdate(temp);
			
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}

class MouseHandler implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton)e.getSource();
		button.setBackground(Color.pink);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton)e.getSource();
		button.setBackground(Color.white);
	}
}


public class teamproject {	
	/*mysql연결*/
	public static Connection loadDB() {
		Connection connection = null;
		String url = "jdbc:mysql://localhost:3306/movie";
		String userId = "madang";
		String password = "madang";
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); //MySQL Driver load
			System.out.println("Driver load success");
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
			
		try {
			connection = DriverManager.getConnection(url, userId, password);
			System.out.println("Database connection success");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static void main(String[] args) {
		new mainFrame();
	//	new selectcaseFrame("7번방의 선물");
	}
}



/*시작 윈도우 창 - 관리자 및 회원 선택*/
class mainFrame extends JFrame{
	 Container page = getContentPane();

	 
	 private JButton manager = new JButton();
	 private JButton cust = new JButton();
	 
	 private JLabel label1 = new JLabel("Manager");
	 private JLabel label2 = new JLabel("Customer");
	 
	 private ImageIcon managerImg = new ImageIcon(mainFrame.class.getResource("/teamproject/img/manager.png"));
	 private ImageIcon custImg = new ImageIcon(mainFrame.class.getResource("/teamproject/img/customer.png"));
	 
	 public mainFrame(){
		setTitle("Movie Management Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		page.setLayout(null);
		
		page.add(manager);
	    page.add(cust);
	    
	    page.add(label1);
	    page.add(label2);
	    
	    
		manager.setBounds(20, 25, 100, 100);
		cust.setBounds(170, 25, 100, 100);
		label1.setBounds(20, 130, 100, 20);
		label2.setBounds(170, 130, 100, 20);
		
		manager.setIcon(managerImg);
		cust.setIcon(custImg);
	    
		page.setBackground(Color.LIGHT_GRAY);
		manager.setBackground(Color.white);
		cust.setBackground(Color.white);
		
		manager.addMouseListener(new MouseHandler());
		cust.addMouseListener(new MouseHandler());
		
		
	    page.setVisible(true);
	    
	    /*관리자 버튼 event handler*/  
	    manager.addActionListener(event -> {
	    	setVisible(false);
	    	JFrame loginFrame = new loginFrame();
	    	loginFrame.setVisible(true);
	    });
	    /*회원 버튼 event handler*/
	    cust.addActionListener(event -> {
	    	setVisible(false);
	    	JFrame customerloginFrame = new memberselectFrame();
	    	customerloginFrame.setVisible(true);
	    });
	    setSize(300,200);
	    setVisible(true);
	}
	
}
/*관리자 로그인 창 - id=17011517 or 20010459 password=1234*/
class loginFrame extends JFrame {
	Container page = getContentPane();
	
	private JLabel information = new JLabel("ID와 PASSWORD를 입력해주세요.");
	private JLabel id_label = new JLabel("ID");
	private JLabel password_label = new JLabel("Password");
	private JTextField id_text = new JTextField();
	private JPasswordField password_text = new JPasswordField();
	private JButton login_button = new JButton("");
	
	private ImageIcon loginImg = new ImageIcon(mainFrame.class.getResource("/teamproject/img/login.png"));
	
	DBManager program = new DBManager();
	
	public loginFrame() {
		setTitle("로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		page.setLayout(null);
		
		page.setVisible(true);
		
		page.add(information);
		page.add(id_label);
		page.add(password_label);
		
		page.add(id_text);
		page.add(password_text);
		page.add(login_button);
		
		page.setBackground(Color.LIGHT_GRAY);
		login_button.setBackground(Color.white);
		
		loginImg = changeSize(loginImg);
		login_button.setIcon(loginImg);
		
		information.setBounds(50, 5, 200, 15);
		id_label.setBounds(35, 25, 20, 20);
		id_text.setBounds(90, 25, 100, 20);
		password_label.setBounds(10, 55, 80, 20);
		password_text.setBounds(90, 55, 100, 20);
		
		login_button.setBounds(215, 25, 50, 50);
		login_button.addMouseListener(new MouseHandler());
		
		setSize(300, 130);
		/*login 버튼 event handler*/
		login_button.addActionListener(event-> {
			if(program.login(id_text.getText(), password_text.getText())) {
				setVisible(false);
				JFrame managerFrame = new managerFrame(); //managerFrame 객체 생성
			}
			else {
				information.setText("ERROR: Wrong ID or Password"); //관리자 정보가 아닐 경우 error msg 출력
			}
		});
		
	}
	
	
	
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(40,40,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
}
/*관리자 윈도우 창 - 초기화 & 테이블 변경 & 테이블 보기 선택*/
class managerFrame extends JFrame {
	Container page = getContentPane();
	
	JButton init = new JButton("초기화");
	JButton changeTable = new JButton("테이블 변경");
	JButton printTable = new JButton("테이블 보기");
	JLabel information = new JLabel("원하는 버튼을 클릭하세요.");
	
	DBManager db = new DBManager();
	
	public managerFrame() {
		setTitle("Movie Management Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		page.setLayout(new FlowLayout());
		
		page.add(init);
		page.add(changeTable);
		page.add(printTable);
		page.add(information);
		//page.setBackground(Color.LIGHT_GRAY);
		
		init.setBackground(Color.white);
		changeTable.setBackground(Color.white);
		printTable.setBackground(Color.white);
		
		init.addMouseListener(new MouseHandler());
		changeTable.addMouseListener(new MouseHandler());
		printTable.addMouseListener(new MouseHandler());
		
		page.setVisible(true);
		/*초기화 버튼은 누르면 바로 진행 후 완료 문구 출력*/
		init.addActionListener(event -> {
			db.initTable();
			information.setText("초기화가 완료되었습니다.");
		});
		/*update를 위한 frame생성*/
		changeTable.addActionListener(event-> {
			JFrame updateFrame = new updateFrame(page);
		//	page.setVisible(false);
		});
		/*print할 table을 선택하는 frame생성*/
		printTable.addActionListener(event-> {
			JFrame printTableFrame = new selectTableFrame();
			
		});
		
		addWindowListener(new windowListener());
		
		setSize(300, 100);
		setVisible(true);
	}
	class windowListener implements WindowListener {
		JFrame mainFrame;
		
		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			mainFrame = new mainFrame();
		
		}

		@Override
		public void windowClosed(WindowEvent e) {
			mainFrame.setVisible(true);
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
/*update를 위한 윈도우 창*/
class updateFrame extends JFrame {
	Container page = getContentPane();
	JLabel information = new JLabel("입력, 삭제, 변경 SQL문을 작성해주세요.");
	JTextArea sql = new JTextArea(); 
	JButton save = new JButton("입력");
	JButton cancel = new JButton("취소");
	JButton ex = new JButton("도움말");
	JScrollPane scroll = new JScrollPane(sql);
	DBManager db = new DBManager();
	
	public updateFrame (Container previouspage) {
		setTitle("Change TABLE");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		page.setLayout(null);
		
		information.setBounds(20, 20, 600, 20);
		sql.setBounds(20, 50, 800, 20);
		save.setBounds(20, 80, 100, 30);
		cancel.setBounds(120, 80, 100, 30);
		ex.setBounds(720, 80, 100, 30);
		
		page.add(information);
		page.add(sql);
		page.add(save);
		page.add(cancel);
		page.add(ex);
		page.add(scroll);
		
		save.setBackground(Color.white);
		cancel.setBackground(Color.white);
		ex.setBackground(Color.white);
		
		save.addMouseListener(new MouseHandler());
		cancel.addMouseListener(new MouseHandler());
		ex.addMouseListener(new MouseHandler());
		
		page.setVisible(true);
		
		save.addActionListener(event -> {
			String temp = sql.getText();
			if(db.updateTable(temp)) {
				information.setText("변경이 완료되었습니다.");
				sql.setText("");
			}
			else {
				information.setText("변경에 실패하였습니다. 도움말을 참고하여 작성해주세요.");
				sql.setText("");
			}
		});
		
		cancel.addActionListener(event1 -> {
			information.setText("다시 입력, 삭제, 변경 SQL문을 작성해주세요.");
			sql.setText("");
		});
		
		ex.addActionListener(event2 -> {
			JFrame helpFrame = new helpFrame(page);
			
		});
		
		
		setSize(850, 160);
		setVisible(true);
	}
	
}
/*관리자 update sql문 도움말 window창*/
class helpFrame extends JFrame {
	Container page = getContentPane();
	JLabel help = new JLabel("<sql문 작성을 위한 도움말>");
	JLabel[] information = new JLabel[14];
	
	
	public helpFrame(Container previous){
		setTitle("도움말");
	//	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		help.setSize(800,20);
		page.setLayout(null);
		page.add(help);
		
		help.setBounds(20, 20, 1000, 20);
		
		for(int i=0; i<14; i++) {
			information[i] = new JLabel();
			information[i].setBounds(20, 40+i*20, 1000, 20);
			
		}
		
		for(int i=0; i<14; i++) {
			page.add(information[i]);
		}
		
		information[0].setText("입력");
		information[1].setText("=> INSERT INTO 테이블명 values(열 데이터, ...)");
		information[2].setText("삭제");
		information[3].setText("=> DELETE FROM 테이블명 where 조건");
		information[4].setText("변경");
		information[5].setText("=> UPDATE 테이블명 SET 변경할 열 = 데이터 where 행 선별 조건");
		information[6].setText("테이블 data 형식");
		information[7].setText("Customers: cust_id, cust_name, phone, email");
		information[7].setToolTipText("EX] Customers: VALUES(16105236, '유재석', '010-7451-1425', 'useok@naver.com');");
		information[8].setText("Movies: movie_id, movie_name, screen_time, screen_rank, director_name, actor_name, jenre, introduction, opendate");
		information[8].setToolTipText("EX] INSERT INTO Movies VALUES(10, '부산행', '118분', '15세', '연상호', '공유', '액션', '부산으로 향하던 ktx안에서 벌어지는 좀비이야기', STR_TO_DATE('2021-12-27','%Y-%m-%d'));");
		information[9].setText("Reservations: reservation_id, payment_way, payment_state, payment_price, cust_id, payment_date");
		information[9].setToolTipText("EX] Reservations VALUES(10, '카드', 'N', 12000, 19001458, STR_TO_DATE('2021-03-17','%Y-%m-%d'));");
		information[10].setText("Rooms: room_id, seat_count, available");
		information[10].setToolTipText("EX] INSERT INTO Rooms VALUES(10, 20, 'N');");
		information[11].setText("Schedules: schedule_id, movie_id, room_id, screendate, screenday, screencount, screentime");
		information[11].setToolTipText("EX] Schedules VALUES(10, 3, 3, STR_TO_DATE('2021-03-17','%Y-%m-%d'), '수요일', 1, '06:00PM');");
		information[12].setText("Seats: seat_id(int), room_id(int), available(string)");
		information[12].setToolTipText("EX] INSERT INTO Seats VALUES(20, 10, 'Y');");
		information[13].setText("Tickets: ticket_id, schedule_id, room_id, seat_id, reservation_id, available, base_price, sale_price");
		information[13].setToolTipText("EX] Tickets VALUES(10,10, 3, 3, 10, 'N', 14000, 12000);");
		
		page.setVisible(true);
		
		setSize(800, 400);
		setVisible(true);
	}
}
/*보고싶은 table을 선택하는 frame*/
class selectTableFrame extends JFrame {
	Container page = getContentPane();
	JLabel[] table_name = new JLabel[7];
	JButton[] table = new JButton[7];
	JLabel information = new JLabel("출력을 원하는 테이블을 선택하세요!");
	
	DBManager db = new DBManager();
	
	private ImageIcon movie = new ImageIcon(mainFrame.class.getResource("/teamproject/img/movie.png"));
	private ImageIcon customer = new ImageIcon(mainFrame.class.getResource("/teamproject/img/customer2.png"));
	private ImageIcon room = new ImageIcon(mainFrame.class.getResource("/teamproject/img/room.png"));
	private ImageIcon seat = new ImageIcon(mainFrame.class.getResource("/teamproject/img/seat.png"));
	private ImageIcon schedule = new ImageIcon(mainFrame.class.getResource("/teamproject/img/schedule.png"));
	private ImageIcon reservation = new ImageIcon(mainFrame.class.getResource("/teamproject/img/reservation.png"));
	private ImageIcon ticket = new ImageIcon(mainFrame.class.getResource("/teamproject/img/ticket.png"));
	
	public selectTableFrame() {
		setTitle("Change TABLE");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		page.setLayout(null);
		
		for(int i=0; i<7; i++) {
			table_name[i] = new JLabel();
			table[i] = new JButton("");
		}
		
		for(int i=0; i<7; i++) {
			table_name[i].setBounds(20+100*i, 20, 100, 20);
			table[i].setBounds(30+100*i, 60, 80, 60);
			table[i].setText("" + i);
			table_name[i].setHorizontalAlignment(SwingConstants.CENTER);
			table[i].setBackground(Color.WHITE);
			table[i].addMouseListener(new MouseHandler());
			table[i].setOpaque(true);
		}
		
		information.setBounds(250, 130, 250, 20);
		page.add(information);
		
		movie = changeSize(movie);
		customer = changeSize(customer);
		room = changeSize(room);
		seat = changeSize(seat);
		schedule = changeSize(schedule);
		reservation = changeSize(reservation);
		ticket = changeSize(ticket);
		
		table_name[0].setText("Movies");
		table_name[1].setText("Customers");
		table_name[2].setText("Rooms");
		table_name[3].setText("Reservations");
		table_name[4].setText("Schedules");
		table_name[5].setText("Seats");
		table_name[6].setText("Tickets");
		
		table[0].setIcon(movie);
		table[1].setIcon(customer);
		table[2].setIcon(room);
		table[3].setIcon(reservation);
		table[4].setIcon(schedule);
		table[5].setIcon(seat);
		table[6].setIcon(ticket);
		
		for(int i=0; i<7; i++) {
			int index = i;
			table[i].addActionListener(event-> {
				
				JFrame printTableFrame = new printTableFrame(db.printOneTable(table_name[index].getText()), index);
			});
		}
		
		
		
		for(int i=0; i<7; i++) {
			page.add(table_name[i]);
			page.add(table[i]);
		}
		
		page.setVisible(true);
		
		setSize(750,200);
		setVisible(true);
	}
	
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(50,50,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
	
}
/*하나의 테이블을 보여주는 Frame*/
class printTableFrame extends JFrame {
	Container page = getContentPane();
	
	int[] rowLength = { 9, 4, 3, 6, 7, 3, 8 };
	
	private String[][] row_data = new String[][] {
		{"영화번호", "영화제목", "상영시간", "제한이용가", "감독명", "배우명", "장르", "소개", "개봉일"},
		{"아이디", "이름", "핸드폰", "이메일"},
		{"상영관번호", "좌석 수", "사용가능"},
		{"예매번호", "결제수단", "결제상태", "결제금액", "고객아이디", "결제일"},
		{"상영시간번호", "영화번호", "상영관번호", "상영일", "상영요일", "상영횟수", "상영시간"},
		{"좌석번호", "상영관번호", "사용가능"},
		{"티켓번호", "상영시간번호", "상영관번호", "좌석번호", "예매번호", "발급여부", "표준금액", "할인금액"}
	};
	
	private String[] row; //행
	private String[][] data; //data 행렬
	
	private JTable table;
	private JScrollPane scrollpane;
	
	printTableFrame(String[][] obj, int index){
		setPreferredSize(new Dimension(1500, 300));
		setLocation(100,100);
		setTitle("Print table");
		row = new String[rowLength[index]];
		row = row_data[index];
		
		data = obj;
		
		table = new JTable(data, row);
		
		scrollpane = new JScrollPane(table);
		page.add(scrollpane, BorderLayout.CENTER);
		
		page.setVisible(true);
		pack();
		
		setVisible(true);
	
	}
}

/*회원-비회원 선택 창*/
class memberselectFrame extends JFrame {
	Container page = getContentPane();
	private JLabel member = new JLabel("회원");
	private JLabel nomember = new JLabel("비회원");
	
	private JButton membtn = new JButton();
	private JButton nomembtn = new JButton();
	
	private ImageIcon memImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/member.png"));
	private ImageIcon nomemImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/nomember.png"));
	
	memberselectFrame (){
		setTitle("회원이세요?");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		page.setLayout(null);
		
		membtn.setBounds(20, 25, 100, 100);
		nomembtn.setBounds(150, 25, 100, 100);
		member.setBounds(20, 130, 100, 20);
		nomember.setBounds(150, 130, 100, 20);
		
		page.setBackground(Color.lightGray);
		membtn.setBackground(Color.WHITE);
		nomembtn.setBackground(Color.WHITE);
		
		
		membtn.setOpaque(true);
		nomembtn.setOpaque(true);
		
		member.setHorizontalAlignment(JLabel.CENTER);
		nomember.setHorizontalAlignment(JLabel.CENTER);
		
		memImg = changeSize(memImg);
		nomemImg = changeSize(nomemImg);
		
		membtn.setIcon(memImg);
		nomembtn.setIcon(nomemImg);
		
		membtn.addActionListener(event -> {
			setVisible(false);
			JFrame memberloginFrame = new memberloginFrame();
			memberloginFrame.setVisible(true);
		});
		
		membtn.addMouseListener(new MouseHandler());
		nomembtn.addMouseListener(new MouseHandler());
		
		nomembtn.addActionListener(event -> {
			setVisible(false);
			JFrame customerFrame = new customerFrame();
			customerFrame.setVisible(true);
		});
		
		
		page.add(member);
		page.add(nomember);
		page.add(membtn);
		page.add(nomembtn);
		
		
		
		setSize(280,200);
	    setVisible(true);
		
	}
	
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(50,50,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
}
/*회원 로그인 창*/
class memberloginFrame extends JFrame {
	DBManager program = new DBManager();
	
	Container page = getContentPane();
	Container page2 = getContentPane();
	
	
	private JLabel information = new JLabel("ID를 입력해주세요.");
	
	
	private JTextField id_text = new JTextField();
	private JButton login_button = new JButton();

	private ImageIcon loginImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/login.png"));
	
	
	public memberloginFrame() {
		setTitle("로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		page.setLayout(null);
		
		page.setVisible(true);
		
		page.setBackground(Color.LIGHT_GRAY);
		login_button.setBackground(Color.white);
		
		loginImg = changeSize(loginImg);
		
		login_button.setIcon(loginImg);
		
		login_button.addMouseListener(new MouseHandler());
		
		page.add(information);

		
		page.add(id_text);
		page.add(login_button);
		
		information.setBounds(60, 10, 200, 15);
		id_text.setBounds(20, 30, 180, 20);
		
		login_button.setBounds(205, 10, 40, 40);
		
		setSize(270, 100);
		/*login 버튼 event handler*/
		login_button.addActionListener(event-> {
			String temp = id_text.getText();
			if(program.isMember(temp)) {
				setVisible(false);
				JFrame customerFrame = new customerFrame(temp);
				customerFrame.setVisible(true);
			}
			else {
				information.setText("너 회원 아닌데?");
				id_text.setText("");
			}
		});
		
	}
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(30,30,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
	
}

/*고객 프레임 창*/
class customerFrame extends JFrame {
	Container page = getContentPane();
	
	JButton searchbtn = new JButton();
	JButton checkbtn = new JButton();
	JButton addbtn = new JButton();
	
	JLabel search = new JLabel("영화 검색");
	JLabel check = new JLabel("예매 조회");
	JLabel add = new JLabel("회원 가입");
	
	
	JLabel information = new JLabel("원하는 버튼을 클릭하세요.");
	
	private ImageIcon searchImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/search.png"));
	private ImageIcon checkImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/check.png"));
	private ImageIcon addImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/add.png"));
	
	ActionHandler checkAction = new ActionHandler();
	
	DBManager db = new DBManager();
	
	public customerFrame() {
		setTitle("고객");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		page.setLayout(null);
		
		searchImg = changeSize(searchImg);
		checkImg = changeSize(checkImg);
		addImg = changeSize(addImg);
		
		searchbtn.setIcon(searchImg);
		checkbtn.setIcon(checkImg);
		addbtn.setIcon(addImg);
		
		page.setBackground(Color.LIGHT_GRAY);
		searchbtn.setBackground(Color.white);
		checkbtn.setBackground(Color.white);
		addbtn.setBackground(Color.white);
		
		searchbtn.setBounds(10, 10, 90, 70);
		checkbtn.setBounds(110, 10, 90, 70);
		addbtn.setBounds(210, 10, 90, 70);
		
		search.setBounds(30, 90, 60, 20);
		check.setBounds(130, 90, 60, 20);
		add.setBounds(230, 90, 60, 20);
		
		information.setBounds(80, 130, 200, 20);
		
		searchbtn.addMouseListener(new MouseHandler());
		checkbtn.addMouseListener(new MouseHandler());
		addbtn.addMouseListener(new MouseHandler());
		
		page.add(searchbtn);
		page.add(checkbtn);
		page.add(addbtn);
		
		page.add(search);
		page.add(check);
		page.add(add);
		
		page.add(information);
		
		page.setVisible(true);
		/*영화 검색을 위해선 회원가입을 먼저 해야함*/
		searchbtn.addActionListener(checkAction);
		/*예매 조회를 하려면 회원가입을 먼저 해야함*/
		checkbtn.addActionListener(checkAction);
		/*회원 가입을 위한 frame생성*/
		addbtn.addActionListener(event -> {
			setVisible(false);
			JFrame addFrame = new addFrame();
		});
		
		setSize(330, 200);
		setVisible(true);
	}
	
	public customerFrame(String id) {
		this();
		addbtn.setVisible(false);
		add.setVisible(false);
		information.setLocation(30, 120);
		setSize(230, 200);
		searchbtn.removeActionListener(checkAction);
		/*영화 검색을 위한 프레임 생성*/
		searchbtn.addActionListener(event -> {
			JFrame movie_searchFrame = new movie_searchFrame(id);
			setVisible(false);
		});
		checkbtn.removeActionListener(checkAction);
		/*예매 조회를 위한 프레임 생성*/
		checkbtn.addActionListener(event->{
			JFrame reservationFrame = new reservation_Frame(id);
			setVisible(false);
		
		});
	}
	
	public class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			information.setText("먼저 회원 가입을 해주세요.");
		}
		
	}
	
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(50,50,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
}


/*회원가입 프레임*/
class addFrame extends JFrame{
	Container page = getContentPane();
	
	JLabel id_label = new JLabel("아이디");
	JLabel name_label = new JLabel("이름");
	JLabel phone_label = new JLabel("핸드폰");
	JLabel email_label = new JLabel("이메일");
	JLabel information = new JLabel("");
	
	JTextField id = new JTextField();
	JTextField name = new JTextField();
	JTextField phone = new JTextField();
	JTextField email = new JTextField();
	
	private ImageIcon yesImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/yes.png"));
	private ImageIcon noImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/no.png"));
	
	DBManager db = new DBManager();
	
	JButton okbtn = new JButton();
	JButton cancelbtn = new JButton();
	
	public addFrame() {
		setTitle("회원 가입");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		page.setLayout(null);
		
		id_label.setBounds(20, 10, 50, 20);
		name_label.setBounds(20, 40, 50, 20);
		phone_label.setBounds(20, 70, 50, 20);
		email_label.setBounds(20, 100, 50, 20);
		information.setBounds(30, 140, 180, 20);
		
		id_label.setHorizontalAlignment(JLabel.CENTER);
		name_label.setHorizontalAlignment(JLabel.CENTER);
		phone_label.setHorizontalAlignment(JLabel.CENTER);
		email_label.setHorizontalAlignment(JLabel.CENTER);
		information.setHorizontalAlignment(JLabel.LEFT);
		
		id.setBounds(80, 10, 200, 20);
		name.setBounds(80, 40, 200, 20);
		phone.setBounds(80, 70, 200, 20);
		email.setBounds(80, 100, 200, 20);
		id.setText("1234");
		
		okbtn.setBackground(Color.white);
		cancelbtn.setBackground(Color.white);
		page.setBackground(Color.lightGray);
		
		yesImg = changeSize(yesImg);
		noImg = changeSize(noImg);
		
		okbtn.setIcon(yesImg);
		cancelbtn.setIcon(noImg);
		
		okbtn.addMouseListener(new MouseHandler());
		cancelbtn.addMouseListener(new MouseHandler());
		
		
		okbtn.setBounds(190, 130, 40, 40);
		cancelbtn.setBounds(240, 130, 40, 40);
		
		okbtn.addActionListener(event -> {
			if(db.addMember(id.getText(), name.getText(), phone.getText(), email.getText())) {
				String cust_id = id.getText();
				setVisible(false);
				JFrame customerFrame = new customerFrame(cust_id);
			}
			else {
				information.setText("존재하는 아이디입니다.");
				id.setText("");
			}
		});
		
		cancelbtn.addActionListener(event -> {
			id.setText("");
			name.setText("");
			phone.setText("");
			email.setText("");
		});
		
		addWindowListener(new My());
		
		page.add(id_label);
		page.add(name_label);
		page.add(phone_label);
		page.add(email_label);
		page.add(information);
		page.add(id);
		page.add(name);
		page.add(phone);
		page.add(email);
		page.add(okbtn);
		page.add(cancelbtn);
		
		page.setVisible(true);
		
		setSize(320, 220);
		setVisible(true);
		
	}
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(35,35,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
	
	
	public class My implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			JFrame customerFrame = new customerFrame();
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
		
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
	
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
		
		}
		
	}
}

class movie_searchFrame extends JFrame{
	Container c=getContentPane();
	JPanel p = new JPanel(new FlowLayout());
	JPanel pa = new JPanel(new FlowLayout());
	JPanel pb = new JPanel(new FlowLayout());

	JLabel label = new JLabel("영화정보를 검색하시오");
	JTextField tf = new JTextField(30);
	//JLabel la = new JLabel("영화정보");
	JButton btn = new JButton("검색");
	JTable jt;
	JScrollPane sp;
	DBManager db = new DBManager();
	String [][] data_array;
	String[] columns={"영화 번호", "영화 제목","상영 시간","상영 등급","감독명","배우명","장르","영화 소개","개봉일"};
	int movie_id;
	String ID;
	public movie_searchFrame(String cust_id){
		ID = cust_id;
	    setTitle("영화정보");
	    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    c.setLayout(new BorderLayout(0,30));
	    setLocationRelativeTo(null);
	    //label.setBounds(200,20,30,20);
	    setLocation(500, 100);
	    p.add(label);
	    p.add(tf);
	    p.add(btn);
	    p.setBackground(Color.yellow);
	    c.add(p, BorderLayout.NORTH);
	    addWindowListener(new myWindowListener());  
	      
	    //c.add(pa);
	    btn.addActionListener(new ActionListener() {
	       public void actionPerformed(ActionEvent e) {
	          
	          ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
	          String t=tf.getText();
	          datas = db.movieSearch(t);
	          
	          data_array= new String[datas.size()][9];
	          for(int k=0; k<datas.size(); k++) {
	             for(int i=0; i<9; i++) {
	                data_array[k][i]=datas.get(k).get(i);
	             }
	          }
	          jt = new JTable(data_array, columns);
	           
	          jt.getColumnModel().getColumn(2).setPreferredWidth(150);
	          jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	          
	          sp = new JScrollPane(jt);
	          c.add(sp,BorderLayout.CENTER);
	          
	          setSize(600, 800);
	          
	          jt.addMouseListener(new MouseListener() {
	             public void mouseClicked(MouseEvent e) {
	                int row;
	                int cel=0;
	                JTable jt=(JTable)e.getSource();
	                row=jt.getSelectedRow();
	                String table_info=(String)jt.getValueAt(row, 0);
	               
	                movie_id = Integer.parseInt(table_info);
	                JFrame schedule= new schedule_Frame(cust_id, movie_id);
	                setVisible(false);
	             }
	             @Override
	             public void mousePressed(MouseEvent e) {
	                 // TODO Auto-generated method stub
	                 
	             }
	              @Override
	             public void mouseReleased(MouseEvent e) {
	                 // TODO Auto-generated method stub
	                 
	             }
	              @Override
	             public void mouseEntered(MouseEvent e) {
	                 // TODO Auto-generated method stub
	                 
	             }
	              @Override
	             public void mouseExited(MouseEvent e) {
	                 // TODO Auto-generated method stub
	                 
	             }
	          });
	            
	            
	            
	          tf.setText("");      
	          setVisible(true);
	            
	            
	            
	          c.remove(jt);
	       }
	      
	    });

	    c.setVisible(true);

	    setSize(600,80);
	    setVisible(true);

	  }
	  class myWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			JFrame customerFrame = new customerFrame(ID);
		
			setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		  
	  }
	   
}
class schedule_Frame extends JFrame{
	Container c = getContentPane();
	JLabel label=new JLabel("영화 스케줄");
	JTable jt;
	JScrollPane sp;
	JPanel p = new JPanel();
	DBManager db = new DBManager();
	public schedule_Frame(String cust_id, int movie_id) {
		setTitle("영화 스케줄");
	//	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		c.setLayout(new BorderLayout(0,30));
		
		p.add(label);
		c.add(p,BorderLayout.NORTH);
		ArrayList<ArrayList<String>> schedule_datas = new ArrayList<ArrayList<String>>();

		schedule_datas = db.schedule_reserInfo(movie_id);
		
		String[] schedule_columns={"상영일정번호", "영화번호","상영관 번호","상영시작일","상영요일","상영회차","상영시작시간"};

		String [][]schedule_data_array= new String[schedule_datas.size()][schedule_columns.length];
		
		for(int k=0; k<schedule_datas.size(); k++) {
			for(int i=0; i<schedule_columns.length; i++) {
				schedule_data_array[k][i]=schedule_datas.get(k).get(i);
			}
		}

		jt = new JTable(schedule_data_array, schedule_columns);
		sp = new JScrollPane(jt);
		
		jt.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				int row;
				int cel=0;
				String room_id = null;
				String schedule_id = null;
				JTable jt=(JTable)e.getSource();
				row=jt.getSelectedRow();
				String table_info=(String)jt.getValueAt(row, cel);
				room_id = (String)jt.getValueAt(row, 2);
				schedule_id = (String)jt.getValueAt(row, 0);
				JFrame seats= new seat_Frame(cust_id, schedule_id, movie_id, room_id, row, table_info);
				setVisible(false);

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		c.add(sp);
		c.setVisible(true);
		setSize(600,800);
		setVisible(true);
		
	}
}
class seat_Frame extends JFrame{
	Container c=getContentPane();
    JPanel p = new JPanel(new FlowLayout());
    JPanel pb = new JPanel(new BorderLayout());
	JLabel la = new JLabel("좌석");	
	

	JLabel information = new JLabel("");
	
	JButton okbutton = new JButton("예약");

	
	JTable jt;
	JScrollPane sp;
	DBManager db = new DBManager();
	
	private int myrow;
	private int mycol;
	
	seat_Frame(String cust_id, String schedule_id, int movie_id, String room_id, int row, String table_info){
		ArrayList<ArrayList<String>> seat_datas = new ArrayList<ArrayList<String>>();

		setTitle("좌석정보");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setLayout(new BorderLayout(0,30));
		setLocation(500,200);
		information.setSize(600, 20);
		information.setHorizontalAlignment(JLabel.CENTER);
		
		p.add(la);
		p.setBackground(Color.yellow);
		
		
		c.add(okbutton, BorderLayout.SOUTH);
		c.add(information, BorderLayout.CENTER);
		c.add(p, BorderLayout.NORTH);
		
		okbutton.addActionListener(event -> {
			String temp = (String)jt.getValueAt(myrow, 2);
			if(temp.equals("N")) {
				information.setText("이미 예약된 좌석은 예약할 수 없습니다.");
			}
			else {
				temp = (String)jt.getValueAt(myrow, 0); //좌석번호
				JFrame payFrame = new payFrame(cust_id, schedule_id, movie_id, room_id, temp);
				setVisible(false);
			}
			
		});
		
		seat_datas=db.seat_show3(row,table_info);

		String[] seats_columns={"좌석번호", "상영관번호", "사용여부"};
		String [][]seat_data_array= new String[seat_datas.size()][seats_columns.length];

		for(int k=0; k<seat_datas.size(); k++) {
			for(int i=0; i<seats_columns.length; i++) {
				seat_data_array[k][i]=seat_datas.get(k).get(i);
			}
		}
		
		jt = new JTable(seat_data_array, seats_columns);
		sp = new JScrollPane(jt);
		
		
		pb.add(sp, BorderLayout.NORTH);
		pb.add(information, BorderLayout.SOUTH);
		c.add(pb, BorderLayout.CENTER);
		
		jt.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				JTable table = (JTable)e.getSource();
				myrow = table.getSelectedRow();
				mycol = table.getSelectedColumn();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	
			
		});
		
		c.setVisible(true);
		setSize(600,600);
		setVisible(true);
		
	}
}

class payFrame extends JFrame {
	Container page = getContentPane();
	private JLabel information = new JLabel("결제 수단을 선택해주세요.");
	private JRadioButton radio1 = new JRadioButton("현금");
	private JRadioButton radio2 = new JRadioButton("카드");
	
	private ButtonGroup group1 = new ButtonGroup();
	
	private JRadioButton radio3 = new JRadioButton("지금 결제");
	private JRadioButton radio4 = new JRadioButton("나중에 결제");
	
	private ButtonGroup group2 = new ButtonGroup();
	
	private JCheckBox check1 = new JCheckBox("멤버십 있음");
	private JCheckBox check2 = new JCheckBox("티켓 발급");
	
	
	private JButton okbutton = new JButton();
	private ImageIcon okImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/ok.png"));
	
	DBManager db = new DBManager();
	
	payFrame(String cust_id, String schedule_id, int movie_id, String room_id, String seat_id){
		setTitle("결제수단 선택");
		setLocationRelativeTo(null);
		setLayout(null);
		
		radio1.setBounds(20, 50, 100, 20);
		radio2.setBounds(140, 50, 100, 20);
		
		group1.add(radio1);
		group1.add(radio2);
		
		radio3.setBounds(20, 90, 100, 20);
		radio4.setBounds(140, 90, 100, 20);
		
		group2.add(radio3);
		group2.add(radio4);
		
		check1.setBounds(20, 130, 100, 20);
		check2.setBounds(140, 130, 100, 20);
		
		page.add(radio1);
		page.add(radio2);
		page.add(radio3);
		page.add(radio4);
		page.add(check1);
		page.add(check2);
		
		okImg = changeSize(okImg);
		
		information.setBounds(20, 20, 200, 20);
		okbutton.setBounds(250, 65, 70, 70);
		okbutton.setIcon(okImg);
		okbutton.setBackground(Color.white);
		okbutton.addMouseListener(new MouseHandler());
		
		okbutton.addActionListener(event -> {
			String way = null;
			String now = null;
			boolean membership = false;
			boolean ticket = false;
			if(group1.getSelection()==null || group2.getSelection() == null)
				information.setText("결제 수단과 시간을 선택해주세요.");
			
			else {
				if(radio1.isSelected()) way = radio1.getText();
				else way = radio2.getText();
				
				if(radio3.isSelected()) now = "Y";
				else now = "N";
				
				if(check1.isSelected()) membership = true;
				if(check2.isSelected()) ticket = true;
				db.Reservation(cust_id, schedule_id, movie_id, room_id, seat_id, way, now, membership, ticket);
				
				new sucessFrame(Integer.decode(cust_id).intValue());
				setVisible(false);
			}
			
		});
		
		
		page.add(information);
		page.add(okbutton);
		
		page.setVisible(true);
		
		setSize(350, 210);
		setVisible(true);
		
		
	}
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
	
}


class reservation_Frame extends JFrame{
	Container c=getContentPane();
    JPanel p = new JPanel(new FlowLayout());
    JPanel pa = new JPanel(new FlowLayout());
    JPanel pb = new JPanel(new FlowLayout());
    JButton btn_delete = new JButton("삭제");
    JButton btn_change = new JButton("변경");
	JLabel la = new JLabel("예매한 영화 정보");
	JLabel information = new JLabel("원하는 예매한 영화 정보를 보려면 테이블을 클릭하세요.");
	JTable jt;
	JScrollPane sp;
	String id;
	DBManager db = new DBManager();
	ArrayList<String> reservationid;
	
	reservation_Frame(String cust_id){
		id = cust_id;
		setTitle("예매한영화정보");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setLayout(new BorderLayout(0,30));
		setLocation(500,200);
		//label.setBounds(200,20,30,20);

		p.add(la);
		p.setBackground(Color.yellow);
		c.add(p, BorderLayout.NORTH);
		information.setHorizontalAlignment(JLabel.CENTER);
		c.add(information, BorderLayout.SOUTH);
		
		addWindowListener(new WindowHandler());
			
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
		String[] columns={"영화명", "상영일","상영관 번호","좌석 번호","판매가격","예매내역 삭제","예매내역 변경"};
		reservationid = db.getReservationID(cust_id);
		datas = db.reserInfo(cust_id);
		
		String [][]data_array= new String[datas.size()][7];
		for(int k=0; k<datas.size(); k++) {
			for(int i=0; i<5; i++) {
				data_array[k][i]=datas.get(k).get(i);
			}
			data_array[k][5]="";
			data_array[k][6]="";
		}
		

		jt = new JTable(data_array, columns);
		sp = new JScrollPane(jt);
		jt.getColumnModel().getColumn(5).setCellRenderer(new btn_cell(jt, cust_id));
		jt.getColumnModel().getColumn(5).setCellEditor(new btn_cell(jt, cust_id));
		jt.getColumnModel().getColumn(6).setCellRenderer(new btn_cell2(jt, cust_id));
		jt.getColumnModel().getColumn(6).setCellEditor(new btn_cell2(jt, cust_id));
		jt.addMouseListener(new MouseHandler2());
		c.add(sp);
		

		
		
		setSize(700,600);
		setVisible(true);
		
	}
	
	class MouseHandler2 implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			Object src = e.getSource();
			Object room_id = null;
			Object seat_id = null;
			if(src==jt) {
				int row = jt.getSelectedRow();
				int col = jt.getSelectedColumn();
				room_id = jt.getValueAt(row, 2);
				seat_id = jt.getValueAt(row, 3);
				new infoFrame(db.getticketinfo((String)room_id , (String)seat_id));
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class btn_cell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		JButton jb;
		public btn_cell(JTable jt, String cust_id) {
			jb=new JButton("삭제");
			jb.addActionListener(e->{
				int row=jt.getSelectedRow();
				String movie_name=(String)jt.getValueAt(row, 0);
				String room_id =(String)jt.getValueAt(row, 2);
				String seat_id =(String)jt.getValueAt(row, 3);
				
				db.delete(reservationid.get(row), movie_name, room_id, seat_id);
			
				setVisible(false);
				new sucessFrame();
			});
		}
		 @Override
	        public Object getCellEditorValue() {
	            return null;
	        }
	 
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	                int row, int column) {
	            return jb;
	        }
	 
	        @Override
	        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
	                int column) {
	            return jb;
	        }
	}
	class btn_cell2 extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		JButton jb;
		public btn_cell2(JTable jt, String cust_id) {
			jb=new JButton("변경");
			jb.addActionListener(e->{
				int row=jt.getSelectedRow();
				int cel=0;
				String movie_name=(String)jt.getValueAt(row, cel);
				
				JFrame change_reserFrame = new selectElement(cust_id,movie_name,reservationid.get(row));
				change_reserFrame.setVisible(true);
				
				setVisible(false);
			});
		}
		 @Override
	        public Object getCellEditorValue() {
	            return null;
	        }
	 
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	                int row, int column) {
	            return jb;
	        }
	 
	        @Override
	        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
	                int column) {
	            return jb;
	        }
	}
	class WindowHandler implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			JFrame customerFrame = new customerFrame(id);
			setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}

class infoFrame extends JFrame {
	Container page = getContentPane();
	
	private String[][] data1; //schedule data
	private String[][] data2; //room data
	private String[][] data3; //ticket data
	
	private String[] row1 = new String[] {"상영번호", "영화번호", "상영관번호", "상영일자", "상영요일", "상영횟수", "상영시간"};
	private String[] row2 = new String[] {"상영관번호", "좌석번호", "사용가능"};
	private String[] row3 = new String[] {"티켓번호", "상영번호", "상영관번호", "좌석번호", "예약번호", "티켓발급여부", "표준금액", "할인금액"};
	
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	
	JLabel label1 = new JLabel("상영일정");
	JLabel label2 = new JLabel("상영관");
	JLabel label3 = new JLabel("티켓");
	
	
	JTable schedule;
	JTable room;
	JTable ticket;
	
	JScrollPane scrollpane1;
	JScrollPane scrollpane2;
	JScrollPane scrollpane3;
	
	infoFrame(String[][] data){
		
		setTitle("예매 관련 정보");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(500,200);
		setLayout(null);
		page.setLayout(null);
		
		panel1.setPreferredSize(new Dimension(800, 40));
		panel2.setPreferredSize(new Dimension(800, 40));
		panel3.setPreferredSize(new Dimension(800, 40));
		
		panel1.setLocation(50, 20);
		panel2.setLocation(50, 120);
		panel3.setLocation(50, 220);
		
		
		data1 = new String[1][];
		data2 = new String[1][];
		data3 = new String[1][];
		
		data1[0] = data[0];
		data2[0] = data[1];
		data3[0] = data[2];
		
		schedule = new JTable(data1, row1);
		room = new JTable(data2, row2);
		ticket = new JTable(data3, row3);
		
		panel1.setBounds(0, 10, 800, 15);
		panel2.setBounds(0, 50, 800, 15);
		panel3.setBounds(0, 90, 800, 15);
		
		label1.setBounds(5, 20, 50, 20);
		label2.setBounds(5, 70, 50, 20);
		label3.setBounds(5, 130, 50, 20);
		
		
		scrollpane1 = new JScrollPane(schedule);
		scrollpane2 = new JScrollPane(room);
		scrollpane3 = new JScrollPane(ticket);
		
		scrollpane1.setBounds(60, 10, 800, 60);
		scrollpane2.setBounds(60, 70, 800, 60);
		scrollpane3.setBounds(60, 130, 800, 60);
		
		panel1.add(scrollpane1);
		panel2.add(scrollpane2);
		panel3.add(scrollpane3);
		
		scrollpane1.add(label1);
		scrollpane2.add(label2);
		scrollpane3.add(label3);
		
		page.add(scrollpane1);
		page.add(scrollpane2);
		page.add(scrollpane3);
		
		page.add(label1);
		page.add(label2);
		page.add(label3);
	
		page.setVisible(true);
		
		
		setSize(900,240);
		setVisible(true);

	}
}
class sucessFrame extends JFrame {
	Container page = getContentPane();
	JButton yes = new JButton();
	JButton no = new JButton();
	
	JLabel label = new JLabel("완료되었습니다! 감사합니다.");
	JLabel goinit = new JLabel("초기화면으로 가시겠습니까?");
	JLabel exit = new JLabel("프로그램을 종료합니다.");
	
	private ImageIcon yesImg = new ImageIcon(mainFrame.class.getResource("/teamproject/img/ok.png"));
	private ImageIcon noImg = new ImageIcon(mainFrame.class.getResource("/teamproject/img/no.png"));
	
	ActionListener my = new MyActionListener();
	
	sucessFrame(){
		setLayout(null);
		setTitle("감사합니다");
		yesImg = changeSize(yesImg);
		noImg = changeSize(noImg);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		yes.setBackground(Color.white);
		no.setBackground(Color.white);
		
		yes.addMouseListener(new MouseHandler());
		no.addMouseListener(new MouseHandler());
		
		yes.setIcon(yesImg);
		no.setIcon(noImg);
		
		
		label.setBounds(20, 20, 200, 20);
		goinit.setBounds(20, 60, 200, 20);
		
		yes.setBounds(230, 30, 50, 50);
		no.setBounds(290, 30, 50, 50);
		
		
		page.add(goinit);
		page.add(label);
		page.add(no);
		page.add(yes);
		
		page.setVisible(true);
		
		yes.addActionListener(my);
		
		no.addActionListener(event -> {
			setVisible(false);
		});
		
		
		setSize(380, 150);
		setVisible(true);
		
	}
	
	sucessFrame(int id){
		this();
		
		yes.removeActionListener(my);
		yes.addActionListener(event -> {
			new customerFrame(Integer.toString(id));
			setVisible(false);
		});
		
	}
	
	sucessFrame(String str){
		
		this();
		setLayout(null);
		setTitle("감사합니다");
		
		yes.removeActionListener(my);
		goinit.setVisible(false);
		no.setVisible(false);
		
		exit.setBounds(20, 60, 200, 20);
		page.add(exit);
		
		yes.addActionListener(event -> {
			System.exit(0);
			setVisible(false);
		});
		
		setSize(320,150);
		
	}
	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new mainFrame();
			setVisible(false);
		}
		
	}
	
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(40,40,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
}

class selectElement extends JFrame {
	Container c=getContentPane();
	JButton another_movie = new JButton();
	JButton another_schedule = new JButton();
	
	private String id;
	private ImageIcon movieImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/movie.png"));
	private ImageIcon scheduleImg = new ImageIcon(teamproject.class.getResource("/teamproject/img/schedule.png"));
	
	JLabel movie = new JLabel("영화 변경");
	JLabel schedule = new JLabel("상영 일정 변경");
	DBManager db = new DBManager();
	
	

	public selectElement(String cust_id, String movie_name, String reservation_id) {
		id = cust_id;
		setTitle("선택하세요.");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		c.setLayout(null);
		
		movieImg = changeSize(movieImg);
		scheduleImg = changeSize(scheduleImg);
		
		another_movie.setBackground(Color.white);
		another_schedule.setBackground(Color.white);
		
		
		another_movie.setIcon(movieImg);
		another_schedule.setIcon(scheduleImg);
		
		c.add(another_movie);
		c.add(another_schedule);
		c.add(movie);
		c.add(schedule);
		
		movie.setHorizontalAlignment(JLabel.CENTER);
		schedule.setHorizontalAlignment(JLabel.CENTER);
		
		another_movie.addMouseListener(new MouseHandler());
		another_schedule.addMouseListener(new MouseHandler());
	    
		another_movie.setBounds(20, 25, 100, 100);
		another_schedule.setBounds(170, 25, 100, 100);
		movie.setBounds(20, 135, 100, 20);
		schedule.setBounds(170, 135, 100, 20);
	    
	    c.setVisible(true);
	    
	    another_movie.addActionListener(event -> {
	    	setVisible(false);
	    	JFrame movie_change = new movieChangeFrame(cust_id,movie_name,reservation_id);
	    	movie_change.setVisible(true);
	    });
	    another_schedule.addActionListener(event -> {
	    	setVisible(false);
	    	JFrame schedule_change = new scheduleChangeFrame(cust_id,movie_name,reservation_id);
	    	schedule_change.setVisible(true);
	    });
	    setSize(300,200);
	    setVisible(true);
	    
	    addWindowListener(new windowListener());
	    
	}
	public ImageIcon changeSize(ImageIcon icon) {
		Image temp = icon.getImage();
		temp = temp.getScaledInstance(80,80,java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		return icon;
	}
	
	class windowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			new reservation_Frame(id);
			setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
class movieChangeFrame extends JFrame{
	Container c=getContentPane();
	JTable jt;
	JScrollPane sp;
	DBManager db = new DBManager();
	String id;
	String movie_name;
	String reservationid;
	
	public movieChangeFrame(String cust_id, String moviename, String reservation_id){
		id = cust_id;
		movie_name = moviename;
		reservationid = reservation_id;
		
		String [][] data_array;
		String[] columns={"영화 번호", "영화 제목","상영 시간","상영 등급","감독명","배우명","장르","영화 소개","개봉일","변경"};				

		setTitle("영화정보");
	//	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setLayout(new BorderLayout(0,30));
		setLocation(500,200);
		//label.setBounds(200,20,30,20);
		
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>();
		datas = db.movieTable();
		
		data_array= new String[datas.size()][10];
		for(int k=0; k<datas.size(); k++) {
			for(int i=0; i<9; i++) {
				data_array[k][i]=datas.get(k).get(i);
			}
			data_array[k][9]="";
		}
		jt = new JTable(data_array, columns);

		
	
		sp = new JScrollPane(jt);
		c.add(sp,BorderLayout.CENTER);
		jt.getColumnModel().getColumn(9).setCellRenderer(new btn_cell(cust_id));
		jt.getColumnModel().getColumn(9).setCellEditor(new btn_cell(cust_id));
		
		
		c.add(sp);
		
		addWindowListener(new windowlistener());
			
	
		c.setVisible(true);

		setSize(700,800);
		setVisible(true);
		
		
	}
	class btn_cell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		JButton jb;
		public btn_cell(String cust_id) {
			jb=new JButton("변경");
			jb.addActionListener(e->{
				int row=jt.getSelectedRow();
				int cel=1;
				String table_info=(String)jt.getValueAt(row, cel);
				JFrame movie_scheduleFrame = new movie_scheduleFrame(cust_id, table_info, reservationid);
				setVisible(false);
			});
		}
		 @Override
	        public Object getCellEditorValue() {
	            return null;
	        }
	 
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	                int row, int column) {
	            return jb;
	        }
	 
	        @Override
	        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
	                int column) {
	            return jb;
	        }
	}
	class windowlistener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			new selectElement(id, movie_name, reservationid);
			setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
class movie_scheduleFrame extends JFrame{
	Container c=getContentPane();
	JTable jt;
	JScrollPane sp;
	DBManager db = new DBManager();
	String id;
	String movie_name;
	String reservationid;
	
	public movie_scheduleFrame(String cust_id, String moviename, String reservation_id) {
		id = cust_id;
		movie_name = moviename;
		reservationid = reservation_id;
		setTitle("예매가능한 시간표");
		setLocation(500,200);
		ArrayList<ArrayList<String>> schedule_datas = new ArrayList<ArrayList<String>>();
		schedule_datas = db.schedule_reserInfo_3(moviename);
		String[] schedule_columns={"상영일정번호", "영화번호","상영관 번호","상영시작일","상영요일","상영회차","상영시작시간", "변경"};

		String [][]schedule_data_array= new String[schedule_datas.size()][schedule_columns.length];
		
		for(int k=0; k<schedule_datas.size(); k++) {
			for(int i=0; i<schedule_columns.length-1; i++) {
				schedule_data_array[k][i]=schedule_datas.get(k).get(i);
			}
			schedule_data_array[k][7]="";
		}
		
		jt = new JTable(schedule_data_array, schedule_columns);
		sp = new JScrollPane(jt); 
		
		jt.getColumnModel().getColumn(7).setCellRenderer(new btn_cell(cust_id));
		jt.getColumnModel().getColumn(7).setCellEditor(new btn_cell(cust_id));
		
		//addWindowListener(new windowlistener());
		
		c.add(sp);
		
		setSize(600,500);
		setVisible(true);
	}
	class btn_cell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		JButton jb;
		public btn_cell(String cust_id) {
			jb=new JButton("변경");
			jb.addActionListener(e->{
				int row=jt.getSelectedRow();
				int cel=2;
				String room_id=(String)jt.getValueAt(row, cel);
				String movie_id=(String)jt.getValueAt(row, 1);
				String schedule_id=(String)jt.getValueAt(row, 0);
				JFrame seat_show2Frame = new seat_show2Frame(cust_id, schedule_id, room_id, movie_id, reservationid);
				setVisible(false);
			});
		}
		 @Override
	        public Object getCellEditorValue() {
	            return null;
	        }
	 
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	                int row, int column) {
	            return jb;
	        }
	 
	        @Override
	        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
	                int column) {
	            return jb;
	        }
	}
	class windowlistener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			new movieChangeFrame(id, movie_name, reservationid);
			setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
class scheduleChangeFrame extends JFrame{
	Container c=getContentPane();
	JTable jt;
	JScrollPane sp;
	DBManager db = new DBManager();
	String id;
	String movie_name;
	String reservationid;
	public scheduleChangeFrame(String cust_id, String moviename, String reservation_id) {
		id = cust_id;
		movie_name = moviename;
		reservationid = reservation_id;
		
		setLocation(500, 200);
		ArrayList<ArrayList<String>> schedule_datas = new ArrayList<ArrayList<String>>();
		schedule_datas = db.schedule_reserInfo_3(moviename);
		//System.out.println(schedule_datas);
		String[] schedule_columns={"상영일정번호", "영화번호","상영관 번호","상영시작일","상영요일","상영회차","상영시작시간", "변경"};
		
		String [][]schedule_data_array= new String[schedule_datas.size()][schedule_columns.length];
		
		for(int k=0; k<schedule_datas.size(); k++) {
			for(int i=0; i<schedule_columns.length-1; i++) {
				schedule_data_array[k][i]=schedule_datas.get(k).get(i);
			}
			schedule_data_array[k][7]="";
		}
		
		jt = new JTable(schedule_data_array, schedule_columns);
		sp = new JScrollPane(jt);
		setLocationRelativeTo(null);
		jt.getColumnModel().getColumn(7).setCellRenderer(new btn_cell(cust_id));
		jt.getColumnModel().getColumn(7).setCellEditor(new btn_cell(cust_id));
		
		
		c.add(sp);
		
		addWindowListener(new windowlistener());
				
		setSize(600,500);
		setVisible(true);
	}
	class btn_cell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
		JButton jb;
		public btn_cell(String cust_id) {
			jb=new JButton("변경");
			jb.addActionListener(e->{
				int row=jt.getSelectedRow();
				int cel=2;
				String room_id=(String)jt.getValueAt(row, cel);
				String movie_id =(String)jt.getValueAt(row, 1);
				String schedule_id = (String)jt.getValueAt(row, 0);
				JFrame seat_show2Frame = new seat_show2Frame(cust_id, schedule_id, room_id, movie_id, reservationid);
				setVisible(false);
			});
		}
		 @Override
	        public Object getCellEditorValue() {
	            return null;
	        }
	 
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	                int row, int column) {
	            return jb;
	        }
	 
	        @Override
	        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
	                int column) {
	            return jb;
	        }
	}
	class windowlistener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			new selectElement(id, movie_name, reservationid);
			setVisible(false);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
class seat_show2Frame extends JFrame{
	
	Container c = getContentPane();
	JTable jt;
	JScrollPane sp;
    JPanel p = new JPanel(new FlowLayout());
    JPanel buttonpanel = new JPanel(new FlowLayout());
    JPanel pb = new JPanel(new FlowLayout());

	JLabel la = new JLabel("좌석");	
	JLabel label_yes = new JLabel("사용 가능한 좌석입니다");
	JLabel label_no = new JLabel("이미 예약된 좌석입니다");
	JLabel price ;

	JButton button = new JButton("선택");

	
	DBManager db = new DBManager();
	
	String id;
	String movie_name;
	String seat_id = null;
	
	public seat_show2Frame(String cust_id, String schedule_id, String room_id, String movie_id, String reservation_id) {
		id = cust_id;
		movie_name = null;
		
		setTitle("좌석선택");
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setLayout(new BorderLayout(0,30));
		setLocation(500,200);
	
		button.setBackground(Color.white);
		ArrayList<ArrayList<String>> seat_datas = new ArrayList<ArrayList<String>>();

		
		p.add(la);
		pb.add(label_yes);
		pb.add(label_no);
		p.setBackground(Color.yellow);
		c.add(p, BorderLayout.NORTH);
		
		button.setSize(200, 20);
		buttonpanel.add(button);
		c.add(buttonpanel, BorderLayout.SOUTH);
		
		label_no.setVisible(false);
		label_yes.setVisible(false);
		
		seat_datas=db.seat_show2(room_id);

		String[] seats_columns={"좌석번호", "상영관번호","사용여부"};
		String [][]seat_data_array= new String[seat_datas.size()][seats_columns.length];

		for(int k=0; k<seat_datas.size(); k++) {
			for(int i=0; i<seats_columns.length; i++) {
				seat_data_array[k][i]=seat_datas.get(k).get(i);
			}
		}
		
		jt = new JTable(seat_data_array, seats_columns);
		sp = new JScrollPane(jt);
		
		
		pb.add(sp);
		c.add(pb, BorderLayout.CENTER);
		
		pb.add(sp);
		c.add(pb, BorderLayout.CENTER);
		
		setSize(600,800);
		c.setVisible(true);
		setVisible(true);
		
		jt.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				int row;
				int cel=2;
				JTable jt=(JTable)e.getSource();
				row=jt.getSelectedRow();
				String table_info=(String)jt.getValueAt(row, cel);
				
				String room_id=(String)jt.getValueAt(row, 1);
				seat_id=(String)jt.getValueAt(row, 0);
				
				
				if(table_info.equals("Y")) {
					label_yes.setVisible(true);
					label_no.setVisible(false);
				}
				else if(table_info.equals("N")) {
					label_yes.setVisible(false);
					label_no.setVisible(true);
		
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		button.addActionListener(event -> {
			if(db.changeReservation(reservation_id, schedule_id, room_id, seat_id)) {
				JFrame successFrame = new sucessFrame(Integer.decode(cust_id).intValue());
				setVisible(false);
			}
			else{
				label_yes.setText("잘못된 접근입니다. 예약을 다시 확인");
			}
		});

	}
		
}



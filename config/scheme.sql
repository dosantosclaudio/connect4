-- SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS ranks;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cells;
DROP TABLE IF EXISTS boards;
-- SET FOREIGN_KEY_CHECKS=1;


--	Create users table
CREATE TABLE users(
    id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(60) UNIQUE NOT NULL,
    password VARCHAR (56) NOT NULL,
    first_name VARCHAR(56) DEFAULT NULL,
    last_name VARCHAR(56) DEFAULT NULL,
  CONSTRAINT users_pk PRIMARY KEY (id)
);


--	Creates ranks table
CREATE TABLE ranks(
	id INT NOT NULL AUTO_INCREMENT,
	won_games INT NOT NULL DEFAULT 0,
	tie_games INT NOT NULL DEFAULT 0,
	played_games INT NOT NULL DEFAULT 0,
	score FLOAT NOT NULL DEFAULT 0,           -- VER SI NOTACION es correcta   TIENE QUE SER CALCULADO PREGUNTAR COMO HACERlo
	user_id INT NOT NULL,
  CONSTRAINT ranks_pk PRIMARY KEY (id)
 );


--	Create games table
CREATE TABLE games(
	id INT NOT NULL AUTO_INCREMENT, 
	player1_id INT NOT NULL,
	player2_id INT NOT NULL,
	result_p1 ENUM('WIN','LOOSE','TIE'),
	init_date DATETIME , -- año-mes-dia horas:minutos:segundos 
	end_date DATETIME ,
  CONSTRAINT games_pk PRIMARY KEY (id)
); 


--	Create cells table
CREATE TABLE cells(
	id INT NOT NULL AUTO_INCREMENT,
	user_id INT DEFAULT NULL,
	board_id INT NOT NULL,
	row INT NOT NULL,
	col INT NOT NULL,
  CONSTRAINT cells_pk PRIMARY KEY (id)
);

--	Create boards table
CREATE TABLE boards(
	id INT NOT NULL AUTO_INCREMENT,
	game_id INT NOT NULL,
  CONSTRAINT boards_pk PRIMARY KEY (id)
);
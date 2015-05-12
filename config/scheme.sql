DROP TABLE IF EXISTS users;
CREATE TABLE users(
    id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(60) UNIQUE,
    first_name VARCHAR(56) DEFAULT NULL,
    last_name VARCHAR(56) DEFAULT NULL,
  CONSTRAINT users_pk PRIMARY KEY (id)
);

DROP TABLE IF EXISTS ranks;
CREATE TABLE ranks(
	id INT NOT NULL AUTO_INCREMENT,
	won_games INT NOT NULL DEFAULT 0,
	tie_games INT NOT NULL DEFAULT 0,
	played_games INT NOT NULL DEFAULT 0,
	score FLOAT NOT NULL,      -- VER SI NOTACION es correcta   TIENE QUE SER CALCULADO PREGUNTAR COMO HACERlo
	user_id INT NOT NULL,
  CONSTRAINT ranks_pk PRIMARY KEY (id),
  CONSTRAINT users_fk FOREIGN KEY (user_id) REFERENCES users(id)
 );


DROP TABLE IF EXISTS games;
CREATE TABLE games(
	id INT NOT NULL AUTO_INCREMENT, 
	player1 INT NOT NULL,
	player2 INT NOT NULL,
	result_p1 ENUM('WIN','LOOSE','TIE'),
	init_date DATETIME , -- año-mes-dia horas:minutos:segundos 
	end_date DATETIME ,
	CONSTRAINT games_pk PRIMARY KEY (id),
	CONSTRAINT player1_fk FOREIGN KEY (player1) REFERENCES users (id),
	CONSTRAINT player2_fk FOREIGN KEY (player2) REFERENCES users (id)
); 

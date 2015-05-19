package com.unrc.app;
import java.util.*;
import java.lang.Exception;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@BelongsToParents({ 
@BelongsTo(foreignKeyName="player1_id",parent=User.class), 
@BelongsTo(foreignKeyName="player2_id",parent=User.class) 
})

public class Game extends Model{ 	
	
	public Game(){

	}

	public Game(User p1,User p2){
		Game g = new Game();
		g.set("player1_id",p1.get("id"));
		g.set("player2_id", p2.get("id"));
		g.saveIt();
		Board b = new Board(g);
	}


	//	Insert chip on the board
	public void doMovement(User p){
		int column=-1;
		Board b =Board.findFirst("game_id = ?",this.get("id"));
		try{
			column = requestCol();
		}catch(Exception e){
			this.doMovement(p);
		}
		try{
			b.fillCell(p,column);
		}catch(BoardException f){
			switch (f.getCode()){
				case "000":
					System.out.println("Has been detected some problems in this aplication ");
					break;
				case "001":
					System.out.println(f.getMessage());
					this.doMovement(p);
					break;
				case "002":
					System.out.println(f.getMessage());
					this.doMovement(p);
					break;
			}
		}
	}

	private int requestCol() throws Exception{
		System.out.println("Put the number of the column to complete");
		Scanner s = new Scanner(System.in);
		int input= s.nextInt();
		return input;
	}

	// Down search.
	private int dSearch(User usr,int c,int r ){
		if (c<0 || c>6 || r<0 || r>5) {
			return 0;  // this place is not in the board.
		}else{
			// the place is valid in this board.
			List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")),c,r);
			Cell aux_1=aux.get(0);
			if (aux_1.get("user_id")==usr.get("id")) {
				return 1+dSearch(usr,c,r-1);
			}else{
				return 0;
			}
		}
	}

	// Right search.
	private int rSearch(User usr,int c,int r ){
		if (c<0 || c>6 || r<0 || r>5) {
			return 0;  // this place is not in the board.
		}else{
			// the place is valid in this board.
			List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")),c,r);
			Cell aux_1=aux.get(0);
			if (aux_1.get("user_id")==usr.get("id")) {
				return 1+rSearch(usr,c+1,r);
			}else{
				return 0;
			}
		}
	}

	// Left search.
	private int lSearch(User usr,int c,int r ){
		if (c<0 || c>6 || r<0 || r>5) {
			return 0;  // this place is not in the board.
		}else{
			// the place is valid in this board.
			List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")),c,r);
			Cell aux_1=aux.get(0);
			if (aux_1.get("user_id")==usr.get("id")) {
				return 1+lSearch(usr,c-1,r);
			}else{
				return 0;
			}
		}
	}

	// Up-right search.
	private int uRSearch(User usr,int c,int r ){
		if (c<0 || c>6 || r<0 || r>5) {
			return 0;  // this place is not in the board.
		}else{
			// the place is valid in this board.
			List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")),c,r);
			Cell aux_1=aux.get(0);
			if (aux_1.get("user_id")==usr.get("id")) {
				return 1+ uRSearch(usr,c+1,r+1);
			}else{
				return 0;
			}
		}
	}

	// Down-left search.
	private int dLSearch(User usr,int c,int r ){
		if (c<0 || c>6 || r<0 || r>5) {
			return 0;  // this place is not in the board.
		}else{
			// the place is valid in this board.
			List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")),c,r);
			Cell aux_1=aux.get(0);
			if (aux_1.get("user_id")==usr.get("id")) {
				return 1+dLSearch(usr,c-1,r-1);
			}else{
				return 0;
			}
		}
	}

	// Up-left search.
	private int uLSearch(User usr,int c,int r ){
		if (c<0 || c>6 || r<0 || r>5) {
			return 0;  // this place is not in the board.
		}else{
			// the place is valid in this board.
			List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")),c,r);
			Cell aux_1=aux.get(0);
			if (aux_1.get("user_id")==usr.get("id")) {
				return 1+uLSearch(usr,c+1,r-1);
			}else{
				return 0;
			}
		}
	}

	// Down-right search.
	private int dRSearch(User usr,int c,int r ){
		if (c<0 || c>6 || r<0 || r>5) {
			return 0;  // this place is not in the board.
		}else{
			// the place is valid in this board.
			List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")),c,r);
			Cell aux_1=aux.get(0);
			if (aux_1.get("user_id")==usr.get("id")) {
				return 1+dRSearch(usr,c-1,r+1);
			}else{
				return 0;
			}
		}
	}

	// Check if the user, usr,  won the game. 
	public boolean isWin(User usr,int c, int r){
		return 	(dSearch(usr,c,r-1)>=3) || 
				(rSearch(usr,c+1,r) + lSearch(usr,c-1,r)>=3) || 
				(uRSearch(usr,c+1,r+1) + dLSearch(usr,c-1,r-1)>=3) || 
				(uLSearch(usr,c-1,r+1) + dRSearch(usr,c+1,r-1)>=3);
	}
/*
	//Actualiza rank luego de que haya un ganador. Utiliza metodo de Rank
	public boolean updateRankWithWinner(User winner,User looser){

	}

	//Actualiza rank luego de que ha ocurrido un empate. Utiliza metodo de Rank.
	public boolean updateRankWithDraw(){

	}
*/
}
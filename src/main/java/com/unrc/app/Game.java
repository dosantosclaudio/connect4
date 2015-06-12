package com.unrc.app;
import java.util.*;
import java.lang.Exception;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@BelongsToParents({ 
@BelongsTo(foreignKeyName="player1_id",parent=User.class), 
@BelongsTo(foreignKeyName="player2_id",parent=User.class) 
})

public class Game extends Model{ 	
	private Board b;
	
	public Game(){
	}



	public Game(Pair<User,User> p){
		this.set("player1_id",p.getFst().get("id"));
		this.set("player2_id",p.getSnd().get("id"));
		this.set ("init_date",getDateMysql());
		this.saveIt();
		b= new Board(this);
	}

	public String toString2(){
		return this.getString("id");
	}


	public static String getDateMysql(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String reportDate = df.format(today);
		return reportDate;	
	}
	public int turnUser(){
		return b.counterCellNull();
	}
	public void saveGame(){

		b.saveBoard(this);
	}

	public void resumeGame(){
		b=new Board();
		b.updateBoard(this);
	}

	//	Insert chip on the board
	public Cell doMovement(User p,Integer col) throws BoardException{
	//String column="-1";
	//	Board b =Board.findFirst("game_id = ?", this.get("id"));
		
	/*	try{
			System.out.println("Plase "+p.get("first_name")+" "+p.get("last_name")+" insert a coin.");
			column = requestCol();
			if (column.equals("s") || column.equals("S") ){
				return null;									//If the user want to save the game.
			}
		}catch(Exception e){
			return doMovement(p);
		}*/
		/*try{*/
			return b.fillCellMemory(p,col);
		/*}catch(BoardException f){
			switch (f.getCode()){
				case "000":
					System.out.println("Has been detected some problems in this aplication ");
					c=null;
					break;
				case "001":
					System.out.println(f.getMessage());
					c=doMovement(p);
					break;
				case "002":
					System.out.println(f.getMessage());
					c=doMovement(p);
					break;
			}
		}*/
		
	}

	private String requestCol() throws Exception{
		System.out.print("Put the number of the column to complete or press 'S' to save the game: ");
		Scanner s = new Scanner(System.in);
		String input= s.nextLine();
		return input;
	}

	// Down search.
	private int dSearch(User usr,int c,int r ){
		if (c<0 || c>6 || r<0 || r>5) {
			return 0;  // this place is not in the board.
		}else{
			System.out.println("DDDDDDDD");
			// the place is valid in this board.
			//Board b=findFirst("game_id",this.get("id"));
			Cell [][]  board=b.getBoard();
			//List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")).get("id"),c,r);
			Cell aux_1=board [r] [c];
			if (aux_1.get("user_id").equals(usr.get("id"))) {
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
		//	List<Cell> aux= Cell.where("board_id = ? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")).get("id"),c,r);
		//	Board b=findFirst("game_id",this.get("id"));
			Cell [][]  board=b.getBoard();
			Cell aux_1=board [r] [c];
			if ((aux_1.get("user_id")!=null)&&(aux_1.get("user_id").equals(usr.get("id")))) {
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
			//List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")).get("id"),c,r);
			//Board b=findFirst("game_id",this.get("id"));
			Cell [][]  board=b.getBoard();
			Cell aux_1=board [r] [c];
			if (((aux_1.get("user_id")!=null)&&aux_1.get("user_id").equals((usr.get("id"))))) {
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
		//	List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")).get("id"),c,r);
		//	Board b=findFirst("game_id",this.get("id"));
			Cell [][]  board=b.getBoard();
			Cell aux_1=board [r] [c];
			if (((aux_1.get("user_id")!=null)&&aux_1.get("user_id").equals(usr.get("id")))) {
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
			//List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")).get("id"),c,r);
		//	Board b=findFirst("game_id",this.get("id"));
			Cell [][]  board=b.getBoard();
			Cell aux_1=board [r] [c];
			if (((aux_1.get("user_id")!=null)&&aux_1.get("user_id").equals(usr.get("id")))) {
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
			//List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")).get("id"),c,r);
		//	Board b=findFirst("game_id",this.get("id"));
			Cell [][]  board=b.getBoard();
			Cell aux_1=board [r] [c];
			if (((aux_1.get("user_id")!=null)&&aux_1.get("user_id").equals(usr.get("id")))) {
				return 1+uLSearch(usr,c-1,r+1);
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
		//	List<Cell> aux= Cell.where("board_id=? and col = ? and row = ?",Board.findFirst("game_id = ?",this.get("id")).get("id"),c,r);
		//	Board b=findFirst("game_id",this.get("id"));
			Cell [][]  board=b.getBoard();
			Cell aux_1=board [r] [c];
			if (((aux_1.get("user_id")!=null)&&aux_1.get("user_id").equals(usr.get("id"))))	 {
				return 1+dRSearch(usr,c+1,r-1);
			}else{
				return 0;
			}
		}
	}

	// Check if the user, usr,  won the game. 
	public boolean thereIsAWinner(User usr,Cell a){
		System.out.println("AAAAA");
		int c= (int) a.get("col");
		int r= (int) a.get("row");
		
		return 	(dSearch(usr,c,r-1)>=3) || 
				(rSearch(usr,c+1,r) + lSearch(usr,c-1,r)>=3) || 
				(uRSearch(usr,c+1,r+1) + dLSearch(usr,c-1,r-1)>=3) || 
				(uLSearch(usr,c-1,r+1) + dRSearch(usr,c+1,r-1)>=3);
	}

	// Update statistics of the 
	public void updateRankWithWinner(User winner,User looser){
		Rank.userWin(winner);
		Rank.userLose(looser);
	}

	//Actualiza rank luego de que ha ocurrido un empate. Utiliza metodo de Rank.
	public void updateRankWithDraw(User u1, User u2){
		Rank.userDraw(u1);
		Rank.userDraw(u2);
	}

	public boolean full(){
	//	Board b= Board.findFirst("game_id= ?",this.get("id"));
		return b.fullBoard();
	}

	public boolean fullCol(Integer i){
		return b.fullCol(i);
	}
/*
	public void printBoardOnScreen(Pair<User,User> players){
	//	Board b= Board.findFirst("game_id= ?",this.get("id"));
		b.printBoard(players);
	}*/

	public Board getBoard(){
		return this.b;
	}
}
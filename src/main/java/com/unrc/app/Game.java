package com.unrc.app;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.lang.Exception;

import java.util.Calendar;
import java.util.Date;
import java.util.*;

@BelongsToParents({ 
@BelongsTo(foreignKeyName="player1_id",parent=User.class), 
@BelongsTo(foreignKeyName="player2_id",parent=User.class) 
})

public class Game extends Model{ 	
	
	Board b;
	
	public Game(){

	}
	// Constructor2
	public Game(Pair<User,User> p){
		this.set("player1_id",p.getFst().get("id"));
		if (p.getSnd()!=null)
			this.set("player2_id",p.getSnd().get("id"));
		else{
			this.set("player2_id",0);
		}
		this.set("channel",0);
		this.set ("init_date",getDateMysql());
		this.saveIt();
		b= new Board(this);
	}
	// Retorna un string con los jugadores de un juego
	public String toStringPlayers(){
		User u1=User.findFirst("id=?", this.get("player1_id"));
		User u2=User.findFirst("id=?", this.get("player2_id"));
		
		return (u1.toString2() + "   VS   " + u2.toString2());
	}

	public String toStringPlayer1(){
		User u1=User.findFirst("id=?", this.get("player1_id"));
		return u1.toString2();

	}

	public String toStringChannel(){
		return this.getString("channel");
	}
	// Retorna el codigo del juego
	public String toString2(){
		return this.getString("id");
	}

	// Retorna fecha actual
	public static String getDateMysql(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = Calendar.getInstance().getTime();
		String reportDate = df.format(today);
		return reportDate;	
	}

	// Retorna True si es el turno del jugador 1 
	//retorna False si es el turno del jugador 2
	public boolean turnUser(){
		return (b.counterCellNull() %2==0);
	}

	// Carga el tablero de acuerdo al juego
	public void resumeGame(){
		b=new Board();
		b.updateBoard(this);
	}

	//	Inserta una ficha en el tablero
	public Cell doMovement(User p,Integer col) throws BoardException{
		return b.fillCellMemory(p,col);
	}

	// 
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
			// the place is valid in this board.
			Cell [][]  board=b.getBoard();
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
		int c= (int) a.get("col");
		int r= (int) a.get("row");
		
		return 	(dSearch(usr,c,r-1)>=3) || 
				(rSearch(usr,c+1,r) + lSearch(usr,c-1,r)>=3) || 
				(uRSearch(usr,c+1,r+1) + dLSearch(usr,c-1,r-1)>=3) || 
				(uLSearch(usr,c-1,r+1) + dRSearch(usr,c+1,r-1)>=3);
	}

	// Update statistics of the 
	public void updateRankWithWinner(User winner,User looser){
		winner.win();
		looser.loose();
	}

	//Actualiza rank luego de que ha ocurrido un empate. Utiliza metodo de Rank.
	public void updateRankWithDraw(User u1, User u2){
		u1.tie();
		u2.tie();
	}

	public boolean full(){
		return b.fullBoard();
	}

	public boolean fullCol(Integer i){
		return b.fullCol(i);
	}
	public Board getBoard(){
		return this.b;
	}
}
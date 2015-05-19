package com.unrc.app;
import java.util.*;
import java.lang.Exception;
import org.javalite.activejdbc.Model;

public class Board extends Model{

	public Board(){}

	public Board(Game g){
		Board b =new Board();
		b.set("game_id",g.get("id"));
		b.saveIt();
		for (int i=0; i<7; i++) {
			for (int j=0; j<6; j++) {
				Cell aux= new Cell();
				aux.set("board_id",b.get("id"));
				aux.set("user_id",null);
				aux.set("col",i);
				aux.set("row",j);
				aux.saveIt();
			}
		}
	}
	
	// Incert the chip in colum, if it is not full
	public void fillCell(User u, int colum) throws BoardException{
		if (colum<0 || colum>=7){
			throw new BoardException("Invalid column value","001");
		}else{
			int i=0;
			List<Cell> aux= Cell.where("board_id = ? and col = ?",this.get("game_id"), colum).orderBy("row");
			while (i<6 && aux.get(i).get("user_id")!=null){
				i++;
			}
			if (i>=6){
				throw new BoardException("This column is complete, insert your chip in another place","002");
			}else{
				if (aux.get(i).get("user_id")!=null){
					aux.get(i).set("user_id",u.get("user_id")).saveIt(); 
				}else{
					throw new BoardException("Fatal error","000");
				}
			}
		}
	}
	
/*	public fillCellP2(User u,int colum){
		if (colum<0 || colum>=7){
			//exepcion el valor de las columnas es incorrecto
		}else{
			int i=0;
			while ( i<6 && this.board[colum][i].getCellOwner()!=null){
				i++;
			}
			if (i>=6){
				//exepcion columna llena
			}else{
				if (this.board[colum][i].getCellOwner()==null){
					this.board[colum][i]=new Cell(u,'0');
				}else{
					//hace cualquier cosa
				}
			}
		}
	}
*/

	//	Initializes the board, all the cells are empty.
	public void emptyBoard(){
		for (int i=0; i<7; i++) {
			for (int j=0; j<6; j++) {
				Cell aux= Cell.createIt("board_id",this.get("id"),"row",j,"col",i);
				aux.saveIt();
			}
		}
	}

	//	Show the board on the screen
	public void printBoard(){
		User p1= User.findFirst("id = ?", Board.findFirst("id = ?",this.get("game_id")).get("player1_id"));
		User p2= User.findFirst("id = ?", Board.findFirst("id = ?",this.get("game_id")).get("player2_id"));
		for (int i=0; i<6; i++) {
			System.out.println("");
			for (int j=6; j>=0; j--) {
				List<Cell> aux= Cell.where("board_id = ? and col = ? and row = ?",this.get("id"),i,j);
				Cell c = aux.get(0);
				if (p1.get("id")== c.get("user_id")) {
					System.out.print("X ");
				}else{
					if (p2.get("id")== c.get("user_id")) {
						System.out.print("0 ");
					}else{
						System.out.print("- ");
					}
				}
			}
		}
	}

	// Return true when the board is full
	public Boolean fullBoard(){
		List<Cell> c=Cell.where("board_id = ? and user_id = ?",this.get("id"),null);
		return c.isEmpty();
	}
}

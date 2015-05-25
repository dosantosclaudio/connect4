package com.unrc.app;
import java.util.*;
import java.lang.Exception;
import org.javalite.activejdbc.Model;

public class Board extends Model{
	public Board(){}

	public Board(Game g){
		
		this.set("game_id",g.get("id"));
		this.saveIt();
		for (int i=0; i<7; i++) {
			for (int j=0; j<6; j++) {
				Cell aux= new Cell();
				aux.set("board_id",this.get("id"));
				aux.set("user_id",null);
				aux.set("col",i);
				aux.set("row",j);
				aux.saveIt();
			}
		}
	}
	
	// Incert the chip in colum, if it is not full
	// Return the cell in whitch the chip was inserted  
	public Cell fillCell(User u, int colum) throws BoardException{
		int i=0;
		List<Cell> aux=null; 

		if (colum<0 || colum>=7){
			throw new BoardException("Invalid column value","001");
		}else{
			aux= Cell.where("board_id = ? and col = ?",this.get("id"), colum).orderBy("row");
			while (i<6 && aux.get(i).get("user_id")!=null){
				i++;
			}
			
			// Muy ineficiente.

			if (i>=6){
				throw new BoardException("This column is complete, insert your chip in another place","002");
			}else{
				if (aux.get(i).get("user_id")==null){
					aux.get(i).set("user_id",u.get("id")).saveIt(); 
				}else{
					throw new BoardException("Fatal error","000");
				}
			}
		}
		return aux.get(i);
	}

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
	public void printBoard(Pair<User,User> players){
		Menu.clearConsole();
		char[][] o = new char[6][7];
		for (int i=0; i<6; i++) {
			for (int j=6; j>=0; j--){
				List<Cell> aux= Cell.where("board_id = ? and col = ? and row = ?",this.get("id"),j,i);
				Cell c = aux.get(0);
				if (players.getFst().get("id")== c.get("user_id")) {
					o[i][j]='X';
				}else{
					if (players.getSnd().get("id")== c.get("user_id")) {
						o[i][j]='0';
					}else{
						o[i][j]='-';
					}
				}
			}
		}
		for (int i=0; i<6; i++) {
			System.out.println("");
			for (int j=6; j>=0; j--){
				System.out.print(o[i][j]);
			}
		}
	}

	// Return true when the board is full
	public Boolean fullBoard(){
		List<Cell> c=Cell.where("board_id = ? and user_id is null",this.get("id"));
		System.out.println(c.size());
		return c.isEmpty();
	}
}

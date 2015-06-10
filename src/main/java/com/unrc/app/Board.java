package com.unrc.app;
import java.util.*;
import java.lang.Exception;
import org.javalite.activejdbc.Model;

public class Board extends Model{
	
	private Cell [] [] boardM;	

	public Board(){
		boardM=new Cell[6][7];
	}
	

	public Board(Game g){
		boardM=new Cell[6] [7];
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
				boardM [j] [i] = aux;


			}
		}
		System.out.println("Acaaaa");
		System.out.println(boardM [2] [2]);
	}
	


	public void updateBoard(Game g){
		Board b=Board.findFirst("game_id=?",g.get("id"));
		List<Cell> aux=Cell.where("board_id=? order by col,row", b.get("id") );
		int count=0;
		for(int i=0;i<7;i++){
			for (int j=0;j<6;j++){
				Cell a=aux.get(count);
				boardM[j][i]=a;
				count++;
			}
		}


	}

	public int counterCellNull(){
		int count=0;
		for(int i=0;i<7;i++){
			for (int j=0;j<6;j++){
				if (boardM[j][i].get("user_id")!=null){
					count++;
				}
			}
		}
		return count;
	}
	public void saveBoard(Game g){
		Board board=Board.findFirst("game_id=?",g.get("id"));
		List<Cell> aux=Cell.where("board_id=? order by col,row",board.get("id"));
		int count=0;
		for(int i=0;i<7;i++){
			for (int j=0;j<6;j++){
				Cell a=aux.get(count);
				Cell b=boardM[j][i];
				count++;
				a.set("user_id",b.get("user_id"));
				a.saveIt();
			}
		}

	}
	public Cell fillCellMemory(User u,int colum) throws BoardException{
		int i=0;
		if (colum<0 || colum>=7){
			throw new BoardException("Invalid column value","001");
		}else{
			while ((i<6) && (boardM[i][colum].get("user_id")!=null)){
				i++;
			}
			if (i>=6){
				throw new BoardException("This column is complete, insert your chip in another place","002");
			}else{
				if (boardM[i][colum].get("user_id")==null){
					boardM[i][colum].set("user_id",u.get("id")).saveIt();
				}
				else{
					throw new BoardException("Fatal error","000");
				}
			}
		}
		return boardM[i][colum];
	
	}




	// Incert the chip in colum, if it is not full
/*	// Return the cell in whitch the chip was inserted  
	public void fillCell(User u, int colum) throws BoardException{
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
		//return aux.get(i);
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
	public void printBoard(Pair<User,User> players){
		Menu.clearConsole();
		char[][] o = new char[6][7];
		for (int i=0; i<7; i++) {
			for (int j=0; j<6; j++){
				//List<Cell> aux= Cell.where("board_id = ? and col = ? and row = ?",this.get("id"),j,i);
				
				Cell c = this.boardM[j][i];
				if ((c.get("user_id")!=null)&&(players.getFst().get("id").equals(c.get("user_id")))) {
					o[j][i]='X';
				}else{
					if ((c.get("user_id")!=null)&&(players.getSnd().get("id").equals(c.get("user_id")))) {
						o[j][i]='0';
					}else{
						o[j][i]='-';
					}
				}
			}
		}
		for (int i=5; i>=0; i--) {
			System.out.println("");
			for (int j=0; j<7; j++){
				System.out.print(o[i][j]);
			}
		}
		System.out.println("");
	}

	// Return true when the board is full
	/*public Boolean fullBoard(){
		List<Cell> c=Cell.where("board_id = ? and user_id is null",this.get("id"));
		System.out.println(c.size());
		return c.isEmpty();
	}*/

	public Boolean fullBoard(){
		Boolean result=true;
		for (int i=0;i<7;i++){
			for(int j=0;j<6;j++){
				result=result&&(boardM[j][i].get("user_id")!=null);
			}
		}
		return result;
	}

	public Cell[] [] getBoard(){
		return this.boardM;
	}

	public void setBoard(Cell [] [] board){
		this.boardM=board;
	}
}

package com.unrc.app;

import java.util.*;

import java.lang.Exception;

import org.javalite.activejdbc.Model;

public class Board extends Model{
	
	private Cell [] [] boardM;	
	
	public Board(){
		boardM=new Cell[6][7];
	}
	
	public List<String>  toList(Game g){
		List<String> res=new LinkedList<String>();
		Cell current;
		for (int i=5; i>=0;i--){
			res.add("<tr align= "+'"'+"center"+'"'+">");
			for (int j=0;j<=6;j++){
				current=boardM[i][j];
				if (current.get("user_id")==null){
					res.add("<td bgcolor="+'"'+"#FFFFFF"+'"'+">");
					res.add("</td>");

				}else{
					if(isPlayer1(g,current)){
						res.add("<td bgcolor="+'"'+"#FF0000"+'"'+">");
						res.add("</td>");
					}else{
						res.add("<td bgcolor="+'"'+"#0004FF"+'"'+">");
						res.add("</td>");
					}
					
				}
			
			}
		}
		res.add("</table>");
		return res;
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
	}
	
	private Boolean isPlayer1(Game g,Cell c){
		System.out.println(this.get("game_id"));
		return (Game.findFirst("id=?",g.get("id")).get("player1_id").equals(c.get("user_id")));
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

	//	Initializes the board, all the cells are empty.
	public void emptyBoard(){
		for (int i=0; i<7; i++) {
			for (int j=0; j<6; j++) {
				Cell aux= Cell.createIt("board_id",this.get("id"),"row",j,"col",i);
				aux.saveIt();
			}
		}
	}

	public Boolean fullBoard(){
		Boolean result=true;
		for (int i=0;i<7;i++){
			for(int j=0;j<6;j++){
				result=result&&(boardM[j][i].get("user_id")!=null);
			}
		}
		return result;
	}

	public Boolean fullCol(Integer i){
		return (boardM[5][i].get("user_id")!=null);
	}
	
	public Cell[] [] getBoard(){
		return this.boardM;
	}

	public void setBoard(Cell [] [] board){
		this.boardM=board;
	}


}

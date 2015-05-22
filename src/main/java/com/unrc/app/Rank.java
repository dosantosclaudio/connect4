package com.unrc.app;

import org.javalite.activejdbc.Model;

public class Rank extends Model{
	
	private void empty(){

	}



	// Registra una partida ganada para player;
	public static void userWin(User player) {
		Rank ranking = Rank.findFirst("user_id = ?", player.get("id"));
		
		if (ranking == null) {
			Rank r = new Rank();
			r.set("user_id", player.get("id"));
			r.set("played_games", 1);
			r.set("won_games", 1);	
			r.set("score", 100);
			r.save();
	
		}else{	
			ranking.set("played_games", ranking.getInteger("played_games")+1);
			ranking.set("won_games", ranking.getInteger("won_games")+1);
			ranking.set("score", ranking.getInteger("score")+100);
			ranking.save();
		}
	}	


	// Registra una partida empatada para player y player2;
	public static void userDraw(User player){
		Rank ranking = Rank.findFirst("user_id = ?", player.get("id"));
		
		if (ranking == null) {
			Rank r = new Rank();
			r.set("user_id", player.get("id"));
			r.set("played_games", 1);
			r.set("tie_games", 1);
			r.set("score", 50);
			r.save();	
				
		}else{	
			ranking.set("played_games", ranking.getInteger("played_games")+1);
			ranking.set("tie_games", ranking.getInteger("tie_games")+1);
			ranking.set("score", ranking.getInteger("score")+50);
			ranking.save();
		}
	}

	public static void userLose(User player){
		Rank ranking = Rank.findFirst("user_id = ?", player.get("id"));
		
		if (ranking == null) {
			Rank r = new Rank();
			r.set("user_id", player.get("id"));
			r.set("played_games", 1);
			r.set("score", -60);
			r.save();	

		}else{	
			ranking.set("played_games", ranking.getInteger("played_games")+1);
			ranking.set("score", ranking.getInteger("score")-60);
			ranking.save();
			
		}
	}

}
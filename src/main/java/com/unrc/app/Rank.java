package com.unrc.app;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;

public class Rank extends Model{
	
	public String toStringName(){
		User u=User.findFirst("id=?",this.getString("user_id"));
		return u.toString2();
	}

	public String toStringPos(){
		
		long i=Rank.count("score> ?",this.get("score"));
		i++;
		
		return Long.toString(i);
	}

	public String toStringScore(){
		return this.getString("score");
	}

	// register a win game 
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

	// register a tie game 
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

	// register a lose game 
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
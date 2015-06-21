package com.unrc.app;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unrc.app.*;

import static org.junit.Assert.assertEquals;
import static org.javalite.test.jspec.JSpec.the;


public class RankTest {
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/connect4_test", "root", "root");
        System.out.println("RankTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("RankTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }


    @Test
    public void testRankingInitialization(){
    
    	try{
            User.insert("francotagro@gmail.com","francotagro","","contraseña");
    	}catch(UserException e){
            System.out.println(e.getCode());
        }
        User user = User.findFirst("email=?", "francotagro@gmail.com");
        Rank.userWin(user);

        Rank ranking = Rank.findFirst("user_id = ?", user.get("id"));
    	assertEquals(ranking.get("played_games"), 1);
        assertEquals(ranking.get("won_games"), 1);
        assertEquals(ranking.get("score"), 100);    

    }


    @Test
    public void testRankingUpdate(){
    
        try{
            User.insert("francotagro@gmail.com","francotagro","","contraseña");
        }catch(UserException e){
            System.out.println(e.getCode());
        }
        User user = User.findFirst("email=?", "francotagro@gmail.com");
        Rank.userWin(user);

        Rank.userDraw(user);
        Rank ranking = Rank.findFirst("user_id = ?", user.get("id"));
        assertEquals(ranking.get("played_games"), 2);
        assertEquals(ranking.get("won_games"), 1);
        assertEquals(ranking.get("tie_games"), 1);        
        assertEquals(ranking.get("score"), 150); 

        Rank.userLose(user);
        ranking = Rank.findFirst("user_id = ?", user.get("id"));
        assertEquals(ranking.get("played_games"), 3);
        assertEquals(ranking.get("won_games"), 1);
        assertEquals(ranking.get("score"), 90);      

    }


}

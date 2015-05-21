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
    public void testPlayedGames(){
    
   		User user = new User();
    	user.set("email", "juan@hotmail.coms");
    	user.set("password", "pass");
    	user.save();

    	Rank rank = new Rank();
    	rank.userWin(user);

      Rank ranking = Rank.findFirst("user_id = ?", user.get("id"));
    	assertEquals(ranking.get("played_games"), 1);

      System.out.println("TEST 1");

    }

    @Test
    public void testPlayed(){
    
      System.out.println("TEST 2");
      

    }
    

}

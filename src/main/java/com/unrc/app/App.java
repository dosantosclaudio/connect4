package com.unrc.app;
import java.util.*;

import com.unrc.app.User;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import java.util.*;

public class App
{
    public static void main( String[] args )
    {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/connect4", "root", "root");
		User u1= User.createIt("email","leandrobuttignol@gmail.com","first_name","Leo","password","password");
		u1.saveIt();
		User u2= User.createIt("email","leandrobuttignol1@gmail.com","first_name","Leo1","password","password");
		u2.saveIt();
		Game g= Game.createIt("player1_id",u1.get("id"),"player2_id",u2.get("id"));
		g.saveIt();
		Board p= Board.createIt("game_id",g.get("id"));
		p.saveIt();
        Base.close();
    }
	
}
	

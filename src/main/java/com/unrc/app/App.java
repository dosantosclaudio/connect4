package com.unrc.app;

import com.unrc.app.User;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import java.util.*;

public class App
{
    public static void main( String[] args )
    {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/connect4", "root", "root");


        Base.close();
    }
	
}
	



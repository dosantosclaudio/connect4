package com.unrc.app;
import java.util.*;
import static spark.Spark.*;
import com.unrc.app.User;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import java.util.*;
import java.lang.Exception;
import java.io.Console;
public class App
{
	public App(){
	//	Menu m=new Menu();
	}

    public static void main( String[] args )
    {
        get("/hello", (req, res) -> "Hello World");
    /*    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/connect4", "root", "root");

        App a=new App();
        
        Base.close();*/
    }
	
}
	

package com.unrc.app;
import java.util.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.get;
import static spark.Spark.post;
import com.unrc.app.User;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import java.util.*;
import java.lang.Exception;
import java.io.Console;
public class App
{
	public App(){
		Menu.showWebApp();
	}

    public static void main( String[] args )
    {

      /*    get("/", (request, response) -> {
               return new ModelAndView(null, "initPage.mustache");
            }, new MustacheTemplateEngine());
            */
        

        App a=new App();
        
        Base.close();
    }
	
}
	

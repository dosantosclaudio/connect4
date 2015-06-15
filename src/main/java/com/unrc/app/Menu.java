
package com.unrc.app;

import org.javalite.activejdbc.Base;
import com.unrc.app.Pair;
import com.unrc.app.User;

import java.lang.Exception;
import java.io.Console;
import java.util.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;
import static spark.Spark.after;


public class Menu{
	public static final String driver = "com.mysql.jdbc.Driver";
    public static final String jdbc = "jdbc:mysql://localhost/connect4";
    public static final String userdb = "root";
    public static final String passdb = "root";

	public Menu(){}

	public static void showWebApp(){


		//	Open database before execute a rote
		before((request, response) -> {
			if(!(Base.hasConnection())){
				Base.open(driver,jdbc,userdb,passdb);
			}

		});
		
		//	Close database after execute a rote
		after((request, response) -> {
    		if(Base.hasConnection()){
				Base.close();
			}
		});
		
		//	Show init page 
		get("/", (request, response) -> {
			HashMap<String,Object> attributes=new HashMap<String,Object> ();
			request.session(true);   
			String usr=request.session().attribute("SESSION_NAME");  
			if (usr==null){
				attributes.put("currentUser","Sign In or Sign Up for play now!");
				attributes.put("nameButton",'"'+"Sign In"+'"');
			}else{
				attributes.put("currentUser",User.findFirst("id=?",usr).get("email"));
				attributes.put("stateSignUp","disabled");
				attributes.put("nameButton",'"'+"Sign Out"+'"');	
			}
         	return new ModelAndView(attributes, "web/initPage.mustache");
		}, new MustacheTemplateEngine());


		post("/signup",(request,response)-> {	
			String usr=request.session().attribute("SESSION_NAME");  
			if(usr!=null){
				response.redirect("/");
				return null;
			}else{
				return new ModelAndView(null,"web/signUp.mustache");
			}
		},new MustacheTemplateEngine());

		
		post("/signin",(request,response)-> {
			String usr=request.session().attribute("SESSION_NAME");  
			if(usr!=null){
				request.session().removeAttribute("SESSION_NAME");
				response.redirect("/");
				return null;
			}else{
				return new ModelAndView(null,"web/signIn.mustache");
			}
		},new MustacheTemplateEngine());


		post("/signingup",(request,response)-> {
			// Obtengo datos del formulario
			 String email=request.queryParams("email");
			 String f_name=request.queryParams("first_name");
			 String l_name=request.queryParams("last_name");
			 String pass=request.queryParams("password");
			 String pass_check=request.queryParams("passwordcheck");
			 if (!pass.equals(pass_check)){
			 	return new ModelAndView(null,"web/signUp.mustache");
			 }else{
			 	 try{
					User.insert(email,f_name,l_name,pass);	
				 	HashMap<String,Object> attributes=new HashMap<String,Object>();
				 	String user=User.findFirst("email=?",email).getString("id");
				 	request.session().attribute("SESSION_NAME",user);
				 	attributes.put("currentUser",user);
					response.redirect("/");
				 	return null;
				 }catch(UserException e){
				 	return new ModelAndView(null,"web/signUp.mustache");
				 }

			 }
		},new MustacheTemplateEngine());



		post("/signingin",(request,response)-> {
			String email=request.queryParams("email");			
			String pass=request.queryParams("password");
		 	try{
				User current_user=User.signIn(email,pass);			
				HashMap<String,Object> attributes=new HashMap<String,Object> ();
				attributes.put("currentUser",current_user.get("id"));
				String user=current_user.getString("id");
				request.session().attribute("SESSION_NAME",user);
				response.redirect("/");
				return null;
			}catch(UserException e){
		 		return new ModelAndView(null,"web/signIn.mustache");
			}
		},new MustacheTemplateEngine());


		post("/selectSavedGame",(request,response)->{
			String user1=request.session().attribute("SESSION_NAME");
			if(user1==null){
				response.redirect("/");
				return null;
			}else{
				Map<String, Object> attributes = new HashMap<String,Object>();
				List<Game> game=Game.where("(player1_id=? or player2_id=?)and end_date is null",user1,user1);
				attributes.put("player1_id",user1);
				attributes.put("games",game);
				return new ModelAndView(attributes,"web/selectSavedGame.mustache");
			}
		},new MustacheTemplateEngine());


		post("/selectOpponent",(request,response)->{
			String user1=request.session().attribute("SESSION_NAME");
			if(user1==null){
				response.redirect("/");
				return null;
			}else{								
				Map<String, Object> attributes = new HashMap<String,Object>();
				attributes.put("player1_id",user1);
				attributes.put("user1",User.findFirst("id=?",user1).get("email"));	
				List<User> anotherU=User.where("id<>?",user1);
				attributes.put("users",anotherU);
				return new ModelAndView(attributes,"web/selectOpponent.mustache");
			}
		},new MustacheTemplateEngine());


		post("/play",(request,response)->{	//inicio de juego.
			String user1_id=request.session().attribute("SESSION_NAME");
			String rGame=request.queryParams("game");													
			Map<String, Object> attributes = new HashMap<>();
			Game currentGame;
			if (rGame!=null){
				currentGame=Game.findFirst("id=?",rGame);
				currentGame.resumeGame();
				String user2_id=Integer.toString((Integer)currentGame.get("player2_id"));
				attributes.put("user1",user1_id);
				attributes.put("user2",user2_id);
				if(currentGame.turnUser() %2==0){
					attributes.put("turnUser",user1_id);
				}else{
					attributes.put("turnUser",user2_id);
				}
			}else{
				String user2=request.session().attribute("PLAYER2");
				String user2_id;
				if (user2==null){
					user2=request.queryParams("player2");
					user2_id= Integer.toString((Integer)User.findFirst("email=?",user2).get("id"));
					
				}else{
					user2_id=user2;
				}
				attributes.put("user1",user1_id);
				attributes.put("user2",user2_id);
				attributes.put("turnUser",user1_id);
				Game g=new Game(new Pair<User,User>(User.findFirst("id=?",user1_id),User.findFirst("id=?",user2_id)));
				currentGame=g;
				currentGame.resumeGame();
			}
			Integer count=0;
			for (int i=0;i<7;i++){	
				if (currentGame.fullCol(count)) {
					attributes.put("stateButton"+Integer.toString(count),"disabled");
				}else{
					attributes.put("stateButton"+Integer.toString(count),"");
				}
				count++;
			}
			request.session().attribute("gameId",currentGame.getInteger("id"));
			attributes.put("board",currentGame.getBoard().toList(currentGame));
			return new ModelAndView(attributes,"web/play.mustache");			
		},new MustacheTemplateEngine());


		post("/doMovement",(request,response)->{
			Cell c=null;
			String turnUser;
			Integer gameId=request.session().attribute("gameId");
			Game currentGame=Game.findFirst("id=?",Integer.toString(gameId));
			currentGame.resumeGame();
			if(currentGame.turnUser() %2==0){
					turnUser=request.session().attribute("SESSION_NAME");
			}else{
					turnUser=request.queryParams("player2");
			}
			String col=request.queryParams("col");
			String user1_id=request.session().attribute("SESSION_NAME");
			String user2_id=request.queryParams("player2");
			String current_g=request.queryParams("game");
			User player1=User.findFirst("id=?",user1_id);
			User player2=User.findFirst("id=?",user2_id);
			Map<String, Object> attributes = new HashMap<>();
			User turn=User.findFirst("id=?",turnUser);
			try{
				c=currentGame.doMovement(turn,Integer.parseInt(col));
			}catch(BoardException f){
				switch (f.getCode()){
					case "000":
						System.out.println("Has been detected some problems in this aplication ");
						break;
					case "001":
						System.out.println(f.getMessage());
						break;
					case "002":
						System.out.println(f.getMessage());
						break;
					}
			}

			if (!currentGame.thereIsAWinner(turn,c) && !currentGame.full()){		
				attributes.put("user1",user1_id);
				attributes.put("user2",user2_id);
				if (turnUser.equals(user1_id)){
					attributes.put("turnUser",user2_id);
				}else{
					attributes.put("turnUser",user1_id);
				}
				Integer count=0;
				for (int i=0;i<7;i++){
					if (currentGame.fullCol(count)) {
						attributes.put("stateButton"+Integer.toString(count),"disabled");
					}else{
						attributes.put("stateButton"+Integer.toString(count),"");
					}
					count++;
				}
				attributes.put("gameId",currentGame.get("id"));
				attributes.put("board",currentGame.getBoard().toList(currentGame));
				return new ModelAndView(attributes,"web/play.mustache");
			}else{
					request.session().attribute("PLAYER2",user2_id);
					currentGame.set("end_date",Game.getDateMysql());
					currentGame.saveIt();
				if (currentGame.full()){
					if(currentGame.thereIsAWinner(turn,c)) {		
						if(turnUser.equals(user1_id)){
							currentGame.updateRankWithWinner(player1,player2);
							currentGame.set("result_p1","WIN");
							attributes.put("user",User.findFirst("id=?",user1_id).getString("email"));
							attributes.put("text","The WINNER is ");
							currentGame.saveIt();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}else{
							currentGame.updateRankWithWinner(player2,player1);
							currentGame.set("result_p1","LOOSE");
							attributes.put("user",User.findFirst("id=?",user2_id).getString("email"));
							attributes.put("text","The WINNER is ");
							currentGame.saveIt();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}
					}else{
						currentGame.updateRankWithDraw(player1,player2);
						currentGame.set("result_p1","TIE"); 
						attributes.put("text","TIE");
						attributes.put("user",null);	
						currentGame.saveIt();
						return new ModelAndView(attributes,"web/finishedGame.mustache");
					}
					
				}else{
					if(currentGame.thereIsAWinner(turn,c)) {
						if(turnUser.equals(user1_id)){
							currentGame.updateRankWithWinner(player1,player2);
							attributes.put("user",User.findFirst("id=?",user1_id).getString("email"));
							attributes.put("text","The WINNER is ");
							currentGame.saveIt();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}else{
							currentGame.updateRankWithWinner(player2,player1);
							currentGame.set("result_p1","LOOSE");
							attributes.put("user",User.findFirst("id=?",user2_id).getString("email"));
							attributes.put("text","The WINNER is ");
							currentGame.saveIt();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}
						
					}
				}
				return new ModelAndView(null,"web/initPage.mustache");
				//partida ganada o empatada"
			}
		},new MustacheTemplateEngine());

		
		post("/revenge",(request,response)->{
			response.redirect("/play");
			return null;
		},new MustacheTemplateEngine());

		
		post("/abandonedGame",(request,response)->{
			Integer gameId=request.session().attribute("gameId");
			Game currentGame=Game.findFirst("id=?",Integer.toString(gameId));
			currentGame.resumeGame();
			User player1= User.findFirst("id=?",(String)request.session().attribute("SESSION_NAME"));
			User player2=User.findFirst("id=?",request.queryParams("player2"));
			currentGame.set("end_date",Game.getDateMysql());
			currentGame.saveIt();
			if(currentGame.turnUser() %2==0){	
				currentGame.updateRankWithWinner(player2,player1);
			}else{
				currentGame.updateRankWithWinner(player1,player2);
			}
			request.session().removeAttribute("gameId");
			request.session().removeAttribute("PLAYER2");
			response.redirect("/");
			return null;
		},new MustacheTemplateEngine());
		
	
		post("/finishedgame",(request,response)->{
			request.session().removeAttribute("PLAYER2");
			request.session().removeAttribute("gameId");
			response.redirect("/");
			return null;
		},new MustacheTemplateEngine());


		post("/savegame",(request,response)->{	
			request.session().removeAttribute("PLAYER2");
			request.session().removeAttribute("gameId");
			response.redirect("/");
			return null;
		},new MustacheTemplateEngine());


		post("/ranking",(request,response)->{
			Map<String, Object> attributes = new HashMap<String,Object>();
			List<Rank> usersRank=Rank.where("true order by score DESC");
			attributes.put("ranks",usersRank);
			return new ModelAndView(attributes,"web/ranking.mustache");
		},new MustacheTemplateEngine());

	}	
}



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
        
    public static Game currentGameChanchada=null;
	
	public Menu(){}

	public static void showWebApp(){

		before((request, response) -> {
			Base.open(driver,jdbc,userdb,passdb);
		});
	
		after((request, response) -> {
    		Base.close();
		});


		get("/", (request, response) -> {
			HashMap<String,Object> attributes=new HashMap<String,Object> ();
			request.session(true);   
			String usr=request.session().attribute("SESSION_NAME");  
			if (usr==null){
				attributes.put("currentUser","Sign In or Sign Up for play now!");
				attributes.put("nameButton",'"'+"Sign In"+'"');
			}else{
				//Base.open(driver,jdbc,userdb,passdb);
				attributes.put("currentUser",User.findFirst("id=?",usr).get("email"));
				attributes.put("stateSignUp","disabled");
				attributes.put("nameButton",'"'+"Sign Out"+'"');
				//	Base.close();
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

		post("/signingup",(request,response)-> {
			String email=request.queryParams("email");
			System.out.println(email);
			String f_name=request.queryParams("first_name");
			System.out.println(f_name);
			String l_name=request.queryParams("last_name");
			System.out.println(l_name);
			String pass=request.queryParams("password");
			System.out.println(pass);
			String pass_check=request.queryParams("passwordcheck");
			System.out.println(pass_check);
			if (!pass.equals(pass_check)){
				return new ModelAndView(null,"web/signUp.mustache");
			}else{
				try{
					//Base.open(driver,jdbc,userdb,passdb);
					User.insert(email,f_name,l_name,pass);
					
					HashMap<String,Object> attributes=new HashMap<String,Object> ();
					String user=User.findFirst("email=?",email).getString("id");
					System.out.println(user);
					request.session().attribute("SESSION_NAME",user);
					attributes.put("currentUser",user);
					//Base.close();
					response.redirect("/");
					return null;
				}catch(UserException e){
					//Base.close();
					return new ModelAndView(null,"web/signUp.mustache");
				}
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

		post("/signingin",(request,response)-> {

			String email=request.queryParams("email");
			System.out.println(email);
			String pass=request.queryParams("password");
			System.out.println(pass);
		 	try{
				//Base.open(driver,jdbc,userdb,passdb);
				User current_user=User.signIn(email,pass);
			
				HashMap<String,Object> attributes=new HashMap<String,Object> ();
				attributes.put("currentUser",current_user.get("id"));
				String user=current_user.getString("id");
				request.session().attribute("SESSION_NAME",user);
				//Base.close();
				response.redirect("/");
				return null;
			}catch(UserException e){

		 	//Base.close();
				return new ModelAndView(null,"web/signIn.mustache");

			}
		 			
		},new MustacheTemplateEngine());

		post("/selectSavedGame",(request,response)->{
			String user1=request.session().attribute("SESSION_NAME");
			if(user1==null){
				response.redirect("/");
				return null;
			}else{
			
				//Base.open(driver,jdbc,userdb,passdb);
				Map<String, Object> attributes = new HashMap<String,Object>();
				List<Game> game=Game.where("(player1_id=? or player2_id=?)and end_date is null",user1,user1);
				attributes.put("player1_id",user1);
				System.out.println(game.size());
				attributes.put("games",game);
				//Base.close();
				return new ModelAndView(attributes,"web/selectSavedGame.mustache");
			}
		},new MustacheTemplateEngine());


		post("/selectOpponent",(request,response)->{
			String user1=request.session().attribute("SESSION_NAME");
			if(user1==null){
				response.redirect("/");
				return null;
			}else{
									
				//Base.open(driver,jdbc,userdb,passdb);
				Map<String, Object> attributes = new HashMap<String,Object>();
				attributes.put("player1_id",user1);
				attributes.put("user1",User.findFirst("id=?",user1).get("email"));
				
				System.out.println(User.findFirst("id=?",user1).get("email"));
				List<User> anotherU=User.where("id<>?",user1);
				System.out.println(anotherU);
				attributes.put("users",anotherU);
				//Base.close();
				return new ModelAndView(attributes,"web/selectOpponent.mustache");
			}
			/*}else{
				return new ModelAndView(null,"web/loginOrSignup.mustache");

			}*/	
		},new MustacheTemplateEngine());

		post("/play",(request,response)->{	//inicio de juego.
			//Base.open(driver,jdbc,userdb,passdb);
			String user1_id=request.session().attribute("SESSION_NAME");
			String resumeGame=request.queryParams("game");
			Map<String, Object> attributes = new HashMap<>();
			if (resumeGame!=null){
				currentGameChanchada=Game.findFirst("id=?",resumeGame);
				currentGameChanchada.resumeGame();
				String user2_id=Integer.toString((Integer)currentGameChanchada.get("player2_id"));
				System.out.println("FIJA ACA");
				System.out.println(user2_id);
				attributes.put("user1",user1_id);
				attributes.put("user2",user2_id);
				if(currentGameChanchada.turnUser() %2==0){
					attributes.put("turnUser",user1_id);
				}else{
					attributes.put("turnUser",user2_id);
				}
			}else{
				String user2=request.session().attribute("PLAYER2");
				String user2_id;
				if (user2==null){
					user2=request.queryParams("player2");
				
				//String user2=request.queryParams("player2");
					System.out.println(user2);
					System.out.println(User.findFirst("email=?",user2) );
					user2_id= Integer.toString((Integer)User.findFirst("email=?",user2).get("id"));
					System.out.println(user1_id);
					System.out.println(user2_id);
				}else{
					user2_id=user2;
				}
				attributes.put("user1",user1_id);

				attributes.put("user2",user2_id);
				attributes.put("turnUser",user1_id);

				Game g=new Game(new Pair<User,User>(User.findFirst("id=?",user1_id),User.findFirst("id=?",user2_id)));
				currentGameChanchada=g;


			}
			attributes.put("board",currentGameChanchada.getBoard().toList(currentGameChanchada));
			//Base.close();
			return new ModelAndView(attributes,"web/play.mustache");			

		},new MustacheTemplateEngine());

		post("/doMovement",(request,response)->{
			Cell c=null;
			String turnUser;
			if(currentGameChanchada.turnUser() %2==0){
				
					turnUser=request.session().attribute("SESSION_NAME");
			}else{
					turnUser=request.queryParams("player2");
			}


			String col=request.queryParams("col");
			String user1_id=request.session().attribute("SESSION_NAME");
			String user2_id=request.queryParams("player2");
			String current_g=request.queryParams("game");
			//Base.open(driver,jdbc,userdb,passdb);
			User player1=User.findFirst("id=?",user1_id);
			User player2=User.findFirst("id=?",user2_id);
			Map<String, Object> attributes = new HashMap<>();
			//Game currentGameChanchada=Game.findFirst("id=?",current_g);
			System.out.println(turnUser);
			System.out.println(user1_id);
			User turn=User.findFirst("id=?",turnUser);
			try{
				c=currentGameChanchada.doMovement(turn,Integer.parseInt(col));
				System.out.println(c);
			}catch(BoardException f){
				switch (f.getCode()){
					case "000":
						System.out.println("Has been detected some problems in this aplication ");
						//c=null;
						break;
					case "001":
						System.out.println(f.getMessage());
						
						break;
					case "002":
						System.out.println(f.getMessage());
						
						break;
					}
			}
			if (!currentGameChanchada.thereIsAWinner(turn,c) && !currentGameChanchada.full()){
				
				attributes.put("user1",user1_id);
				attributes.put("user2",user2_id);
				if (turnUser.equals(user1_id)){
					attributes.put("turnUser",user2_id);
					
				}else{
					attributes.put("turnUser",user1_id);
				
				}
				Integer count=0;
				for (int i=0;i<7;i++){
					if (currentGameChanchada.fullCol(count)) {
						attributes.put("stateButton"+Integer.toString(count),"disabled");
					}else{
						attributes.put("stateButton"+Integer.toString(count),"");
					}
					count++;
				}


				attributes.put("gameId",currentGameChanchada.get("id"));
				attributes.put("board",currentGameChanchada.getBoard().toList(currentGameChanchada));
			//	Base.close();
				return new ModelAndView(attributes,"web/play.mustache");
			}else{

					request.session().attribute("PLAYER2",user2_id);
					currentGameChanchada.set("end_date",Game.getDateMysql());
					currentGameChanchada.saveIt();
				if (currentGameChanchada.full()){
					if(currentGameChanchada.thereIsAWinner(turn,c)) {
						
						if(turnUser.equals(user1_id)){
							currentGameChanchada.updateRankWithWinner(player1,player2);
							currentGameChanchada.set("result_p1","WIN");
							
							attributes.put("user",User.findFirst("id=?",user1_id).getString("email"));
							attributes.put("text","The WINNER is ");
							currentGameChanchada.saveIt();
			//				Base.close();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}else{
							currentGameChanchada.updateRankWithWinner(player2,player1);
							currentGameChanchada.set("result_p1","LOOSE");
							
							attributes.put("user",User.findFirst("id=?",user2_id).getString("email"));
							attributes.put("text","The WINNER is ");
							currentGameChanchada.saveIt();
			//				Base.close();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}
					}else{
						currentGameChanchada.updateRankWithDraw(player1,player2);
						currentGameChanchada.set("result_p1","TIE GAME"); 
						attributes.put("text","TIE");
						attributes.put("user",null);
						
						currentGameChanchada.saveIt();
			//			Base.close();
						return new ModelAndView(attributes,"web/finishedGame.mustache");
					}
					
				}else{
					if(currentGameChanchada.thereIsAWinner(turn,c)) {
						if(turnUser.equals(user1_id)){
							currentGameChanchada.updateRankWithWinner(player1,player2);
							attributes.put("user",User.findFirst("id=?",user1_id).getString("email"));
							attributes.put("text","The WINNER is ");
							
							currentGameChanchada.saveIt();
			//				Base.close();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}else{
							currentGameChanchada.updateRankWithWinner(player2,player1);
							currentGameChanchada.set("result_p1","LOOSE");
							attributes.put("user",User.findFirst("id=?",user2_id).getString("email"));
							attributes.put("text","The WINNER is ");
							currentGameChanchada.saveIt();
			//				Base.close();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}
						
					}
				}


			//	Base.close();
				return new ModelAndView(null,"web/initPage.mustache");
				//partida ganada o empatada"
			}
			

		},new MustacheTemplateEngine());

		post("/revenge",(request,response)->{
			response.redirect("/play");
			return null;
		},new MustacheTemplateEngine());

		post("/abandonedGame",(request,response)->{
			//Base.open(driver,jdbc,userdb,passdb);
			String currentGameId=request.queryParams("gameId");
			Game currentGame=Game.findFirst("id=?",currentGameId);
			currentGame.resumeGame();
			User player1= User.findFirst("id=?",(String)request.session().attribute("SESSION_NAME"));
			User player2=User.findFirst("id=?",request.queryParams("player2"));

			if(currentGame.turnUser() %2==0){	
				currentGame.updateRankWithWinner(player2,player1);
			}else{
				currentGame.updateRankWithWinner(player1,player2);
			}
			request.session().removeAttribute("PLAYER2");
			//Base.close();
			response.redirect("/");
			return null;

		},new MustacheTemplateEngine());
		
	
		post("/finishedgame",(request,response)->{
			request.session().removeAttribute("PLAYER2");
			response.redirect("/");
			return null;
		},new MustacheTemplateEngine());


		post("/savegame",(request,response)->{
			//Base.open(driver,jdbc,userdb,passdb);
			String game_id=request.queryParams("gameId");
			currentGameChanchada.saveGame();
			//Base.close();
			response.redirect("/");
			return null;
		},new MustacheTemplateEngine());


		post("/ranking",(request,response)->{
		//	String user1=request.queryParams("player1");

			//Base.open(driver,jdbc,userdb,passdb);
			Map<String, Object> attributes = new HashMap<String,Object>();

			List<Rank> usersRank=Rank.where("true order by score DESC");
		
			System.out.println(usersRank);
			attributes.put("ranks",usersRank);

			//Base.close();
			return new ModelAndView(attributes,"web/ranking.mustache");

		},new MustacheTemplateEngine());

	}	
}


package com.unrc.app;

import org.javalite.activejdbc.Base;
import com.unrc.app.Pair;
import com.unrc.app.User;

import java.lang.Exception;
import java.io.Console;
import java.util.*;
import java.net.*;
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

	private static String getServerIp(){
	    String ip="";
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                InetAddress addr = addresses.nextElement();
	                ip = addr.getHostAddress();
	                System.out.println(iface.getDisplayName() + " " + ip);
	            }
	        }
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
		return ip;
	}

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
		//  the user must signin for execute an option
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

		get("/signup",(request,response)-> {	
			String usr=request.session().attribute("SESSION_NAME");  
			if(usr!=null){
				response.redirect("/");
				return null;
			}else{
				return new ModelAndView(null,"web/signUp.mustache");
			}
		},new MustacheTemplateEngine());
		
		get("/signin",(request,response)-> {
			String usr=request.session().attribute("SESSION_NAME");  
			if(usr!=null){
				request.session().removeAttribute("SESSION_NAME");
				response.redirect("/");
				return null;
			}else{
				return new ModelAndView(null,"web/signIn.mustache");
			}
		},new MustacheTemplateEngine());

		get("/signingup",(request,response)-> {
			// Obtengo datos del formulario
			String email=request.queryParams("email");
			String f_name=request.queryParams("first_name");
			String l_name=request.queryParams("last_name");
			String pass=request.queryParams("password");
			String pass_check=request.queryParams("passwordcheck");
			HashMap<String,Object> attributes=new HashMap<String,Object>();
			if (!pass.equals(pass_check)){
					attributes.put("message","The passwords aren't equals");
				return new ModelAndView(attributes,"web/signUp.mustache");
			}else{
				try{
					User.insert(email,f_name,l_name,pass);	
					String user=User.findFirst("email=?",email).getString("id");
					request.session().attribute("SESSION_NAME",user);
					attributes.put("currentUser",user);
					response.redirect("/");
					return null;
				}catch(UserException e){
				 	if (e.getCode().equals("001")){
						attributes.put("message",e.getMessage());
					}
					if (e.getCode().equals("002")){
						attributes.put("message",e.getMessage());
					}


					return new ModelAndView(attributes,"web/signUp.mustache");
				}

			}
		},new MustacheTemplateEngine());

		get("/signingin",(request,response)-> {
			String email=request.queryParams("email");			
			String pass=request.queryParams("password");
			HashMap<String,Object> attributes=new HashMap<String,Object> ();
		 	try{
				User current_user=User.signIn(email,pass);			
				attributes.put("currentUser",current_user.get("id"));
				String user=current_user.getString("id");
				request.session().attribute("SESSION_NAME",user);
				response.redirect("/");
				return null;
			}catch(UserException e){
				if (e.getCode().equals("003")){
					attributes.put("message",e.getMessage());
				}
				if (e.getCode().equals("004")){
					attributes.put("message",e.getMessage());
				}

		 		return new ModelAndView(attributes,"web/signIn.mustache");
			}
		},new MustacheTemplateEngine());


		get("/selectSavedGame",(request,response)->{
			String user1=request.session().attribute("SESSION_NAME");
			if(user1==null){
				response.redirect("/");
				return null;
			}else{
				Map<String, Object> attributes = new HashMap<String,Object>();
				attributes.put("player1_id",user1);
				attributes.put("user1",User.findFirst("id=?",user1).get("email"));
				List<Game> game=Game.where("(player1_id=? xor player2_id=?)and end_date is null",user1,user1);
				attributes.put("games",game);
				return new ModelAndView(attributes,"web/selectSavedGame.mustache");
			}
		},new MustacheTemplateEngine());


		get("/selectOpponent",(request,response)->{
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
			String rGame=request.queryParams("game");													
			Map<String, Object> attributes = new HashMap<>();
			Game currentGame;
			String user1_id, user2_id, turn_user;
			if (rGame!=null){
				currentGame=Game.findFirst("id=?",rGame);
				currentGame.resumeGame();
				user1_id=Integer.toString((Integer)currentGame.get("player1_id"));
				user2_id=Integer.toString((Integer)currentGame.get("player2_id"));
				if(currentGame.turnUser()){ 
					turn_user=user1_id;
				}else{
					turn_user=user2_id;
				}
			}else{
				Integer lastGameId=request.session().attribute("gameId");
				if (lastGameId!=null){												// if the game is revange
					Game lastGame=Game.findFirst("id=?",Integer.toString(lastGameId));
					user1_id=Integer.toString((Integer)lastGame.get("player1_id"));
					user2_id=Integer.toString((Integer)lastGame.get("player2_id"));
				}else{
					user1_id=request.session().attribute("SESSION_NAME");
					String user2=request.queryParams("player2");
					user2_id= Integer.toString((Integer)User.findFirst("email=?",user2).get("id"));
					
				}
				turn_user=user1_id;
				currentGame = new Game(new Pair<User,User>(User.findFirst("id=?",user1_id),User.findFirst("id=?",user2_id)));
				currentGame.resumeGame();
			}
			// Se restringe la insercion de fichas en el tabler deshabilitando  los botones
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
			attributes.put("user1",user1_id);
			attributes.put("user2",user2_id);
			attributes.put("turnUser",turn_user);
			attributes.put("turnUserEmail",User.findFirst("id=?",turn_user).getString("email"));			
			attributes.put("board",currentGame.getBoard().toList(currentGame));
			attributes.put("gameId",currentGame.getInteger("id"));
			return new ModelAndView(attributes,"web/play.mustache");			
		},new MustacheTemplateEngine());

		post("/doMovement",(request,response)->{
														//ACORTAR METODO!!!!!!!!1 NO PUEDE TENER 130 LINEAS 
			Integer gameId = request.session().attribute("gameId");
			Game currentGame = Game.findFirst("id=?",Integer.toString(gameId));
			currentGame.resumeGame();
			String 	turn_user, 
					sessionUser = request.session().attribute("SESSION_NAME"),
					col = request.queryParams("col"),
					user1_id = request.queryParams("player1"),
					user2_id = request.queryParams("player2"),
					current_g = request.queryParams("game");
			User 	player1 = User.findFirst("id=?",user1_id),
					player2 = User.findFirst("id=?",user2_id);
			Map<String, Object> attributes = new HashMap<>();
			Cell c=null;

			if(currentGame.turnUser()){
					turn_user=request.queryParams("player1");
			}else{
					turn_user=request.queryParams("player2");
			}
			User turn=User.findFirst("id=?",turn_user);
			try{ // this try catch check movement 
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
				// the game must continue
				if (turn_user.equals(user1_id)){
					turn_user =user2_id;
				}else{
					turn_user =user1_id;
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
				attributes.put("user1",user1_id);
				attributes.put("user2",user2_id);
				attributes.put("turnUser",turn_user);
				attributes.put("turnUserEmail",User.findFirst("id=?",turn_user).getString("email"));
				attributes.put("gameId",currentGame.get("id"));
				attributes.put("board",currentGame.getBoard().toList(currentGame));
				return new ModelAndView(attributes,"web/board.mustache");
			}else{
				//the game must stop for any option, full board o there is a winner.
				currentGame.set("end_date",Game.getDateMysql());
				currentGame.saveIt();
				if (currentGame.full()){
					if(currentGame.thereIsAWinner(turn,c)) {		
						// FULL BOARD AND PLAYER 2 IS WINNER
						currentGame.updateRankWithWinner(player2,player1);
						currentGame.set("result_p1","LOOSE");
						if (user2_id.equals(sessionUser)){
							attributes.put("text","GAME OVER ");
						}else{
							attributes.put("text","CONGRATULATIONS ");	
						}
						attributes.put("user",null);
						currentGame.saveIt();
						return new ModelAndView(attributes,"web/finishedGame.mustache");
					}else{
						// FULL BOARD BUT THERE IS NO WINNER
						currentGame.updateRankWithDraw(player1,player2);
						currentGame.set("result_p1","TIE"); 
						attributes.put("text","TIE");
						attributes.put("user",null);	
						currentGame.saveIt();
						return new ModelAndView(attributes,"web/finishedGame.mustache");
					}
				}else{

					if(currentGame.thereIsAWinner(turn,c)) {
						if(turn_user.equals(user1_id)){
							currentGame.updateRankWithWinner(player1,player2);
							if (user1_id.equals(sessionUser)){
								attributes.put("text","CONGRATULATIONS");
							}else{
								attributes.put("text","GAME OVER ");	
							}
							currentGame.set("result_p1","WIN");
							attributes.put("user",null);
							currentGame.saveIt();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}else{
							currentGame.updateRankWithWinner(player2,player1);
							currentGame.set("result_p1","LOOSE");
							if (user2_id.equals(sessionUser)){
								attributes.put("text","CONGRATULATIONS ");
							}else{
								attributes.put("text","GAME OVER ");	
							}
							attributes.put("user",null);
							currentGame.saveIt();
							return new ModelAndView(attributes,"web/finishedGame.mustache");
						}
					}
				}
				return new ModelAndView(null,"web/initPage.mustache");
			}
		},new MustacheTemplateEngine());

		
		get("/revenge",(request,response)->{
			response.redirect("/play");
			return null;
		},new MustacheTemplateEngine());

		
		get("/abandonedGame",(request,response)->{
			Integer gameId=request.session().attribute("gameId");
			Game currentGame=Game.findFirst("id=?",Integer.toString(gameId));
			currentGame.resumeGame();
			User 	player1= User.findFirst("id=?",currentGame.get("player1_id")),
					player2=User.findFirst("id=?",currentGame.get("player2_id"));
			currentGame.set("end_date",Game.getDateMysql());
			currentGame.saveIt();
			if(currentGame.turnUser()){	
				currentGame.updateRankWithWinner(player2,player1);
			}else{
				currentGame.updateRankWithWinner(player1,player2);
			}
			request.session().removeAttribute("gameId");
			response.redirect("/");
			return null;
		},new MustacheTemplateEngine());
		
	
		get("/finishedgame",(request,response)->{
			request.session().removeAttribute("gameId");
			response.redirect("/");
			return null;
		},new MustacheTemplateEngine());


		get("/savegame",(request,response)->{	
			request.session().removeAttribute("gameId");
			response.redirect("/");
			return null;
		},new MustacheTemplateEngine());


		get("/ranking",(request,response)->{
			Map<String, Object> attributes = new HashMap<String,Object>();
			List<Rank> usersRank=Rank.where("true order by score DESC");
			attributes.put("ranks",usersRank);
			return new ModelAndView(attributes,"web/ranking.mustache");
		},new MustacheTemplateEngine());

		//************ PLAY ONLINE ******************
		
		get("/selectOnlineOpponent",(request,response)->{
			String usr=request.session().attribute("SESSION_NAME");  
			if(usr==null){
				response.redirect("/");
				return null;
			}else{
				Map<String, Object> attr = new HashMap<String,Object>();
				/*try {
					InetAddress ip =InetAddress.getLocalHost();
					//attr.put("ip",Inet4Address.getLocalHost().getHostAddress().toString());
					
					attr.put("ip",ip.getHostAddress());
				}catch(UnknownHostException e){
					e.printStackTrace();
				}*/
				attr.put("ip",Menu.getServerIp().toString());
				return new ModelAndView(attr, "web/selectOnlineOpponent.mustache");
			}
		},new MustacheTemplateEngine());


		post("/createTable",(request,response)->{
			Map<String, Object> attributes = new HashMap<String,Object>();
			String usr=request.session().attribute("SESSION_NAME");  
			Game currentGame= new Game(new Pair(User.findFirst("id=?",usr),User.findFirst("id=?",usr)));
			currentGame.set("channel",request.queryParams("channel")).saveIt();
			attributes.put("game",currentGame.get("id"));
			attributes.put("channel",request.queryParams("channel"));
			attributes.put("ip",Menu.getServerIp());
			return new ModelAndView(attributes,"web/createTable.mustache");
		},new MustacheTemplateEngine());

		get("/searchTable", (request,response)->{
			Map<String, Object> attributes = new HashMap<String,Object>();
			List<Game> sleepingGame = Game.where("player1_id=player2_id");
			attributes.put("games",sleepingGame);
			return new ModelAndView(attributes,"web/searchTable.mustache");
		},new MustacheTemplateEngine());
		
		post("/startingGame",(request,response)->{
			Map<String, Object> attributes = new HashMap<String,Object>();
			Game currentGame= Game.findFirst("id=?",request.queryParams("game"));
			currentGame.set("player2_id",request.session().attribute("SESSION_NAME")).saveIt();
			attributes.put("channel",request.queryParams("channel"));
			attributes.put("game",request.queryParams("game"));
			attributes.put("ip",Menu.getServerIp());
			return new ModelAndView(attributes,"web/startingGame.mustache");
		},new MustacheTemplateEngine());


		post("/playOnline",(request,response)->{	
			String rGame=request.queryParams("game");
			String channel="chn"+request.queryParams("channel").toString();											
			Map<String, Object> attributes = new HashMap<>();
			Game currentGame=Game.findFirst("id=?",rGame);
			String user1_id, user2_id, turn_user;
			currentGame.resumeGame();
			user1_id=Integer.toString((Integer)currentGame.get("player1_id"));
			user2_id=Integer.toString((Integer)currentGame.get("player2_id"));
			if(currentGame.turnUser()){ 
				turn_user=user1_id;
			}else{
				turn_user=user2_id;
			}		
			// Se restringe la insercion de fichas en el tabler deshabilitando  los botones
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
			attributes.put("channel",channel);
			attributes.put("user1",user1_id);
			attributes.put("user2",user2_id);
			attributes.put("turnUser",turn_user);
			attributes.put("turnUserEmail",User.findFirst("id=?",turn_user).getString("email"));			
			attributes.put("board",currentGame.getBoard().toList(currentGame));
			attributes.put("gameId",currentGame.getInteger("id"));
			return new ModelAndView(attributes,"web/play.mustache");			
		},new MustacheTemplateEngine());


	}	


}
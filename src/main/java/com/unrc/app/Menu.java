package com.unrc.app;

import com.unrc.app.Pair;
import com.unrc.app.User;
import java.util.*;
import java.lang.Exception;
import java.io.Console;
import org.javalite.activejdbc.Base;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.get;
import static spark.Spark.post;

public class Menu{
	 public static final String driver = "com.mysql.jdbc.Driver";
     public static final String jdbc = "jdbc:mysql://localhost/connect4";
     public static final String userdb = "root";
     public static final String passdb = "root";
        

	public Menu(){
		Base.open(driver,jdbc,userdb,passdb);
		main_menu();
	}


	public static void showWebApp(){
		get("/", (request, response) -> {
			   HashMap<String,Object> attributes=new HashMap<String,Object> ();
			   attributes.put("currentUser",null);	
               return new ModelAndView(attributes, "web/initPage.mustache");
            }, new MustacheTemplateEngine());

		post("/signup",(request,response)-> {
			return new ModelAndView(null,"web/signUp.mustache");
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
				 	Base.open(driver,jdbc,userdb,passdb);
				 	User.insert(email,f_name,l_name,pass);
				 	Base.close();
				 	HashMap<String,Object> attributes=new HashMap<String,Object> ();
				 	attributes.put("currentUser",email);
				 	return new ModelAndView(attributes,"web/initPage.mustache");
				 }catch(UserException e){


				 	Base.close();
				 	return new ModelAndView(null,"web/signUp.mustache");

				 }

			 }
			
		},new MustacheTemplateEngine());








		post("/signin",(request,response)-> {
			return new ModelAndView(null,"web/signIn.mustache");
		},new MustacheTemplateEngine());



	/*	post("/signingin",(request,response)-> {

			 String email=request.queryParams("email");
			 System.out.println(email);
			 String pass=request.queryParams("password");
			 System.out.println(pass);
		 	 try{
			 	Base.open(driver,jdbc,userdb,passdb);
			 	User current_user=User.signIn(email,pass);
			 	Base.close();
			 	HashMap<String,Object> attributes=new HashMap<String,Object> ();
			 	attributes.put("currentUser",currentUser.getE);
			 	return new ModelAndView(attributes,"web/initPage.mustache");
			 }catch(UserException e){


			 	Base.close();
			 	return new ModelAndView(null,"web/signUp.mustache");

			 }

		 
			
		},new MustacheTemplateEngine());*/



		post("/selectOpponent",(request,response)->{
				String user1=request.queryParams("player1");
				System.out.println(user1);
			
				Map<String, Object> attributes = new HashMap<>();
				attributes.put("user1",user1);
				Base.open(driver,jdbc,userdb,passdb);
				List<User> anotherU=User.where("email <> ?",user1);
				Base.close();
				attributes.put("users",anotherU);

				return new ModelAndView(attributes,"web/selectOpponent.mustache");
			/*}else{
				return new ModelAndView(null,"web/loginOrSignup.mustache");

			}*/	
		},new MustacheTemplateEngine());


	/*	 post("/signinup", (request, response) -> {
               
			 String email=request.queryParams("email");
			 String f_name=request.queryParams("first_name");
			 String l_name=request.queryParams("last_name");
			 String pass=request.queryParams("password");
			 String pass_check=request.queryParams("check");
                if (!pass.equals(pass_check)){
			 	return new ModelAndView(null,"web/singUp.mustache");
			 }else{

				 try{
				 	Base.open(driver,jdbc,userdb,passdb);
				 	User.insert(email,f_name,l_name,pass);
				 	Base.close();
				 	return new ModelAndView(null,"web/initPage.mustache");
				 }catch(UserException e){
				 	Base.close();

				 }
			 }
               
            }, new MustacheTemplateEngine());
*/
	}


	
	//It clear the console.
	public final static void clearConsole(){
	 	System.out.print("\033[H\033[2J");
		System.out.flush();
	}	


		public final static void wait(int seconds){
			try {
	    		Thread.sleep(1000*seconds);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
	    		Thread.currentThread().interrupt();
			}
		}
	

	public void main_menu(){
		clearConsole();
		switch(submenu1()){
			case "1":
				signUpMenu();
				break;
			case "2":
					menu_option2();
				break;
			case "3":
				User guest=new User();
				guest.userGuest();
				guest.setFirstNameGuest("Example1");
				guest.setLastNameGuest("Example1");
			//	play(userMenu(guest));				//Hay que cambiar Pair por game
				
				break;
			case "4":
					System.exit(0);
				break;
			default:
				System.out.println("Option is incorrect. Please choose again.");
				wait(2);
				main_menu();
		}

	}

	private String submenu1(){
		clearConsole();
		System.out.println("*************************************");
		System.out.println("***********  CONNECT4  **************");
		System.out.println("");
		System.out.println("Select just a option:");
		System.out.println("   1. Sign Up.");
		System.out.println("   2. Sign In.");
		System.out.println("   3. Play (Guest).");
		System.out.println("   4. Exit.");
		System.out.println("");
		System.out.print("Option: ");
		String option = "";
		Scanner inputScanner = new Scanner(System.in); //Creación de un objeto Scanner
		option = inputScanner.nextLine(); //Invocamos un método sobre un objeto Scanner
		return option;
	}

	private void signUpMenu(){
		clearConsole();
		System.out.println("*************************************");
		System.out.println("***********  CONNECT4  **************");
		System.out.println("");
		System.out.println("Complete:");
		System.out.print("E-mail: ");
		String email = "";
		Scanner inputScanner = new Scanner(System.in); //Creación de un objeto Scanner
		email = inputScanner.nextLine(); //Invocamos un método sobre un objeto Scanner
		System.out.println("");
		System.out.print("First name: ");
		String fName="";
		fName=inputScanner.nextLine(); 
		System.out.println("");
		System.out.print("Last name: ");
		String lName="";
		lName=inputScanner.nextLine();
		String pass=SignUpPassword();
		signUp(email,lName,fName,pass);
		clearConsole();
		System.out.println("You are registered in Connect4. Thank you!.");
		wait(3);
		main_menu();	
	}

	public void signUp(String ema,String fn,String ln,String p){
		try{
			User.insert(ema,fn,ln,p);
			clearConsole();
			System.out.println("You are registered in Connect4 now.");
		}catch(UserException e){
			if (e.getCode().equals("001")){
				clearConsole();
				System.out.println(e.getMessage()+"Press 'Enter' for continue. ");
				Scanner inputScanner = new Scanner(System.in);
				String option=inputScanner.nextLine();
				main_menu();

			}
			if (e.getCode().equals("002")){
				boolean cont=true;
				while (cont){
					clearConsole();
					System.out.println(e.getMessage()+" What do you want to do?\n Press 'C' for enter your password again or press 'X' for go back.");
					Scanner inputScanner = new Scanner(System.in);
					String option=inputScanner.nextLine();
					if ((option.equals("c"))||(option.equals("C"))){
						signUp(ema,fn,ln,SignUpPassword());
						cont=false;

					}else{
						if ((option.equals("X"))||(option.equals("x"))){
							main_menu();
							cont=false;
						}
					}
				}
			}

		}
	}

	private String SignUpPassword(){
		Console console = System.console();
    	char passwordArray[] = console.readPassword("\nEnter your password: ");
    	String pass=new String(passwordArray);
    	System.out.println("");
		char passwordArray1[] = console.readPassword("\nPlease confirm your password: ");
		String pass1=new String(passwordArray1);
		if (!(pass.equals(pass1))){
			System.out.println("You cannot confirm your password.\nPlease press 'Y' for enter your password again or press 'X' for go back.");
			Scanner inputScanner = new Scanner(System.in);
			String option=inputScanner.nextLine();

			if ((option.equals("y"))||(option.equals("u"))){
						return SignUpPassword();
	

					}else{
						if ((option.equals("X"))||(option.equals("x"))){
							main_menu();
							
						}
					}

			return SignUpPassword();		//Si presiona otra tecla que no sea ni y ni x, vuelve a pedir la password.	
		}else{
			return pass;
		}
	}

	public User signInMenu(){
		clearConsole();
		System.out.println("*************************************");
		System.out.println("***********  CONNECT4  **************\n\n");
		System.out.print("Enter your e-mail: ");
		Scanner inputScanner = new Scanner(System.in);
		String em=inputScanner.nextLine();
		Console console=System.console();
		char passwordArray[] = console.readPassword("\nEnter your password: ");
    	String pass=new String(passwordArray);
    	try{
    		return User.signIn(em,pass);

    	}catch(UserException e){
    		clearConsole();
    		if (e.getCode().equals("003")){
    			System.out.println(e.getMessage()+" Press 'R' for sign in again or press 'X' for go back.");
    			String option=inputScanner.nextLine();
    			if ((option.equals("r"))||(option.equals("R"))){
    				return signInMenu();
    			}else{
    				main_menu();
    			}
    		}
    		if (e.getCode().equals("004")){
    			System.out.println(e.getMessage()+" Press 'R' for sign in again or press 'X' for go back.");
				String option=inputScanner.nextLine();
				if ((option.equals("r"))||(option.equals("R"))){
	    			return signInMenu();
	    		}else{
	    			main_menu();
    			}
    		}
    		return null;
    	}
	}
 
 

	public void menu_option2(){
		clearConsole();
		System.out.println("*************************************");
		System.out.println("***********  CONNECT4  **************\n\n");
		System.out.println("Select just a option: ");
		System.out.println("\n 1- New game. ");
		System.out.println(" 2- Resume game.");
		System.out.print("Option :");
		Scanner inputScanner = new Scanner(System.in);
		String em=inputScanner.nextLine();
		if (em.equals("1")){
			play(userMenu(signInMenu()),null);
		}
		if(em.equals("2")){
			resumeGameMenu(signInMenu());
		}

	}

	public Pair<User,User> userMenu(User u){
		clearConsole();
		List<User> anotherU;
		if (u.isGuest()){
			 anotherU=User.findAll();
			clearConsole();
		}else{
			 anotherU=User.where("email <> ?",u.get("email"));
			clearConsole();
		}
		System.out.println("*************************************");
		System.out.println("***********  CONNECT4  **************\n\n");
		if (u.isGuest()){
			System.out.println ("User info - email: "+u.getEmailGuest()+ "	- Name: "+u.getFirstNameGuest()+" "+u.getLastNameGuest());
		}else {
			System.out.println ("User info - email: "+u.get("email")+ "	- Name: "+u.get("first_name")+" "+u.get("last_name"));	
		}
		System.out.println("Please some key for select another user");
		int count=0;
		for (User user: anotherU){
			count++;
			System.out.println ("	"+Integer.toString(count)+"- email: "+user.get("email")+"		Name: "+user.get("first_name")+" "+user.get("last_name"));
		}
		if (count==0){
			System.out.println("There aren't available users, try again. Press Enter for continue ");
			Scanner inputScanner = new Scanner(System.in);
			String em=inputScanner.nextLine();
			main_menu();
		}
		System.out.print("User: ");
		Scanner inputScanner = new Scanner(System.in);
		String em=inputScanner.nextLine();
		return new Pair(u, anotherU.get(Integer.parseInt(em)-1));
	}

	public void resumeGameMenu(User a){	

		List<Game> game=Game.where("(player1_id=? or player2_id=?)and end_date is null",a.get("id"),a.get("id"));
		System.out.println("Please, choose the game that you want resume");
		String em=null;
		if (game.size()==0){
			System.out.println("You don't have any game saved.");
			wait(2);
			main_menu();
		}else{
			System.out.println("Please, choose a game saved: ");
			int count=0;
			for(Game g: game ){
				count ++;
				System.out.println(" "+Integer.toString(count)+" - Number game: "+g.get("id")+" - player 1: "+User.findFirst("id=?",g.get("player1_id")).get("email")+"- player2: "+ User.findFirst("id=?",g.get("player2_id")).get("email"));
			
			}
			System.out.print("Game number : ");
			Scanner inputScanner = new Scanner(System.in);
			em=inputScanner.nextLine();
		}
		Game aux= game.get(Integer.parseInt(em)-1);
		aux.resumeGame();
		play(new Pair<User,User>(User.findFirst("id=?",aux.get("player1_id")),User.findFirst("id=?",aux.get("player2_id"))),aux);

	}






	public void play(Pair<User,User> players,Game game){
		int counter=0;
		Game g;
		if (game==null){
			counter=0;
			g=new Game(players);	
		}else{
			g=game;
			counter=g.turnUser();
			
		}
		User turn;
		g.printBoardOnScreen(players);
		if(counter % 2 == 0){
			turn=players.getFst();
		}else{
			turn=players.getSnd();
		}
		clearConsole();
		g.printBoardOnScreen(players);
		Cell c = g.doMovement(turn);
		if (c==null){
			g.saveGame();					// if the player wanna save the game.
		}else{
			while(!g.thereIsAWinner(turn,c) && !g.full()){
				g.printBoardOnScreen(players);
				counter ++;
				if(counter % 2 == 0){
					turn=players.getFst();
				}else{
					turn=players.getSnd();
				}
				c=g.doMovement(turn);
				if (c==null){
					break;
				}
			}
			if (c==null){
				g.saveGame();				//if the player wanna save the game.

			}else{	
					g.set("end_date",Game.getDateMysql());
					g.saveIt();
				if (g.full()){
					if(g.thereIsAWinner(turn,c)) {
						if(counter % 2 == 0){
							g.updateRankWithWinner(players.getFst(),players.getSnd());
							g.set("result_p1","WIN");
						}else{
							g.updateRankWithWinner(players.getSnd(),players.getFst());
							g.set("result_p1","LOOSE");
						}
					}else{
						g.updateRankWithDraw(players.getFst(),players.getSnd());
						g.set("result_p1","TIE");
					}
					g.saveIt();
				}else{
					if(g.thereIsAWinner(turn,c)) {
						if(counter % 2 == 0){
							g.updateRankWithWinner(players.getFst(),players.getSnd());
							g.set("result_p1","WIN");
						}else{
							g.updateRankWithWinner(players.getSnd(),players.getFst());
							g.set("result_p1","LOOSE");
						}
						g.saveIt();
					}
				}
			}
		}
	}
}


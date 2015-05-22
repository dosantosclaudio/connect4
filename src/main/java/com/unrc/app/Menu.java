package com.unrc.app;

import com.unrc.app.Pair;
import com.unrc.app.User;
import java.util.*;
import java.lang.Exception;
import java.io.Console;

public class Menu{

	public Menu(){
		main_menu();
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
				Pair<User,User> p=userMenu(signInMenu());		//Hay que cambiar Pair por game
				break;
			case "3":
				User guest=new User();
				guest.userGuest();
				guest.setFirstNameGuest("Example1");
				guest.setLastNameGuest("Example1");
				Pair<User,User> p1 =userMenu(guest);				//Hay que cambiar Pair por game
				
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
    				signInMenu();
    			}else{
    				main_menu();
    			}
    		}
    		if (e.getCode().equals("004")){
    			System.out.println(e.getMessage()+" Press 'R' for sign in again or press 'X' for go back.");
				String option=inputScanner.nextLine();
				if ((option.equals("r"))||(option.equals("R"))){
	    			signInMenu();
	    		}else{
	    			main_menu();
    			}
    		}
    		return null;
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
}


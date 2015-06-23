package com.unrc.app;

import org.javalite.activejdbc.Model;

import java.util.List;
import java.util.UUID;

public class User extends Model {

	private String emailGuest, fNameGuest, lNameGuest;

	public String toString2(){
		return this.getString("email");
	}

	public String toStringRanking(){
		Rank userRank=Rank.findFirst("user_id=?",this.get("id"));
		return userRank.toStringPos();
	}

	static {
		validatePresenceOf("email","password");
	}

	//Metodo que permite loguearse. 
	public static User signIn(String email,String pass) throws UserException{
	  	UserValidate helper=new UserValidate();
	  	helper.isValidateSignInUser(email,pass);
	  	return (User) User.where("email = ?",email).get(0);
  	}

  	//Metodo que permite ingresar user a la base de datos. Debe ser situada dentro de una sentencia try/catch
	public static void insert(String email,String fName,String lName,String pass) throws UserException{
  		UserValidate helper=new UserValidate();
	  	helper.isValidateSignUpUser(email,fName,lName,pass); 
	  	User u=new User();
	  	User.createIt("email",email,"first_name",fName,"last_name",lName,"password",pass);
	  	u=findFirst("email = ?",email);
	  	Rank.createIt("user_id",u.get("id"));	//Crear Ranking para usuario recien creado. 
	}


	public void win(){
		Rank.userWin(this);
	}

	public void loose(){
		Rank.userLoose(this);
	}

	public void tie(){
		Rank.userDraw(this);
	}


}

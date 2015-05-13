package com.unrc.app;

import org.javalite.activejdbc.Model;

public class User extends Model {

	private String emailGuest, fNameGuest, lNameGuest;

	static {
		validatePresenceOf("email");
	}


	//Metodo que permite loguearse. Debe ser situada dentro de una sentencia try/catch
	public void signIn(String email,String pass) throws UserException{
	  	UserValidate helper=new UserValidate();
	  	helper.isValidateSingInUser(email,pass);
  	}

  	//Metodo que permite ingresar user a la base de datos. Debe ser situada dentro de una sentencia try/catch
	public void insert(String email,String fName,String lName,String pass) throws UserException{
  		UserValidate helper=new UserValidate();
	  	helper.isValidateSingUpUser(email,fName,lName,pass);
	  	User u=new User();
	  	User.createIt("email",email,"first_name",fName,"last_name",lName,"password",pass);
	}

	//Metodo que permite eliminar usuario, ver despues por el tema de la eliminacion en cascada.
	public void delete(Integer id){
	  	//hacer
	}

}

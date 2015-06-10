package com.unrc.app;
import java.util.List;
import org.javalite.activejdbc.Model;
import java.util.UUID;
public class User extends Model {

	private String emailGuest, fNameGuest, lNameGuest;

	@Override
	public String toString(){
		return this.getString("email");
	}


	static {
		validatePresenceOf("email","password");
	}

	public void userGuest(){
		this.emailGuest=getRandomEmailGuest();
	}

	
	//Metodo que permite loguearse. Debe ser situada dentro de una sentencia try/catch
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

	//Metodo que permite eliminar usuario, ver despues por el tema de la eliminacion en cascada.
	public void delete(Integer id){
	  	//hacer
	}



	public String getRandomEmailGuest(){
		return UUID.randomUUID().toString().substring(0,7);						//Ver el tema de repetidos, poca probabilidad.	
	}

	public boolean isGuest(){
		if (emailGuest!=null){
			return ((emailGuest.length()) > 0);  
		}else{
			return false;
		}
	}

	public String getEmailGuest(){
		return this.emailGuest;
	}

	public String getFirstNameGuest(){
		return this.fNameGuest;
	}

	public String getLastNameGuest(){
		return this.lNameGuest;
	}

	public void setEmailGuest(String e){
		this.emailGuest=e;
	}

	public void setFirstNameGuest(String f){
		this.fNameGuest=f;
	}

	public void setLastNameGuest(String l){
		this.lNameGuest=l;
	}
}

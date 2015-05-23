package com.unrc.app;
import java.util.List;

public class UserValidate {
	
	public UserValidate(){

	}

	//Permite verificar si los datos ingresados son correctos para registrarse en el sistema
	public void isValidateSignUpUser(String email,String fName,String lName,String password) throws UserException{
	  	List<User> list=User.where("email = ?",email);
	  	//Si ya existe el email en la base de datos.(Email es unico).
	  	if (list.size()!=0) {
	  		throw new UserException("Invalid e-mail adress. Allready have an account in Connect4.","001");
  		}else{
  			//Si la password tiene un largo menor a 8 caracteres.
  			if (password.length()<8){
  				throw new UserException("The password is too shortest, it must be equal or more larguest than 8 characters.","002");
  			}
  		}
  	}


  	//Verifica en la base de datos si el email está y si la contraseña es la correcta

  	public void isValidateSignInUser(String email,String pass)throws UserException{
  		
  		List<User> list=User.where("email = ?",email);
  		//Si no existe el email en la base de datos.
  		if (list.size()==0){
  			throw new UserException("Invalid e-mail adress.","003");
  		}else{
  			User u=list.get(0);							//E-mail unico
  			//Si el password no coincide con el password del email ingresado
  			if (!(u.get("password").equals(pass))){
  				throw new UserException("Invalid password.","004");
  			}
  		}
  	}
}
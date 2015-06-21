package com.unrc.app;

import java.lang.Exception;

public class UserException extends Exception{
	private String code;
	public static final long serialVersionUID=700L;

	public UserException(String message, String cod){
		super(message);
		this.code=cod;
	}

	public String getCode(){
		return this.code;
	}

	public void setCode(String cod){
		this.code=code;
	}
}

/*codigos
	"001"=Email is sign up.
	"002"=Password's length is smaller than 8 characters.
	"003"=Email is incorrect.
	"004"=Password is incorrect.
*/
package com.unrc.app;

import java.lang.Exception;

public class BoardException extends Exception{
	private String code;
	public static final long serialVersionUID=700L;

	public BoardException(String message,String cod){
		super(message);
		this.code=cod;
	}

	public String getCode(){
		return this.code;
	}

	public void setCode(String cod){
		this.code=code;
	}
/*
 *	000 = Fatal error.
 *	001 = Invalid column value.
 *	002 = This column is complete, insert your chip in another place.
 */
}

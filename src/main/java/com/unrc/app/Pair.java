package com.unrc.app;
public class Pair<T,F>{
	private T first;
	private F second;

	public Pair(){
		this.first=null;
		this.second=null;
	}

	public Pair(T f, F s){
		this.first=f;
		this.second=s;
	}

	public T getFst(){
		return this.first;
	}

	public F getSnd(){
		return this.second;
	}

	public void setFst(T f){
		this.first=f;
	}

	public void setSnd(F s){
		this.second=s;
	}
}
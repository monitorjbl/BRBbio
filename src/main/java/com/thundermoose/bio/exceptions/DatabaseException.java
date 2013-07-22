package com.thundermoose.bio.exceptions;

public class DatabaseException extends RuntimeException{

	private static final long	serialVersionUID	= 369219765767451793L;

	public DatabaseException(){
		super();
	}
	
	public DatabaseException(String message){
		super(message);
	}
}

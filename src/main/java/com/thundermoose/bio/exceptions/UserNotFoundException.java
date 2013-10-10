package com.thundermoose.bio.exceptions;

public class UserNotFoundException extends RuntimeException{

	private static final long	serialVersionUID	= 369219765767451793L;

	public UserNotFoundException(){
		super();
	}
	
	public UserNotFoundException(String message){
		super(message);
	}
}

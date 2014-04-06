package com.thundermoose.bio.exceptions;

public class NotFoundException extends RuntimeException{

	private static final long	serialVersionUID	= 369219765767451793L;

	public NotFoundException(){
		super();
	}

	public NotFoundException(String message){
		super(message);
	}
}

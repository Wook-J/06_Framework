package edu.kh.project.security.exception;

public class AdminNotFoundException extends RuntimeException {
	
	public AdminNotFoundException(String adminId){
		super("adminId ==> '" + adminId + "' NotFoundException");
	}
}

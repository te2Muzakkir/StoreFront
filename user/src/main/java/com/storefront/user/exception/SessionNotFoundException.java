package com.storefront.user.exception;

public class SessionNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
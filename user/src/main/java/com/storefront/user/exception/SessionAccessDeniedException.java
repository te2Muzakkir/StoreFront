package com.storefront.user.exception;

public class SessionAccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SessionAccessDeniedException(String message) {
        super(message);
    }

}
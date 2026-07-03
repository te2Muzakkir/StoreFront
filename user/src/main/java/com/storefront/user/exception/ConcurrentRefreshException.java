package com.storefront.user.exception;

public class ConcurrentRefreshException extends RefreshTokenException {


	private static final long serialVersionUID = 1L;

	public ConcurrentRefreshException(String message) {
		super(message);
	}

	public ConcurrentRefreshException(String message, Throwable cause) {
		super(message, cause);
	}

}
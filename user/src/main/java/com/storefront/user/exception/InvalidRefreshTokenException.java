package com.storefront.user.exception;

public class InvalidRefreshTokenException extends RefreshTokenException {

	private static final long serialVersionUID = 1L;

	public InvalidRefreshTokenException(String message) {
        super(message);
    }

}
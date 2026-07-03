package com.storefront.user.exception;

public class RefreshTokenNotFoundException extends RefreshTokenException {

	private static final long serialVersionUID = 1L;

	public RefreshTokenNotFoundException(String message) {
        super(message);
    }

}
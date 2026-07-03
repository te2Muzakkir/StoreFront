package com.storefront.user.exception;

public class RefreshTokenAlreadyRevokedException extends RefreshTokenException {

	private static final long serialVersionUID = 1L;

	public RefreshTokenAlreadyRevokedException(String message) {
        super(message);
    }

}
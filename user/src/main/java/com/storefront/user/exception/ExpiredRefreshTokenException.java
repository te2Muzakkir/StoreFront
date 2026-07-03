package com.storefront.user.exception;

public class ExpiredRefreshTokenException extends RefreshTokenException {

	private static final long serialVersionUID = 1L;

	public ExpiredRefreshTokenException(String message) {
        super(message);
    }

}
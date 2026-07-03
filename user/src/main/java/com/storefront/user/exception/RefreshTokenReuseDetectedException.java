package com.storefront.user.exception;

import com.storefront.user.entity.User;

public class RefreshTokenReuseDetectedException extends RefreshTokenException {

	private static final long serialVersionUID = 1L;

	private final User user;

    public RefreshTokenReuseDetectedException(String message, User user) {
        super(message);
        this.user = user;
    }

    public RefreshTokenReuseDetectedException(String message, User user, Throwable cause) {
        super(message, cause);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
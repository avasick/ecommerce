package com.example.ecommerce.exception;

public class DuplicateUserNameException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public DuplicateUserNameException() {
        super();
    }

    public DuplicateUserNameException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DuplicateUserNameException(final String message) {
        super(message);
    }

    public DuplicateUserNameException(final Throwable cause) {
        super(cause);
    }
}
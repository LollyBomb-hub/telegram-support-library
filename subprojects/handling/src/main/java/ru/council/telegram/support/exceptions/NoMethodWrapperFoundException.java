package ru.council.telegram.support.exceptions;

public class NoMethodWrapperFoundException extends RuntimeException {
    public NoMethodWrapperFoundException() {
    }

    public NoMethodWrapperFoundException(String message) {
        super(message);
    }

    public NoMethodWrapperFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMethodWrapperFoundException(Throwable cause) {
        super(cause);
    }

    public NoMethodWrapperFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

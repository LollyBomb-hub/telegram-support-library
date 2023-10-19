package ru.council.telegram.support.exceptions;

public class MethodWrapperAlreadySetException extends RuntimeException {
    public MethodWrapperAlreadySetException() {
    }

    public MethodWrapperAlreadySetException(String message) {
        super(message);
    }

    public MethodWrapperAlreadySetException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodWrapperAlreadySetException(Throwable cause) {
        super(cause);
    }

    public MethodWrapperAlreadySetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

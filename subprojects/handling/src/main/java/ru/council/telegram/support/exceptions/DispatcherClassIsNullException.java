package ru.council.telegram.support.exceptions;

public class DispatcherClassIsNullException extends RuntimeException {
    public DispatcherClassIsNullException() {
    }

    public DispatcherClassIsNullException(String message) {
        super(message);
    }

    public DispatcherClassIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public DispatcherClassIsNullException(Throwable cause) {
        super(cause);
    }

    public DispatcherClassIsNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

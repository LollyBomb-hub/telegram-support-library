package ru.council.telegram.support.exceptions;

public class DispatcherNotAnnotatedException extends RuntimeException {
    public DispatcherNotAnnotatedException() {
    }

    public DispatcherNotAnnotatedException(String message) {
        super(message);
    }

    public DispatcherNotAnnotatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DispatcherNotAnnotatedException(Throwable cause) {
        super(cause);
    }

    public DispatcherNotAnnotatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

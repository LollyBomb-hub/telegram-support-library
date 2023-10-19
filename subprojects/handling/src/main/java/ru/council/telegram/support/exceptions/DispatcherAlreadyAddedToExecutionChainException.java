package ru.council.telegram.support.exceptions;

public class DispatcherAlreadyAddedToExecutionChainException extends RuntimeException {
    public DispatcherAlreadyAddedToExecutionChainException() {
    }

    public DispatcherAlreadyAddedToExecutionChainException(String message) {
        super(message);
    }

    public DispatcherAlreadyAddedToExecutionChainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DispatcherAlreadyAddedToExecutionChainException(Throwable cause) {
        super(cause);
    }

    public DispatcherAlreadyAddedToExecutionChainException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package ru.council.telegram.support.exceptions;

public class MultipleMainDispatchersException extends RuntimeException {
    public MultipleMainDispatchersException() {
    }

    public MultipleMainDispatchersException(String message) {
        super(message);
    }

    public MultipleMainDispatchersException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleMainDispatchersException(Throwable cause) {
        super(cause);
    }

    public MultipleMainDispatchersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

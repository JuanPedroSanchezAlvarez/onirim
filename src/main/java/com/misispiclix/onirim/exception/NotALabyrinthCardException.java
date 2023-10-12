package com.misispiclix.onirim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "400 - Not a Labyrinth Card.")
public class NotALabyrinthCardException extends RuntimeException {

    public NotALabyrinthCardException() { }

    public NotALabyrinthCardException(String message) {
        super(message);
    }

    public NotALabyrinthCardException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotALabyrinthCardException(Throwable cause) {
        super(cause);
    }

    public NotALabyrinthCardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}

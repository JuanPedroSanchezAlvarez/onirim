package com.misispiclix.singleplayergames.onirim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "400 - Invalid card index.")
public class InvalidCardIndexException extends RuntimeException {

    public InvalidCardIndexException() {
    }

    public InvalidCardIndexException(String message) {
        super(message);
    }

    public InvalidCardIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCardIndexException(Throwable cause) {
        super(cause);
    }

    public InvalidCardIndexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

package com.misispiclix.singleplayergames.onirim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "400 - Not a Key Card.")
public class NotAKeyCardException extends RuntimeException {

    public NotAKeyCardException() {
    }

    public NotAKeyCardException(String message) {
        super(message);
    }

    public NotAKeyCardException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAKeyCardException(Throwable cause) {
        super(cause);
    }

    public NotAKeyCardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

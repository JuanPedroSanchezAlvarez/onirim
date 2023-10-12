package com.misispiclix.onirim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED, reason = "405 - Action not allowed.")
public class ActionNotAllowedException extends RuntimeException {

    public ActionNotAllowedException() { }

    public ActionNotAllowedException(String message) {
        super(message);
    }

    public ActionNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionNotAllowedException(Throwable cause) {
        super(cause);
    }

    public ActionNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}

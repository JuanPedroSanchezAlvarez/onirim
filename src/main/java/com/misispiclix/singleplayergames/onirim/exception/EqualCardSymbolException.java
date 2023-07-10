package com.misispiclix.singleplayergames.onirim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "400 - Equal Card Symbol.")
public class EqualCardSymbolException extends RuntimeException {

    public EqualCardSymbolException() { }

    public EqualCardSymbolException(String message) {
        super(message);
    }

    public EqualCardSymbolException(String message, Throwable cause) {
        super(message, cause);
    }

    public EqualCardSymbolException(Throwable cause) {
        super(cause);
    }

    public EqualCardSymbolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}

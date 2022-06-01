package com.ivanxc.netcracker.bookshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParameterFormatException extends RuntimeException {

    public ParameterFormatException(String message) {
        super(message);
    }
}

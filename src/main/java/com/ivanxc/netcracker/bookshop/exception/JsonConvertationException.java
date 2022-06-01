package com.ivanxc.netcracker.bookshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JsonConvertationException extends RuntimeException {

    public JsonConvertationException(String message) {
        super(message);
    }
}

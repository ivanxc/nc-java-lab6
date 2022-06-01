package com.ivanxc.netcracker.bookshop.exception;

import com.ivanxc.netcracker.bookshop.response.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.ivanxc.netcracker.bookshop.controller")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse("Incorrect request", details), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ParameterFormatException.class)
    public ResponseEntity<ErrorResponse> handleParameterFormatException(ParameterFormatException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse("Incorrect request", details), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonConvertationException.class)
    public ResponseEntity<ErrorResponse> handleJsonConvertationException(JsonConvertationException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse("Incorrect request", details), HttpStatus.BAD_REQUEST);
    }

}

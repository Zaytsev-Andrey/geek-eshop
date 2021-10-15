package ru.geekbrains.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            ProductNotFoundException.class,
            UserNotFoundException.class,
            OrderNotFoundException.class
    })
    public ResponseEntity<String> notFoundException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> defaultException(Exception ex) {
        return new ResponseEntity<>("Unknown exception", HttpStatus.BAD_REQUEST);
    }
}
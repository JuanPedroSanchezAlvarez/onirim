package com.misispiclix.onirim.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<List<Map<String, String>>> handleBindError(MethodArgumentNotValidException e) {
        List<Map<String, String>> listOfError = e.getFieldErrors().stream().map(fieldError -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            return errorMap;
        }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(listOfError);
    }

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<List<Map<String, String>>> handleJPAViolations(TransactionSystemException e) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();
        if (e.getCause().getCause() instanceof ConstraintViolationException cve) {
            List<Map<String, String>> listOfError = cve.getConstraintViolations().stream().map(constraintViolation -> {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                return errorMap;
            }).collect(Collectors.toList());
            responseEntity.body(listOfError);
        }
        return responseEntity.build();
    }

}

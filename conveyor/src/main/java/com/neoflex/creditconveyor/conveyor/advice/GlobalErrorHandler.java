package com.neoflex.creditconveyor.conveyor.advice;

import com.neoflex.creditconveyor.conveyor.error.validation.ErrorResponseValidation;
import com.neoflex.creditconveyor.conveyor.error.validation.Violation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandler {

//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponseValidation handleConstraintValidation(ConstraintViolationException e) {
//        List<Violation> violations = e.getConstraintViolations()
//                .stream()
//                .map(violation ->
//                        new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
//                .toList();
//
//        return new ErrorResponseValidation(violations);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseValidation handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        return new ErrorResponseValidation(violations);
    }
}

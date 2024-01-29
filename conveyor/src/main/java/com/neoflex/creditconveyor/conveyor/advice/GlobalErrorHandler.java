package com.neoflex.creditconveyor.conveyor.advice;

import com.neoflex.creditconveyor.conveyor.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyor.conveyor.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyor.conveyor.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyor.conveyor.error.validation.ErrorResponseValidation;
import com.neoflex.creditconveyor.conveyor.error.validation.Violation;
import com.neoflex.creditconveyor.conveyor.util.StatusConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {

    MessageInfoDto messageInfo = new MessageInfoDto(StatusConstants.DEFAULT_ERROR_CODE);

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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageInfoDto> handlePaymentNotFound(ResourceNotFoundException paymentNotFound) {
        log.debug("Input handlePaymentNotFound. paymentNotFound: {}", paymentNotFound.getMessage());
        messageInfo.setRespCode(HttpStatus.NOT_FOUND.value());
        messageInfo.setMessage(paymentNotFound.getMessage());

        log.debug("Output handlePaymentNotFound. messageInfo={ errorCode: {}, respCode: {}, message: {} }",
                messageInfo.getErrorCode(), messageInfo.getRespCode(), messageInfo.getMessage());
        return new ResponseEntity<>(messageInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseValidation> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.debug("Input handleMethodArgumentNotValid. MethodArgumentNotValidException: {}", e.getMessage());

        List<Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();
        ErrorResponseValidation errorResponseValidation = new ErrorResponseValidation(violations);

        log.debug("Output handleMethodArgumentNotValid. response: {}", violations);
        return new ResponseEntity<>(errorResponseValidation, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationAndScoringAndCalculationOfferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseValidation> handleValidationAndScoringAndCalculationOfferException(
            ValidationAndScoringAndCalculationOfferException offer
    ) {
        log.debug("Input handleValidationAndScoringAndCalculationOfferException. offer: {}", offer.getViolations());
        ErrorResponseValidation errorResponseValidation = new ErrorResponseValidation(offer.getViolations());
        return new ResponseEntity<>(errorResponseValidation, HttpStatus.NOT_FOUND);
    }
}

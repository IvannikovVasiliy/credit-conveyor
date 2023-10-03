package com.neoflex.creditconveyor.conveyor.advice;

import com.neoflex.creditconveyor.conveyor.domain.constants.StatusConstants;
import com.neoflex.creditconveyor.conveyor.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyor.conveyor.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyor.conveyor.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyor.conveyor.error.validation.ErrorResponseValidation;
import com.neoflex.creditconveyor.conveyor.error.validation.Violation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageInfoDto handlePaymentNotFound(ResourceNotFoundException paymentNotFound) {
        log.debug("Input handlePaymentNotFound. paymentNotFound: {}", paymentNotFound.getMessage());
        messageInfo.setRespCode(StatusConstants.BAD_REQUEST);
        messageInfo.setMessage(paymentNotFound.getMessage());

        log.debug("Output handlePaymentNotFound. messageInfo={ errorCode: {}, respCode: {}, message: {} }",
                messageInfo.getErrorCode(), messageInfo.getRespCode(), messageInfo.getMessage());
        return messageInfo;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseValidation handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.debug("Input handleMethodArgumentNotValid. MethodArgumentNotValidException: {}", e.getMessage());

        List<Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        log.debug("Output handleMethodArgumentNotValid. response: {}", violations);
        return new ErrorResponseValidation(violations);
    }

    @ExceptionHandler(ValidationAndScoringAndCalculationOfferException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponseValidation handleValidationAndScoringAndCalculationOfferException(
            ValidationAndScoringAndCalculationOfferException offer
    ) {
        log.debug("Input handleValidationAndScoringAndCalculationOfferException. offer: {}", offer.getViolations());
        return new ErrorResponseValidation(offer.getViolations());
    }
}

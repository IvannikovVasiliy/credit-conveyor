package com.neoflex.creditconveyer.application.advice;

import com.neoflex.creditconveyer.application.domain.constant.ErrorConstants;
import com.neoflex.creditconveyer.application.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyer.application.error.exception.BadRequestException;
import com.neoflex.creditconveyer.application.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.application.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.application.error.validation.ErrorResponseValidation;
import com.neoflex.creditconveyer.application.error.validation.Violation;
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

    MessageInfoDto messageInfo = new MessageInfoDto(ErrorConstants.DEFAULT_ERROR_CODE);

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

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageInfoDto handleBadRequest(BadRequestException e) {
        log.debug("Input handleBadRequest. BadRequestException: {}", e.getMessage());

        messageInfo.setRespCode(ErrorConstants.BAD_REQUEST);
        messageInfo.setMessage(e.getMessage());

        log.debug("Output handleBadRequest. messageInfo={ errorCode: {}, respCode: {}, message: {} }",
                messageInfo.getErrorCode(), messageInfo.getRespCode(), messageInfo.getMessage());
        return messageInfo;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public MessageInfoDto handlePaymentNotFound(ResourceNotFoundException paymentNotFound) {
        log.debug("Input handlePaymentNotFound. paymentNotFound: {}", paymentNotFound.getMessage());

        messageInfo.setRespCode(ErrorConstants.NOT_FOUND);
        messageInfo.setMessage(paymentNotFound.getMessage());

        log.debug("Output handlePaymentNotFound. messageInfo={ errorCode: {}, respCode: {}, message: {} }",
                messageInfo.getErrorCode(), messageInfo.getRespCode(), messageInfo.getMessage());
        return messageInfo;
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

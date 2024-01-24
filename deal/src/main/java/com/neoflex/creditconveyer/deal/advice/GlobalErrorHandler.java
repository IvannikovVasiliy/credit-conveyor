package com.neoflex.creditconveyer.deal.advice;

import com.neoflex.creditconveyer.deal.domain.constant.ErrorConstants;
import com.neoflex.creditconveyer.deal.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyer.deal.error.exception.*;
import com.neoflex.creditconveyer.deal.error.validation.ErrorResponseValidation;
import com.neoflex.creditconveyer.deal.error.validation.Violation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {

    MessageInfoDto messageInfo = new MessageInfoDto(ErrorConstants.DEFAULT_ERROR_CODE);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<MessageInfoDto> handlePaymentNotFound(ResourceNotFoundException paymentNotFound) {
        log.debug("Input handlePaymentNotFound. paymentNotFound: {}", paymentNotFound.getMessage());

        messageInfo.setRespCode(ErrorConstants.BAD_REQUEST);
        messageInfo.setMessage(paymentNotFound.getMessage());

        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put("Correlation-Id", List.of(paymentNotFound.getCorrelationId()));

        log.debug("Output handlePaymentNotFound. messageInfo={ errorCode: {}, respCode: {}, message: {} }",
                messageInfo.getErrorCode(), messageInfo.getRespCode(), messageInfo.getMessage());
        return new ResponseEntity<>(messageInfo, headers, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
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

    @ExceptionHandler(ApplicationIsPreapprovalException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public MessageInfoDto handleApplicationIsPreapprovalException(ApplicationIsPreapprovalException e) {
        log.debug("Input handleApplicationIsPreapprovalException. ApplicationIsPreapprovalException: {}",
                e.getMessage());

        messageInfo.setErrorCode(ErrorConstants.APPLICATION_PREAPPROVAL);
        messageInfo.setRespCode(ErrorConstants.CONFLICT);
        messageInfo.setMessage(e.getMessage());

        log.debug("Output handleApplicationIsPreapprovalException. messageInfo={ errorCode: {}, respCode: {}, message: {} }",
                messageInfo.getErrorCode(), messageInfo.getRespCode(), messageInfo.getMessage());
        return messageInfo;
    }

    @ExceptionHandler(ErrorSesCodeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public MessageInfoDto handleWrongSesCodeException(ErrorSesCodeException e) {
        log.debug("Input handleWrongSesCodeException. ErrorSesCodeException: {}", e.getMessage());

        messageInfo.setRespCode(ErrorConstants.CONFLICT);
        messageInfo.setMessage(e.getMessage());

        log.debug("Output handleWrongSesCodeException. messageInfo={ errorCode: {}, respCode: {}, message: {} }",
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

    @ExceptionHandler(ConnectionRefusedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageInfoDto handleConnectionRefusedException(ConnectionRefusedException connectException) {
        log.debug("Input handleConnectionRefusedException. exception: {}", connectException.getMessage());
        messageInfo.setRespCode(ErrorConstants.INTERNAL_SERVER_ERROR);
        messageInfo.setMessage(connectException.getMessage());
        return messageInfo;
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageInfoDto handleBaseConnectRefusedException(ConnectException connectException) {
        log.debug("Input handleBaseConnectRefusedException. exception: {}", connectException.getMessage());
        messageInfo.setRespCode(ErrorConstants.INTERNAL_SERVER_ERROR);
        messageInfo.setMessage(connectException.getMessage());
        return messageInfo;
    }
}

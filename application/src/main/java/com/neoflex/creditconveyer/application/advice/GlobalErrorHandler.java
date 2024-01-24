package com.neoflex.creditconveyer.application.advice;

import com.neoflex.creditconveyer.application.domain.constant.ErrorConstants;
import com.neoflex.creditconveyer.application.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyer.application.error.exception.BadRequestException;
import com.neoflex.creditconveyer.application.error.exception.ConnectionRefusedException;
import com.neoflex.creditconveyer.application.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.application.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.application.error.validation.ErrorResponseValidation;
import com.neoflex.creditconveyer.application.error.validation.Violation;
import com.neoflex.creditconveyer.application.http.HttpConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.HeaderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
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
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<MessageInfoDto> handlePaymentNotFound(ResourceNotFoundException paymentNotFound,
                                                               HttpServletRequest httpServletRequest) {
        log.debug("Input handlePaymentNotFound. paymentNotFound: {}", paymentNotFound.getMessage());

        messageInfo.setRespCode(ErrorConstants.NOT_FOUND);
        messageInfo.setMessage(paymentNotFound.getMessage());

        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        headers.put(HttpConfig.CORRELATION_ID_HEADER_CONFIG, List.of(applicationId.toString()));

        log.debug("Output handlePaymentNotFound. messageInfo={ errorCode: {}, respCode: {}, message: {} }",
                messageInfo.getErrorCode(), messageInfo.getRespCode(), messageInfo.getMessage());
        return new ResponseEntity<>(messageInfo, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConnectionRefusedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageInfoDto handleConnectionRefusedException(ConnectionRefusedException connectException) {
        log.debug("Input handleConnectionRefusedException. exception: {}", connectException.getMessage());
        messageInfo.setRespCode(ErrorConstants.INTERNAL_SERVER_ERROR);
        messageInfo.setMessage(connectException.getMessage());
        return messageInfo;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageInfoDto handleRuntime(RuntimeException e) {
        log.debug("Input handleRuntime. paymentNotFound: {}", e.getMessage());

        messageInfo.setRespCode(ErrorConstants.BAD_REQUEST);
        messageInfo.setMessage(e.getMessage());

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

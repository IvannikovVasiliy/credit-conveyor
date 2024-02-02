package com.neoflex.creditconveyer.application.error.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyer.application.dto.MessageInfoDto;
import com.neoflex.creditconveyer.application.error.exception.BadRequestException;
import com.neoflex.creditconveyer.application.error.exception.ConnectionRefusedException;
import com.neoflex.creditconveyer.application.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.application.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.application.error.validation.ErrorResponseValidation;
import com.neoflex.creditconveyer.application.utils.HttpConfig;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class RetrieveMessageErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.debug("Input decode exception");
        byte[] responseBytes = null;
        try {
            responseBytes = response.body().asInputStream().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        String error = null;
        MessageInfoDto messageInfo = null;
        ErrorResponseValidation errorResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            messageInfo = mapper.readValue(responseBytes, MessageInfoDto.class);
            error = messageInfo.getMessage();
        } catch (Exception e) {
            try {
                errorResponse = mapper.readValue(responseBytes, ErrorResponseValidation.class);
            } catch (IOException ex) {
                throw new RuntimeException(e.getMessage());
            }
        }

        switch (response.status()) {
            case 400:
                if (null != messageInfo) {
                    throw new BadRequestException(null != error ? error : "Bad request");
                }
                if (null != errorResponse) {
                    throw new ValidationAndScoringAndCalculationOfferException(new ErrorResponseValidation(errorResponse.getViolations()));
                }
            case 404:
                throw new ResourceNotFoundException(
                        null != error ? error : "Not Found",
                        response.headers().get(HttpConfig.CORRELATION_ID_HEADER_CONFIG).stream().findFirst().get()
                );
            case 500:
                throw new ConnectionRefusedException(messageInfo.getMessage());
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}

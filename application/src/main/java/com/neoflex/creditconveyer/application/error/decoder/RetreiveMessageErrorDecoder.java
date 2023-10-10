package com.neoflex.creditconveyer.application.error.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyer.application.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyer.application.error.exception.BadRequestException;
import com.neoflex.creditconveyer.application.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.application.error.validation.ErrorResponseValidation;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.webjars.NotFoundException;

import java.io.IOException;

public class RetreiveMessageErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
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
                    throw new ValidationAndScoringAndCalculationOfferException(errorResponse.getViolations());
                }
            case 404:
                return new NotFoundException(null != error ? error : "Not Found");
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}

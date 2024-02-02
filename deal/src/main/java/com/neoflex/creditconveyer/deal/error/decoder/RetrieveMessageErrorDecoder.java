package com.neoflex.creditconveyer.deal.error.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyer.deal.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyer.deal.error.exception.BadRequestException;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.deal.error.validation.ErrorResponseValidation;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class RetrieveMessageErrorDecoder implements ErrorDecoder {

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
        ErrorResponseValidation errorResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            MessageInfoDto message = mapper.readValue(responseBytes, MessageInfoDto.class);
            error = message.getMessage();
        } catch (Exception e) {
            try {
                errorResponse = mapper.readValue(responseBytes, ErrorResponseValidation.class);
            } catch (IOException ex) {
                throw new RuntimeException(e.getMessage());
            }
        }

        switch (response.status()) {
            case 400:
                if (null == error) {
                    throw new ValidationAndScoringAndCalculationOfferException(errorResponse.getViolations());
                } else {
                    throw new BadRequestException(null != error ? error : "Bad request");
                }
            case 404:
                throw new ResourceNotFoundException(null != error ? error : "Not Found");
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}
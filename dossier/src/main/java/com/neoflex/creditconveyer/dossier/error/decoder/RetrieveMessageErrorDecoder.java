package com.neoflex.creditconveyer.dossier.error.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyer.dossier.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyer.dossier.error.exception.BadRequestException;
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            MessageInfoDto message = mapper.readValue(responseBytes, MessageInfoDto.class);
            error = message.getMessage();
        } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
        }

        switch (response.status()) {
            case 400:
                return new BadRequestException(null != error ? error : "Bad request");
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}

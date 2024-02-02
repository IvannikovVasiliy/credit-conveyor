package com.neoflex.creditconveyer.deal.error.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyer.deal.domain.constant.ErrorConstants;
import com.neoflex.creditconveyer.deal.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyer.deal.error.exception.BadRequestException;
import com.neoflex.creditconveyer.deal.error.exception.ConnectionRefusedException;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.deal.error.validation.ErrorResponseValidation;
import feign.Request;
import feign.Response;
import feign.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
//@SpringBootTest
class RetrieveMessageErrorDecoderTest {

    private final RetrieveMessageErrorDecoder retrieveMessageErrorDecoder = new RetrieveMessageErrorDecoder();

//    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void decodeTest() throws IOException {
        MessageInfoDto messageInfoBadRequest =
                new MessageInfoDto(ErrorConstants.DEFAULT_ERROR_CODE, HttpStatus.BAD_REQUEST.value(), "Bad Request");
        ErrorResponseValidation errorResponseValidation = new ErrorResponseValidation();
//
        Response invalidResponse = Response
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .body(Mockito.anyString(), StandardCharsets.UTF_8)
                .build();
        Response validationBadRequestResponse = Response
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .body(objectMapper.writeValueAsBytes(errorResponseValidation))
                .build();
        Response messageInfoDtoBadRequestResponse = Response
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .body(objectMapper.writeValueAsBytes(messageInfoBadRequest))
                .build();
        Response notFoundResponse = Response
                .builder()
                .status(HttpStatus.NOT_FOUND.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .body(objectMapper.writeValueAsBytes(messageInfoBadRequest))
                .build();
//
        String serviceName = "qwerty";
//
        assertThrows(
                RuntimeException.class,
                () -> retrieveMessageErrorDecoder.decode(serviceName, invalidResponse)
        );
        assertThrows(
                BadRequestException.class,
                () -> retrieveMessageErrorDecoder.decode(serviceName, messageInfoDtoBadRequestResponse));
        assertThrows(
                ValidationAndScoringAndCalculationOfferException.class,
                () -> retrieveMessageErrorDecoder.decode(serviceName, validationBadRequestResponse));
        assertThrows(
                ResourceNotFoundException.class,
                () -> retrieveMessageErrorDecoder.decode(serviceName, notFoundResponse));
    }
}
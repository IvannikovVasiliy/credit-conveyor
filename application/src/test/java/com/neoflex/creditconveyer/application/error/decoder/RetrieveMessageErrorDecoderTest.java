package com.neoflex.creditconveyer.application.error.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyer.application.domain.dto.MessageInfoDto;
import com.neoflex.creditconveyer.application.error.exception.BadRequestException;
import com.neoflex.creditconveyer.application.error.exception.ConnectionRefusedException;
import com.neoflex.creditconveyer.application.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.application.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.application.error.validation.ErrorResponseValidation;
import com.neoflex.creditconveyer.application.http.HttpConfig;
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
@SpringBootTest
public class RetrieveMessageErrorDecoderTest {

    private final RetrieveMessageErrorDecoder retrieveMessageErrorDecoder = new RetrieveMessageErrorDecoder();

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void decodeTest() throws IOException {
        MessageInfoDto messageInfoDto = new MessageInfoDto();
        ErrorResponseValidation errorResponseValidation = new ErrorResponseValidation();
        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        headers.put(HttpConfig.CORRELATION_ID_HEADER_CONFIG, List.of("1"));

        Response internalServerErrorResponse = Response
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(objectMapper.writeValueAsBytes(messageInfoDto))
                .build();
        Response invalidResponse = Response
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(Mockito.anyString(), StandardCharsets.UTF_8)
                .build();
        Response validationBadRequestResponse = Response
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(objectMapper.writeValueAsBytes(errorResponseValidation))
                .build();
        Response messageInfoDtoBadRequestResponse = Response
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(objectMapper.writeValueAsBytes(messageInfoDto))
                .build();
        Response notFoundResponse = Response
                .builder()
                .status(HttpStatus.NOT_FOUND.value())
                .request(Request
                        .create(Request.HttpMethod.GET, Mockito.anyString(), Collections.emptyMap(), null, Util.UTF_8))
                .headers(headers)
                .body(objectMapper.writeValueAsBytes(messageInfoDto))
                .build();

        assertThrows(
                ConnectionRefusedException.class,
                () -> retrieveMessageErrorDecoder.decode(Mockito.anyString(), internalServerErrorResponse)
        );
        assertThrows(
                RuntimeException.class,
                () -> retrieveMessageErrorDecoder.decode(Mockito.anyString(), invalidResponse)
        );
        assertThrows(
                BadRequestException.class,
                () -> retrieveMessageErrorDecoder.decode(Mockito.anyString(), messageInfoDtoBadRequestResponse));
        assertThrows(
                ValidationAndScoringAndCalculationOfferException.class,
                () -> retrieveMessageErrorDecoder.decode(Mockito.anyString(), validationBadRequestResponse));
        assertThrows(
                ResourceNotFoundException.class,
                () -> retrieveMessageErrorDecoder.decode(Mockito.anyString(), notFoundResponse));
    }
}

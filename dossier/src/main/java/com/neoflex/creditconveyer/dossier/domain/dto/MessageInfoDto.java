package com.neoflex.creditconveyer.dossier.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageInfoDto {
    public MessageInfoDto(Integer errorCode, Integer respCode, String message) {
        this.errorCode = errorCode;
        this.respCode = respCode;
        this.message = message;
    }

    public MessageInfoDto(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public MessageInfoDto() {
    }

    private Integer errorCode;
    private Integer respCode;
    private String message;
}
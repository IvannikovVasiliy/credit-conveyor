package com.neoflex.creditconveyer.dossier.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomEmailMessage {

    public CustomEmailMessage(String email, String theme, String text) {
        this.email = email;
        this.theme = theme;
        this.text = text;
    }

    public CustomEmailMessage() {
    }

    private String email;
    private String theme;
    private String text;
}

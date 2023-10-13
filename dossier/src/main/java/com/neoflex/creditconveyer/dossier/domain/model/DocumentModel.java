package com.neoflex.creditconveyer.dossier.domain.model;

import com.neoflex.creditconveyer.dossier.service.impl.DocumentServiceImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentModel {

    public DocumentModel(String fileName, String text) {
        this.fileName = fileName;
        this.text = text;
    }

    private String text;
    private String fileName;
}

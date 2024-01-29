package com.neoflex.creditconveyer.dossier.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentModel {

    public DocumentModel(String fileName, String fileText) {
        this.fileName = fileName;
        this.fileText = fileText;
    }

    private String fileText;
    private String fileName;
}

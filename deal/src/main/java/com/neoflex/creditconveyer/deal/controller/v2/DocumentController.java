package com.neoflex.creditconveyer.deal.controller.v2;

import com.neoflex.creditconveyer.deal.service.DealService;
import com.neoflex.creditconveyer.deal.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v2/deal/document")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/{applicationId}/send")
    public ResponseEntity<Void> sendDocuments() {
        documentService.
    }

    @PostMapping("/{applicationId}/sign")
    public ResponseEntity<Void> signDocuments() {

    }

    @PostMapping("/{applicationId}/code")
    public ResponseEntity<Void> codeDocuments() {

    }
}

package com.neoflex.creditconveyer.deal.controller.v2;

import com.neoflex.creditconveyer.deal.domain.dto.VerifyCodeDTO;
import com.neoflex.creditconveyer.deal.service.DocumentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v2/deal/document")
public class DocumentV2Controller {

    private final DocumentService documentService;

    @PostMapping("/{applicationId}/send")
    public ResponseEntity<Void> sendDocuments(@NotNull @PathVariable Long applicationId) {
        log.debug("Request sendDocuments. applicationId: {}", applicationId);
        documentService.sendDocuments(applicationId);

        log.debug("Response sendDocuments. OK");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicationId}/sign")
    public ResponseEntity<Void> signDocuments(@NotNull @PathVariable Long applicationId) {
        log.debug("Request signDocuments. applicationId: {}", applicationId);

        documentService.signDocuments(applicationId);

        log.debug("Response signDocuments. OK");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{applicationId}/code")
    public ResponseEntity<Void> signAllDocuments(@NotNull@PathVariable Long applicationId,
                                                 @Valid @RequestBody VerifyCodeDTO verifyCodeDTO) {
        log.debug("Request signAllDocuments. sesCode: {}", verifyCodeDTO.getSesCode());

        documentService.issuedCredit(applicationId, verifyCodeDTO);

        log.debug("Response signAllDocuments. OK");
        return ResponseEntity.ok().build();
    }
}

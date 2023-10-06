package com.neoflex.creditconveyer.deal.controller.v2;

import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.service.DealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/deal/document")
public class DocumentController {

    @Qualifier("dealSenderEmailServiceImpl") private final DealService dealService;

//    @PostMapping("/document/{applicationId}/send")
//    public ResponseEntity<> sendDocuments() {
//
//    }
//
//    @PostMapping("/document/{applicationId}/sign")
//    public ResponseEntity<> signDocuments() {
//
//    }
//
//    @PostMapping("/document/{applicationId}/code")
//    public ResponseEntity<> codeDocuments() {
//
//    }

    @PutMapping("/offer")
    public ResponseEntity<Void> chooseOffer(@Valid @RequestBody LoanOfferDTO loanOffer) {
        log.debug("Request chooseOffer. loanOffer={applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient: {}}",
                loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient());

        dealService.chooseOffer(loanOffer);

        log.info("Response chooseOffer");
        return ResponseEntity.ok().build();
    }
}

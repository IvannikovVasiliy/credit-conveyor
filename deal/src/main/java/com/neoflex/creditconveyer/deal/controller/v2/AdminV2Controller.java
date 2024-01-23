package com.neoflex.creditconveyer.deal.controller.v2;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationResponseDto;
import com.neoflex.creditconveyer.deal.domain.dto.PageDto;
import com.neoflex.creditconveyer.deal.http.HttpConfig;
import com.neoflex.creditconveyer.deal.service.AdminService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v2/deal/admin")
public class AdminV2Controller {

    private final AdminService adminService;

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<LoanApplicationResponseDto> getApplicationById(@NotNull @PathVariable Long applicationId) {
        log.debug("Request for getting application by id. applicationId={}", applicationId);

        LoanApplicationResponseDto loanApplicationResponseDto = adminService.getApplicationById(applicationId);

        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put("Correlation-Id", List.of(applicationId.toString()));

        log.debug("Response loan-application by applicationId={}", applicationId);

        return new ResponseEntity<>(loanApplicationResponseDto, headers, HttpStatusCode.valueOf(HttpConfig.STATUS_OK));
    }

    @GetMapping("/application")
    public ResponseEntity<List<LoanApplicationResponseDto>> getAllApplications(PageDto page) {
        log.debug("Request to receive all applications");

        adminService.getAllApplications(page);

        return null;
    }

    @PutMapping("/application/{applicationId}/status")
    public ResponseEntity<Void> updateStatusByApplicationId(@PathVariable Long applicationId) {
        log.debug("Request updateStatusByApplicationId. applicationId={}", applicationId);

        adminService.updateStatusByApplicationId(applicationId);

        log.debug("Response updateStatusByApplicationId. OK");
        return ResponseEntity.ok().build();
    }
}

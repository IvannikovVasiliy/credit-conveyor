package com.neoflex.creditconveyer.deal.controller.v2;

import com.neoflex.creditconveyer.deal.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v2/deal/admin")
public class AdminV2Controller {

    private final AdminService adminService;

    @PutMapping("/application/{applicationId}/status")
    public ResponseEntity<Void> updateStatusByApplicationId(@PathVariable Long applicationId) {
        log.debug("Request updateStatusByApplicationId. applicationId={}", applicationId);

        adminService.updateStatusByApplicationId(applicationId);

        log.debug("Response updateStatusByApplicationId. OK");
        return ResponseEntity.ok().build();
    }
}

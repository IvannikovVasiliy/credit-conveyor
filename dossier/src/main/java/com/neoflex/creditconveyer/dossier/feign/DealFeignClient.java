package com.neoflex.creditconveyer.dossier.feign;

import com.neoflex.creditconveyer.dossier.config.DecoderConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "dealFeign", url = "${dealService.hostPort}/v2/deal", configuration = DecoderConfiguration.class)
@Validated
public interface DealFeignClient {

    @PutMapping("/admin/application/{applicationId}/status")
    ResponseEntity<Void> updateStatusApplicationById(@PathVariable Long applicationId);
}

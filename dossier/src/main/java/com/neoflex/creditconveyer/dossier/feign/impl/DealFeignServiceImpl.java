package com.neoflex.creditconveyer.dossier.feign.impl;

import com.neoflex.creditconveyer.dossier.feign.DealFeignClient;
import com.neoflex.creditconveyer.dossier.feign.DealFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealFeignServiceImpl implements DealFeignService {

    private final DealFeignClient dealFeignClient;

    @Override
    public void updateStatus(Long applicationId) {
        log.debug("Input updateStatus. applicationId={}", applicationId);

        dealFeignClient.updateStatusApplicationById(applicationId);

        log.debug("Output updateStatus. applicationId={} was updated", applicationId);
    }
}

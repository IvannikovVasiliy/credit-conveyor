package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import com.neoflex.creditconveyer.deal.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final ApplicationRepository applicationRepository;

    @Override
    public void updateStatusByApplicationId(Long applicationId) {
        log.debug("Input updateStatusByApplicationId. applicationId={}", applicationId);

        applicationRepository.findById(applicationId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Not found. Application with id=%s not found", applicationId)));

        log.debug("Output updateStatusByApplicationId. Success");
    }
}

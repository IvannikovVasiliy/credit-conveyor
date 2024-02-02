package com.neoflex.creditconveyer.deal.service;

import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final ApplicationRepository applicationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveApplication(ApplicationEntity application) {
        log.debug("Input saveApplication");
        applicationRepository.save(application);
        log.debug("Output saveApplication. Success");
    }
}

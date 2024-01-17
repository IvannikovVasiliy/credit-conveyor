package com.neoflex.creditconveyer.deal.service;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationResponseDto;

public interface AdminService {
    void updateStatusByApplicationId(Long applicationId);

    LoanApplicationResponseDto getApplicationById(Long applicationId);
}

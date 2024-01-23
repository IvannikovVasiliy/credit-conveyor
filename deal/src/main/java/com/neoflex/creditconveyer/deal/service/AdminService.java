package com.neoflex.creditconveyer.deal.service;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationResponseDto;
import com.neoflex.creditconveyer.deal.domain.dto.PageDto;

import java.util.List;

public interface AdminService {
    void updateStatusByApplicationId(Long applicationId);

    LoanApplicationResponseDto getApplicationById(Long applicationId);

    List<LoanApplicationResponseDto> getAllApplications(PageDto page);
}

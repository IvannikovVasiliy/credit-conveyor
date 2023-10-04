package com.neoflex.creditconveyer.deal.mapper;

import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import org.mapstruct.Mapper;

@Mapper
public interface SourceMapper {
    ClientEntity sourceToClientEntity(LoanApplicationRequestDTO loanApplicationRequest);
    CreditEntity sourceToCreditEntity(CreditDTO creditDTO);
}

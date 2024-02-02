package com.neoflex.creditconveyer.deal.mapper;

import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.EmploymentDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import com.neoflex.creditconveyer.deal.domain.jsonb.EmploymentJsonb;
import com.neoflex.creditconveyer.deal.domain.model.ApplicationModel;
import com.neoflex.creditconveyer.deal.domain.model.ClientModel;
import com.neoflex.creditconveyer.deal.domain.model.CreditModel;
import org.mapstruct.Mapper;

@Mapper
public interface SourceMapper {
    ClientEntity sourceToClientEntity(LoanApplicationRequestDTO loanApplicationRequest);
    CreditEntity sourceToCreditEntity(CreditDTO creditDTO);
    EmploymentJsonb sourceToEmploymentJsonb(EmploymentDTO employmentDTO);
    ClientModel sourceToClientModel(ClientEntity clientEntity);
    ApplicationModel sourceToApplicationModel(ApplicationEntity applicationEntity);
    CreditModel sourceToCreditModel(CreditEntity creditEntity);
}

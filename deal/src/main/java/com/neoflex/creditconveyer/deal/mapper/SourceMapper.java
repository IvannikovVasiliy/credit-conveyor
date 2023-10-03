package com.neoflex.creditconveyer.deal.mapper;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import org.mapstruct.Mapper;

@Mapper
public interface SourceMapper {
    ClientEntity sourceToEntity(LoanApplicationRequestDTO loanApplicationRequest);
//    SimpleDestination sourceToDestination(SimpleSource source);
//    SimpleSource destinationToSource(SimpleDestination destination);
}

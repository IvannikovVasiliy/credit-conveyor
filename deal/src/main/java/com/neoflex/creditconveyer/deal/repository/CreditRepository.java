package com.neoflex.creditconveyer.deal.repository;

import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<CreditEntity, Long> {
}

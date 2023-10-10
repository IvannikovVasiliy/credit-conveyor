package com.neoflex.creditconveyer.deal.repository;

import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
}

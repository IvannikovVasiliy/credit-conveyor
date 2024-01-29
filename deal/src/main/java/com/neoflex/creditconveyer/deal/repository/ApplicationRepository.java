package com.neoflex.creditconveyer.deal.repository;

import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    Optional<ApplicationEntity> findById(Long id);
}

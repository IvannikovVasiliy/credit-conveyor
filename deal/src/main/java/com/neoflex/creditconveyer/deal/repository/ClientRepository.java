package com.neoflex.creditconveyer.deal.repository;

import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
}

package com.neoflex.creditconveyer.dossier.repository;

import com.neoflex.creditconveyer.dossier.domain.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
}

package com.bnym.poc.repository;

import com.bnym.poc.model.CRBInventoryAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CRBInventoryAuditRepository extends JpaRepository<CRBInventoryAudit, Integer> {

}

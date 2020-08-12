package com.bnym.poc.repository;

import com.bnym.poc.model.CRBInventoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CRBInventoryMasterRepository extends JpaRepository<CRBInventoryMaster, Integer> {


}

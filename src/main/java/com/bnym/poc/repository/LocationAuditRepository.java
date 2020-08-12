package com.bnym.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bnym.poc.model.LocationAudit;


@Repository
public interface LocationAuditRepository extends JpaRepository<LocationAudit, Integer> {

}

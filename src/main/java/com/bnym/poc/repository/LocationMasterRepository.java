package com.bnym.poc.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bnym.poc.model.LocationMaster;


@Repository
public interface LocationMasterRepository  extends JpaRepository<LocationMaster, String> {

}

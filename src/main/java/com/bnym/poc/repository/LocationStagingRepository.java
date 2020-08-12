package com.bnym.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bnym.poc.model.LocationStaging;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface LocationStagingRepository extends JpaRepository<LocationStaging, Integer> {
	LocationStaging findById(int id);
	List<LocationStaging> findByLocationName(String location);
	
	@Transactional
	@Modifying
	@Query("update LocationStaging l set l.normalizedLocation  = ?1 , l.modifiedDate= ?2 where l.locationName = ?3")
	void updateNormalizedLocation(String normalizedLoc, Date modifiedDate, String locationName);
	
	@Query("from LocationStaging l where l.status = ?1")
	List<LocationStaging> findByStatus(String normalizedLoc);
	
	@Transactional
	@Modifying
	@Query("update LocationStaging l set l.status  = ?1 , l.modifiedDate= ?2, l.rejectionNotes = ?3 where l.id = ?4")
	void updateStatus(String status, Date modifiedDate,String notes, int id);
	
	
}
package com.bnym.poc.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.bnym.poc.model.LocationStaging;

public interface LocationNormalizationService {
	
	public String parseExcelFile(MultipartFile file);
	
	public List<LocationStaging> getAllStaging();
	
    public List<LocationStaging> getAllApproval();
    
	public String approveReject(LocationStaging location,String status);
	
   	public String inApproval(List<LocationStaging> data);
   	
   	public String updateNormalized(LocationStaging location);
   	
	public String uploadData (LocationStaging location);



}

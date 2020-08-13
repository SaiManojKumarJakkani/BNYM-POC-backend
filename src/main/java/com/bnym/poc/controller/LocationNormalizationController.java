package com.bnym.poc.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bnym.poc.message.ResponseMessage;
import com.bnym.poc.model.LocationAudit;
import com.bnym.poc.model.LocationMaster;
import com.bnym.poc.model.LocationStaging;
import com.bnym.poc.repository.LocationAuditRepository;
import com.bnym.poc.repository.LocationMasterRepository;
import com.bnym.poc.repository.LocationStagingRepository;
import com.bnym.poc.service.LocationNormalizationService;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/location")
public class LocationNormalizationController {
	@Autowired
	LocationNormalizationService locationNormalizationService;
	@Autowired
	LocationStagingRepository locStagingRepo;
	
	@PostMapping("/uploaddetails")
	public ResponseEntity uploadFile(@RequestBody MultipartFile file) throws IOException {
		String message="";
		try {
			if((file.getOriginalFilename().split("\\.")[1]).equals("xlsx")||(file.getOriginalFilename().split("\\.")[1]).equals("xls"))
				{
				message = locationNormalizationService.parseExcelFile(file);
				}
			else
				message = "Please upload the file of Excel format!!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			}
		catch (Exception e){
			message = "Upload failed, Please try again!!";
            return  ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
	}
	
	@PostMapping("/savedetails")
	public ResponseEntity uploadData (@RequestBody LocationStaging location){
        String message = locationNormalizationService.uploadData(location);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}
    @PutMapping("/updateNormalized")
   	public ResponseEntity updateNormalized(@RequestBody LocationStaging location){
        String message = locationNormalizationService.updateNormalized(location);
        
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

    }
	@GetMapping("/allStaging")
    public ResponseEntity<List<LocationStaging>> getstaging(){
        return new ResponseEntity<>(locationNormalizationService.getAllStaging(), HttpStatus.OK);
	}
	@GetMapping("/allInApproval")
    public ResponseEntity<List<LocationStaging>> getApproval(){
        return new ResponseEntity<>(locationNormalizationService.getAllApproval(), HttpStatus.OK);
	}
	@PutMapping("/approveorrejectall/{status}")
    public  ResponseEntity approveRejectAll(@RequestBody List<LocationStaging> locations,@PathVariable("status")String status){
	    String message = locationNormalizationService.approveRejectAll(locations, status);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}
    @PutMapping("/approveorreject/{status}")
	public ResponseEntity approveReject(@RequestBody LocationStaging location,@PathVariable("status")String status){
       String message = locationNormalizationService.approveReject(location, status);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }
    
    @PutMapping("/toInApproval")
   	public ResponseEntity inApproval(@RequestBody List<LocationStaging> data){
        String message = locationNormalizationService.inApproval(data);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

    }
    @GetMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile() throws Exception{
    	return locationNormalizationService.downloadFile();
    	//return locationNormalizationService.downloadFile();
       // String message = "Sucess";
       // return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }

}

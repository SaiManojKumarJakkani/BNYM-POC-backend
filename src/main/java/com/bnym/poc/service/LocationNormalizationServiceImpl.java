package com.bnym.poc.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;


import com.bnym.poc.model.LocationAudit;
import com.bnym.poc.model.LocationMaster;
import com.bnym.poc.model.LocationStaging;
import com.bnym.poc.repository.LocationAuditRepository;
import com.bnym.poc.repository.LocationMasterRepository;
import com.bnym.poc.repository.LocationStagingRepository;

@Service
public class LocationNormalizationServiceImpl implements LocationNormalizationService {
	
	@Autowired
	LocationStagingRepository locStagingRepo;
	@Autowired
	LocationAuditRepository locAuditRepo;
	@Autowired
	LocationMasterRepository locMasterRepo;
	
	@Override
    public List<LocationStaging> getAllStaging(){
        List<LocationStaging> approvalList = locStagingRepo.findAll();
        return approvalList;
	}
	
	@Override
    public List<LocationStaging> getAllApproval(){
        List<LocationStaging> approvalList = locStagingRepo.findByStatus("IN_APPROVAL");
        return approvalList;
	}
	
	@Override
	public String approveReject(LocationStaging location,String status){
    	LocationStaging locStaging = locStagingRepo.findById(location.getId());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        if(status.equals("approve"))
        {
        	locStagingRepo.updateStatus("APPROVED",date,null,location.getId());
	        locAuditRepo.save(new LocationAudit(locStaging.getLocationName(),locStaging.getNormalizedLocation(),"APPROVED",location.getId()));
        	locMasterRepo.save(new LocationMaster(locStaging.getId(),locStaging.getLocationName(),locStaging.getNormalizedLocation(),date));
        	return "Sucessfully APPROVED the data!";
        }
        else
        {
        	locStagingRepo.updateStatus("REJECTED",date,location.getRejectionNotes(),location.getId());
	        locAuditRepo.save(new LocationAudit(locStaging.getLocationName(),locStaging.getNormalizedLocation(),"REJECTED",location.getId()));
        	return "Sucessfully REJECTED the data!";

        }  
    }
	
	@Override
   	public String inApproval(List<LocationStaging> data){
    	LocationStaging locStaging = null;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
    	for(LocationStaging l:data )
    	{   
    		
    		date = new Date();
    	    locStaging = locStagingRepo.findById(l.getId());
        	locStagingRepo.updateStatus("IN_APPROVAL",date,null,l.getId());
	        locAuditRepo.save(new LocationAudit(locStaging.getLocationName(),locStaging.getNormalizedLocation(),"IN_APPROVAL",locStaging.getId()));
    	}
    	return "Sucessfully moved the data for Approval!";
    }
	
	@Override
   	public String updateNormalized(LocationStaging location){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
		if(!locStagingRepo.findByLocationName(location.getLocationName()).isEmpty())
		{
        locStagingRepo.updateNormalizedLocation(location.getNormalizedLocation(),date,location.getLocationName());

		return "Successfully updated the Normalized Location!";
		}
		else
		{
            return "Location Name is not existed!!";
		}
    }
	
	@Override
	public String uploadData (LocationStaging location){
			String locationName = location.getLocationName();
			String normalized = location.getNormalizedLocation();
			if(locStagingRepo.findByLocationName(locationName).isEmpty())
			{
				LocationStaging locStaging=new LocationStaging();
				locStaging.setLocationName(locationName);
				locStaging.setNormalizedLocation(normalized);
				locStagingRepo.save(locStaging);
				return "Successfully saved the data!";
			}
			else {
	            return "Details already existed!!";
			}	
	}

	 @Override
	 public String parseExcelFile(MultipartFile file) {
		int insertCount=0, ignoreCount=0;
			Workbook workbook = getWorkBook(file);
	        Sheet sheet = workbook.getSheetAt(0);
	        Iterator<Row> rows = sheet.iterator();
	        
	        rows.next();
            List<LocationStaging> locStagingList = new ArrayList<>();
	        while (rows.hasNext()) {
	            Row row = rows.next();
	            LocationStaging locStaging = new LocationStaging();
	            Iterator<Cell> cellIterator = row.cellIterator();
	            
	            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	            Date date = new Date();
	            
	            while(cellIterator.hasNext()){
	                Cell cell = cellIterator.next();
	                int cellIndex = cell.getColumnIndex();
	                
                	if(cellIndex==0 && !locStagingRepo.findByLocationName(getCellValue(cell)).isEmpty()) {
                		ignoreCount++;
                		break;
                	}
                	
               	switch(cellIndex){
	                    case 0:
	                    	//System.out.print(getCellValue(cell)+" \t ");
	                        locStaging.setLocationName(getCellValue(cell));
	                        break;
	                    case 1:
	                    	//System.out.println(getCellValue(cell));
	                        locStaging.setNormalizedLocation(getCellValue(cell));
	                        break;
	                }
	            }
	            if(locStaging.getLocationName()!=null)
	            {	
	            	insertCount++;
	            	locStagingList.add(locStaging);
	            }
	        }
	        if(!CollectionUtils.isEmpty(locStagingList)) {
	        	locStagingRepo.saveAll(locStagingList);
	        }
	        //System.out.println(insertCount+" "+ignoreCount);
	        
		 if(ignoreCount==0)
			 return "Uploaded Successfully and the data is recorded!!";
		 else if(insertCount==0)
			 return "All the records are already existed!!";
		 else
			 return ""+insertCount+" records got inserted and "+ignoreCount+" records are already existed!!";
	 }
	private static String getCellValue(Cell cell){
	        if(cell.getCellType() == CellType.STRING){
	            return cell.getStringCellValue();
	        }
	        else if(cell.getCellType() == CellType.NUMERIC){
	            return cell.getNumericCellValue()+"";
	        }
	        return null;
	}
	private Workbook getWorkBook(MultipartFile file) {
	        Workbook workbook = null;
	        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
	        try {
	            if (extension.equalsIgnoreCase("xls")) {
	               workbook = new HSSFWorkbook(file.getInputStream());

	            } else if (extension.equalsIgnoreCase("xlsx")) {
	               workbook = new XSSFWorkbook(file.getInputStream());
	            }
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	        return workbook;
	 }

}
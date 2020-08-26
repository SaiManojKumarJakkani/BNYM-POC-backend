package com.bnym.poc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	
	//Getting all records from Location_Staging
	@Override
    public List<LocationStaging> getAllStaging(){
        List<LocationStaging> approvalList = locStagingRepo.findAll();
        return approvalList;
	}
	
	//Getting all records from Location_Staging having status as In Approval
	@Override
    public List<LocationStaging> getAllApproval(){
        List<LocationStaging> approvalList = locStagingRepo.findByStatus("IN_APPROVAL");
        return approvalList;
	}
	
	//Aproving or Rejecting all the records of status InApproval
	@Override
	public String approveRejectAll(List<LocationStaging> locations,String status){
		
    	LocationStaging locStaging = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        if(status.equals("approve"))
        {	for(LocationStaging location:locations) {
        	locStaging = locStagingRepo.findById(location.getId());
        	locStagingRepo.updateStatus("APPROVED",date,null,location.getId());
	        locAuditRepo.save(new LocationAudit(locStaging.getLocationName(),locStaging.getNormalizedLocation(),"APPROVED",location.getId()));
        	locMasterRepo.save(new LocationMaster(locStaging.getId(),locStaging.getLocationName(),locStaging.getNormalizedLocation(),date));
        }
        	return "Sucessfully APPROVED the data!";
        }
        else
        {	
            for(LocationStaging location:locations) {
        	locStaging = locStagingRepo.findById(location.getId());
        	locStagingRepo.updateStatus("REJECTED",date,location.getRejectionNotes(),location.getId());
	        locAuditRepo.save(new LocationAudit(locStaging.getLocationName(),locStaging.getNormalizedLocation(),"REJECTED",location.getId()));
            }
        	return "Sucessfully REJECTED the data!";
        }  
    }
	
	//Approve or Reject a record
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
	
	//Moving data to InApproval status
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
	
	//Edit Normalized Location of a record
	@Override
   	public String updateNormalized(LocationStaging location){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
		if(locStagingRepo.findByLocationName(location.getLocationName())!=null)
		{
        locStagingRepo.updateNormalizedLocation(location.getNormalizedLocation(),date,location.getLocationName());
        System.out.println(locStagingRepo.findByLocationName(location.getLocationName()));
        if(locStagingRepo.findByLocationName(location.getLocationName()).getStatus().equals("APPROVED") || locStagingRepo.findByLocationName(location.getLocationName()).getStatus().equals("REJECTED"))
        {
        	System.out.println("helloooooooooooo");
        	locStagingRepo.updateStatus("IN_DRAFT", date,null,locStagingRepo.findByLocationName(location.getLocationName()).getId());
        }

		return "Successfully updated the Normalized Location!";
		}
		else
		{
            return "Location Name is not existed!!";
		}
    }
	
	//Creating a record in Location_Staging
	@Override
	public String uploadData (LocationStaging location){
			String locationName = location.getLocationName();
			String normalized = location.getNormalizedLocation();
			if(locStagingRepo.findByLocationName(locationName)==null)
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
	
	//Parsing the Uploded File
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
	                
                	if(cellIndex==0 && locStagingRepo.findByLocationName(getCellValue(cell))!=null) {
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
			 return "Uploaded Successfully and All "+insertCount+" records got inserted!!";
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

	public File createFile() throws Exception {
    	List<LocationStaging>fileData =locStagingRepo.findAll();
        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet( " Employee Info ");

        //Create row object
        XSSFRow row;

        //This data needs to be written (Object[])
    	
        Map < String, Object[] > empinfo = new TreeMap < String, Object[] >();
        empinfo.put( "1", new Object[] {
           "GSP_LOCATION_NAME", "NORMALIZED_LOCATION_NAME", "STATUS"});
        int i=1;
        for(LocationStaging l:fileData) {
        	i++;
        empinfo.put(""+i,new Object[] {
        		l.getLocationName(),l.getNormalizedLocation(),l.getStatus()
        });
        }
        //Iterate over data and write to sheet
        Set < String > keyid = empinfo.keySet();
        int rowid = 0;
        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object [] objectArr = empinfo.get(key);
            int cellid = 0;
            
            for (Object obj : objectArr){
               Cell cell = row.createCell(cellid++);
               cell.setCellValue((String)obj);
            }
         }
    	File f= new File("Location_Normalization.xlsx");
        FileOutputStream out = new FileOutputStream(f);    
        workbook.write(out);
        out.close();
        System.out.println("Writesheet.xlsx written successfully");

        return f;
		
	}
	
	//Download all the records from Location_Staging
	@Override
    public ResponseEntity<byte[]> downloadFile() throws Exception{
    	File f=createFile();
        String path = "Location_Normalization.xlsx";
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        if(f.exists()) {
    	      return ResponseEntity.ok()
    	          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.getName() + "\"")
    	          .contentType(MediaType.parseMediaType(new MimetypesFileTypeMap().getContentType(f)))
    	          .body(encoded);  
    	    }
        f.deleteOnExit();
    	return ResponseEntity.status(404).body(null);
    }

}
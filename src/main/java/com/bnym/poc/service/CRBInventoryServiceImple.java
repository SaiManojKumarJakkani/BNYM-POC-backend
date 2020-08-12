package com.bnym.poc.service;

import com.bnym.poc.model.CRBInventoryAudit;
import com.bnym.poc.model.CRBInventoryMaster;
import com.bnym.poc.model.CRBInventoryStaging;
import com.bnym.poc.repository.CRBInventoryAuditRepository;
import com.bnym.poc.repository.CRBInventoryMasterRepository;
import com.bnym.poc.repository.CRBInventoryStagingRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.*;

@Service
@Transactional
public class CRBInventoryServiceImple implements CRBInventoryService {

    @Autowired
    private CRBInventoryStagingRepository CRBInventoryStagingRepository;
    @Autowired
    private CRBInventoryAuditRepository CRBInventoryAuditRepository;
    @Autowired
    private CRBInventoryMasterRepository CRBInventoryMasterRepository;


    static List<String> list = new ArrayList<>();

    @Override
    public List<CRBInventoryStaging> getInDraft(String status) {
        return CRBInventoryStagingRepository.findByStatus("InDraft");
    }

    @Override
    public CRBInventoryStaging updateStatus(CRBInventoryStaging cust) {
            if(cust.getStatus().equalsIgnoreCase("approved")) {

                //Update data in master table
                CRBInventoryMaster CRBInventoryMaster = new CRBInventoryMaster();
                CRBInventoryMaster.setMasterId(cust.getStagingId());
                CRBInventoryMaster.setSource(cust.getSource());
                CRBInventoryMaster.setDateOfItem(cust.getDateOfItem());
                CRBInventoryMaster.setECOOCompanyName(cust.getECOOCompanyName());
                CRBInventoryMaster.setIsin(cust.getIsin());
                CRBInventoryMaster.setCuspin(cust.getCuspin());
                CRBInventoryMaster.setSedol(cust.getSedol());
                CRBInventoryMaster.setPrivateComapanyName(cust.getPrivateComapanyName());
                CRBInventoryMaster.setNonPermisibleExpectedDate(cust.getNonPermisibleExpectedDate());
                CRBInventoryMaster.setDate(new Date());
                CRBInventoryMasterRepository.save(CRBInventoryMaster);

            }
            updateCustomerDetails(cust);
            return cust;
    }

    @Override
    public List<CRBInventoryStaging> getInAproval(String status) {
        return CRBInventoryStagingRepository.findByStatus("InAprooval");
    }

    //REST Method
    @Override
    public void add(CRBInventoryStaging customer) {
        //customerInventoryRepository.save(customer);
        updateCustomerDetails(customer);
    }

    //REST METHOD
    @Override
    public List<CRBInventoryStaging> getAllCustomers(){
        return CRBInventoryStagingRepository.findAll();
    }

    //REST METHOD
    @Override
    public CRBInventoryStaging getCustomerById(Integer stagingId) {
        Optional<CRBInventoryStaging> optionalCustomer= CRBInventoryStagingRepository.findById(stagingId);
        return optionalCustomer.get();
    }

    //REST METHOD
    @Override
    public void updateCustomerDetails(CRBInventoryStaging customer) {
        //customer.setId(id);
        customer.setDate(new Date());
        CRBInventoryStagingRepository.save(customer);

        stagingToAudit(customer);
//        CRBInventoryAudit CRBInventoryAudit = new CRBInventoryAudit();
//        CRBInventoryAudit.setAuditId(customer.getStagingId());
//        CRBInventoryAudit.setSource(customer.getSource());
//        CRBInventoryAudit.setDateOfItem(customer.getDateOfItem());
//        CRBInventoryAudit.setECOOCompanyName(customer.getECOOCompanyName());
//        CRBInventoryAudit.setIsin(customer.getIsin());
//        CRBInventoryAudit.setCuspin(customer.getCuspin());
//        CRBInventoryAudit.setSedol(customer.getSedol());
//        CRBInventoryAudit.setPrivateComapanyName(customer.getPrivateComapanyName());
//        CRBInventoryAudit.setNonPermisibleExpectedDate(customer.getNonPermisibleExpectedDate());
//        CRBInventoryAudit.setDate(new Date());
//        CRBInventoryAudit.setStatus(customer.getStatus());
//        CRBInventoryAudit.setStagingId(customer.getStagingId());
//        CRBInventoryAuditRepository.save(CRBInventoryAudit);
    }

    public void stagingToAudit(CRBInventoryStaging customer){
        CRBInventoryAudit CRBInventoryAudit = new CRBInventoryAudit();
        CRBInventoryAudit.setAuditId(customer.getStagingId());
        CRBInventoryAudit.setSource(customer.getSource());
        CRBInventoryAudit.setDateOfItem(customer.getDateOfItem());
        CRBInventoryAudit.setECOOCompanyName(customer.getECOOCompanyName());
        CRBInventoryAudit.setIsin(customer.getIsin());
        CRBInventoryAudit.setCuspin(customer.getCuspin());
        CRBInventoryAudit.setSedol(customer.getSedol());
        CRBInventoryAudit.setPrivateComapanyName(customer.getPrivateComapanyName());
        CRBInventoryAudit.setNonPermisibleExpectedDate(customer.getNonPermisibleExpectedDate());
        CRBInventoryAudit.setDate(new Date());
        CRBInventoryAudit.setStatus(customer.getStatus());
        CRBInventoryAudit.setStagingId(customer.getStagingId());
        CRBInventoryAuditRepository.save(CRBInventoryAudit);

    }

    @Override
    public boolean saveDataFromUploadFile(MultipartFile file) {
        boolean isFlag = false;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension.equalsIgnoreCase("csv")) {
            isFlag = readDataFromCsv(file);
        }else if (extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")){
            isFlag = readDataFromExcelFile(file);

        }
        return isFlag;
    }

    //reading CSV file
    private boolean readDataFromCsv(MultipartFile file) {
        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> rows = csvReader.readAll();
            for (String[] row : rows) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                CRBInventoryStagingRepository.save(new CRBInventoryStaging(row[0], row[1] , row[2], row[3], row[4], row[5], row[6], row[7], "In-Draft", date));
            }
            return true;
        } catch (Exception e) {

            return false;

        }

    }

    //reading xls or xlsx file
    private boolean readDataFromExcelFile(MultipartFile file) {
        Workbook workbook = getWorkBook(file);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        //skipHeader
        //rows.next();
        Row row = rows.next();
        if(isValidated(row)) {
            List<CRBInventoryStaging> customerList = new ArrayList<>();

            while (rows.hasNext()) {
                row = rows.next();
                CRBInventoryStaging customer = new CRBInventoryStaging();
                Iterator<Cell> cellIterator = row.cellIterator();

                //Current Date Formatter
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                int flag = 0;

                loop: while (cellIterator.hasNext() && flag == 0) {
                    Cell cell = cellIterator.next();
                    int cellIndex = cell.getColumnIndex();
                    switch (cellIndex) {
                        case 0:
                            customer.setSource(getCellValue(cell));
                            break;
                        case 1:
                            customer.setDateOfItem(getCellValue(cell));
                            break;
                        case 2:
                            customer.setECOOCompanyName(getCellValue(cell));
                            break;
                        case 3:
                            if(isinExists(getCellValue(cell)))
                            {
                                flag = 1;

                            }else{
                            customer.setIsin(getCellValue(cell));}
                            break;
                        case 4:
                            customer.setCuspin(getCellValue(cell));
                            break;
                        case 5:
                            customer.setSedol(getCellValue(cell));
                            break;
                        case 6:
                            customer.setPrivateComapanyName(getCellValue(cell));
                            break;
                        case 7:
                            customer.setNonPermisibleExpectedDate(getCellValue(cell));
                            break;

                    }
                }
                if(flag == 1){
                    flag = 0;
                    continue;
                }
                if(customer.getECOOCompanyName() != null){
                customerList.add(customer);}
            }

            if (!CollectionUtils.isEmpty(customerList)) {
                CRBInventoryStagingRepository.saveAll(customerList);
                customerList.forEach((n) -> stagingToAudit(n));
            }

            return true;
        }
        return false;
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

    //method to check whether it is xls or xlsx
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

    private boolean isValidated(Row row) {
        CRBInventoryStaging customer = new CRBInventoryStaging();
        Iterator<Cell> cellIterator = row.cellIterator();

        while(cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            int cellIndex = cell.getColumnIndex();
            switch (cellIndex) {
                case 0:
                    if(cell.getStringCellValue().equals("Source")){break;}
                    else{return false;}
                case 1:
                    if(cell.getStringCellValue().equals("Date of Item")){break;}
                    else{return false;}
                case 2:
                    if(cell.getStringCellValue().equals("ECOO Company name")){break;}
                    else{return false;}
                case 3:
                    if(cell.getStringCellValue().equals("ISIN")){break;}
                    else{return false;}
                case 4:
                    if(cell.getStringCellValue().equals("CUSPIN")){break;}
                    else{return false;}
                case 5:
                    if(cell.getStringCellValue().equals("SEDOL")){break;}
                    else{return false;}
                case 6:
                    if(cell.getStringCellValue().equals("Private Company")){break;}
                    else{return false;}
                case 7:
                    if(cell.getStringCellValue().equals("Non Permissible expected date")){break;}
                    else{return false;}

            }
        }

        return true;
    }

    private static boolean isinExists(String isin ) {
        if(list.contains(isin)){
            return true;
        }
        list.add(isin);
        return false;
    }

}



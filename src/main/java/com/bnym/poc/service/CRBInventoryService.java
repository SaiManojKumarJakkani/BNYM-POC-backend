package com.bnym.poc.service;

import com.bnym.poc.model.CRBInventoryStaging;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CRBInventoryService {

    void add(CRBInventoryStaging customer); //REST

    List<CRBInventoryStaging> getAllCustomers(); //REST

    CRBInventoryStaging getCustomerById(Integer id); //REST

    void updateCustomerDetails(CRBInventoryStaging customer); //REST

    boolean saveDataFromUploadFile(MultipartFile file);

    List<CRBInventoryStaging> getInDraft(String status);

    CRBInventoryStaging updateStatus(CRBInventoryStaging cust);

    List<CRBInventoryStaging> getInAproval(String status);

    //CustomerStaging updateStatusToInapproval(CustomerStaging cust);
}

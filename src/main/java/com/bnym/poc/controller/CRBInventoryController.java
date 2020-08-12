package com.bnym.poc.controller;

import com.bnym.poc.message.ResponseMessage;
import com.bnym.poc.model.CRBInventoryStaging;
import com.bnym.poc.repository.CRBInventoryAuditRepository;
import com.bnym.poc.repository.CRBInventoryMasterRepository;
import com.bnym.poc.repository.CRBInventoryStagingRepository;
import com.bnym.poc.service.CRBInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/crb")
public class CRBInventoryController {

    @Autowired
    private CRBInventoryStagingRepository CRBInventoryStagingRepository;
    @Autowired
    private CRBInventoryAuditRepository CRBInventoryAuditRepository;
    @Autowired
    private CRBInventoryService CRBInventoryService;
    @Autowired
    private CRBInventoryMasterRepository CRBInventoryMasterRepository;

    //entering a single record of the customer(REST)
    @PostMapping("/add")
    public ResponseEntity addCustomers(@RequestBody(required = true) CRBInventoryStaging CRBInventoryStaging){
        CRBInventoryService.add(CRBInventoryStaging);
        String message = "Your Data has been added Successfully";
        //return new ResponseEntity<CustomerStaging>(HttpStatus.CREATED);
        return new ResponseEntity(message, HttpStatus.CREATED);
    }

    //displaying all the data of the customer_staging(REST)
    @GetMapping("/getAll")
    public ResponseEntity<List<CRBInventoryStaging>> getAllCustomersStaging(){
       List<CRBInventoryStaging> customerList = CRBInventoryService.getAllCustomers();
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    //get customer by ID REST
    @GetMapping("/{stagingId}")
    public ResponseEntity<CRBInventoryStaging> getCustomerById(@PathVariable(value = "stagingId") Integer stagingId){
        System.out.println("customerId........"+ stagingId);
        CRBInventoryStaging customer= CRBInventoryService.getCustomerById(stagingId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    //update by id REST
    @PutMapping("/{stagingId}")
    public ResponseEntity<CRBInventoryStaging> updateCustomer(@PathVariable(value = "stagingId") Integer stagingId, @RequestBody(required = true) CRBInventoryStaging customer){
        customer.setStagingId(stagingId);
        CRBInventoryService.updateCustomerDetails(customer);
        String message = "Your Data has been Updated Successfully";
        return new ResponseEntity(message, HttpStatus.OK);
    }

    //uploading Excel sheet data into the database(REST)
    @PostMapping("/uploadFile")
    public ResponseEntity uploadExcelFile(@RequestParam("file") MultipartFile file) throws IOException {
        String message = "";
        boolean isFlag = CRBInventoryService.saveDataFromUploadFile(file);
        if(isFlag){
            try{
                    message = "Uploaded the file successfully: " + file.getOriginalFilename();
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }catch(Exception e){
                message = "Could not Upload the file: " + file.getOriginalFilename() + "!";
                return  ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

            }
        }
        message = "Please check file format or excel file Headers!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    //to get/display the "In-draft" data(REST)
    @GetMapping("/getDraft")
    public List<CRBInventoryStaging> getDraft(@RequestParam(value = "status" , required = false) String status){
        List<CRBInventoryStaging> customerList = CRBInventoryService.getInDraft(status);
        return customerList;
    }

    @GetMapping("/{stagingId}/{status}")
    public CRBInventoryStaging updateCustomerStatus(@PathVariable int stagingId, @PathVariable String status){
        CRBInventoryStaging cust = CRBInventoryService.getCustomerById(stagingId);
        cust.setStatus(status);
        CRBInventoryStaging customer = CRBInventoryService.updateStatus(cust);
         return customer;
    }
    @PostMapping("/{stagingId}")
        public ResponseEntity submit(@PathVariable(value = "stagingId") Integer stagingId){
        CRBInventoryStaging cust = CRBInventoryService.getCustomerById(stagingId);
        cust.setStatus("InAprooval");
        CRBInventoryStaging customer = CRBInventoryService.updateStatus(cust);
        String message = "Succesfully submitted!";
        return new ResponseEntity(message, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getInAproval")
    public List<CRBInventoryStaging> getInAproval(@RequestParam(value = "status" , required = false) String status){
        List<CRBInventoryStaging> customerList = CRBInventoryService.getInAproval(status);
        return customerList;
    }





    // 1. inaproval
    // 2. getAllInaproval
    // 3. ISIN


//    @GetMapping("{status}")
//    public CustomerStaging updateCustomerStatusToInapproval(@PathVariable int stagingId, @PathVariable String status){
//        CustomerStaging cust = customerService.getCustomerById(stagingId);
//        cust.setStatus(status);
//        CustomerStaging customer = customerService.updateStatusToInapproval(cust);
//        return customer;
//    }

































//    @GetMapping("/")
//    public void updateStatus(){
//        Customer_audit customerAudit = new Customer_audit();
//        Customer customer = new Customer();
//        customerAudit = new Customer_audit(customer.getName(), customer.getLocation(),customer.getEmail(),
//                            customer.getStatus(), customer.getDate());
//        customerAuditRepository.saveAndFlush(customerAudit);
//
//     System.out.println("success");
//    }















    //RESTAPI
//    @PostMapping("uploadfile")
//    public ResponseEntity uploadFile(@RequestParam("file")MultipartFile file) throws IOException{
//        customerService.readFile(file);
//        return new ResponseEntity(HttpStatus.OK);
//    }
}

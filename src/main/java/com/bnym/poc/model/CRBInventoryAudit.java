package com.bnym.poc.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "CRB_customer_audit")
public class CRBInventoryAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private int auditId;

    @Column(name =  "source_name")
    private String source;

    @Column(name = "dateOfItem")
    private String dateOfItem;

    @Column(name = "ECOO_Company_Name")
    private String ECOOCompanyName;

    @Column(name = "ISIN" , unique = true)
    private String isin;

    @Column(name = "CUSPIN")
    private String cuspin;

    @Column(name = "SEDOL")
    private String sedol;

    @Column(name = "private_comapany_name")
    private String privateComapanyName;

    @Column(name = "non_permisible_expected_date")
    private String nonPermisibleExpectedDate;

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private Date date;

    @Column(name = "staging_id")
    private int stagingId;

    public CRBInventoryAudit() {

    }

    public CRBInventoryAudit(String source, String dateOfItem, String ECOOCompanyName, String isin, String cuspin, String sedol, String privateComapanyName, String nonPermisibleExpectedDate, String status, Date date) {
        this.source = source;
        this.dateOfItem = dateOfItem;
        this.ECOOCompanyName = ECOOCompanyName;
        this.isin = isin;
        this.cuspin = cuspin;
        this.sedol = sedol;
        this.privateComapanyName = privateComapanyName;
        this.nonPermisibleExpectedDate = nonPermisibleExpectedDate;
        this.status = status;
        this.date = date;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDateOfItem() {
        return dateOfItem;
    }

    public void setDateOfItem(String dateOfItem) {
        this.dateOfItem = dateOfItem;
    }

    public String getECOOCompanyName() {
        return ECOOCompanyName;
    }

    public void setECOOCompanyName(String ECOOCompanyName) {
        this.ECOOCompanyName = ECOOCompanyName;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getCuspin() {
        return cuspin;
    }

    public void setCuspin(String cuspin) {
        this.cuspin = cuspin;
    }

    public String getSedol() {
        return sedol;
    }

    public void setSedol(String sedol) {
        this.sedol = sedol;
    }

    public String getPrivateComapanyName() {
        return privateComapanyName;
    }

    public void setPrivateComapanyName(String privateComapanyName) {
        this.privateComapanyName = privateComapanyName;
    }

    public String getNonPermisibleExpectedDate() {
        return nonPermisibleExpectedDate;
    }

    public void setNonPermisibleExpectedDate(String nonPermisibleExpectedDate) {
        this.nonPermisibleExpectedDate = nonPermisibleExpectedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStagingId() {
        return stagingId;
    }

    public void setStagingId(int stagingId) {
        this.stagingId = stagingId;
    }

//    @Override
//    public String toString() {
//        return "CustomerStaging{" +
//                "auditId=" + auditId +
//                ", source='" + source + '\'' +
//                ", dateOfItem=" + dateOfItem +
//                ", ECOOCompanyName='" + ECOOCompanyName + '\'' +
//                ", isin='" + isin + '\'' +
//                ", cuspin='" + cuspin + '\'' +
//                ", sedol='" + sedol + '\'' +
//                ", privateComapanyName='" + privateComapanyName + '\'' +
//                ", nonPermisibleExpectedDate='" + nonPermisibleExpectedDate + '\'' +
//                ", status='" + status + '\'' +
//                ", date=" + date +
//                '}';
//    }

    @Override
    public String toString() {
        return "CRBInventoryAudit{" +
                "auditId=" + auditId +
                ", source='" + source + '\'' +
                ", dateOfItem='" + dateOfItem + '\'' +
                ", ECOOCompanyName='" + ECOOCompanyName + '\'' +
                ", isin='" + isin + '\'' +
                ", cuspin='" + cuspin + '\'' +
                ", sedol='" + sedol + '\'' +
                ", privateComapanyName='" + privateComapanyName + '\'' +
                ", nonPermisibleExpectedDate='" + nonPermisibleExpectedDate + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", stagingId=" + stagingId +
                '}';
    }
}

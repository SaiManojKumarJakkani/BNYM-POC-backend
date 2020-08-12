package com.bnym.poc.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="LOCATION_AUDIT")
public class LocationAudit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "Id")
    private int id;
    
    @Column(name =  "location_name")
    private String locationName;

    @Column(name = "normalized_location")
    private String normalizedLocation;

    @Column(name = "status")
    private String status;

    @Column(name = "log_date")
    private Date logDate;
    
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "staging_id", referencedColumnName = "id")
    @Column(name = "staging_id")
    private int stagingId;
    
    
	public LocationAudit() {
		super();
		// TODO Auto-generated constructor stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.logDate=date;
	}

	public LocationAudit(String locationName, String normalizedLocation, String status,int stagingId) {
		super();
		this.locationName = locationName;
		this.normalizedLocation = normalizedLocation;
		this.status = status;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.logDate=date;
		this.stagingId = stagingId;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getNormalizedLocation() {
		return normalizedLocation;
	}

	public void setNormalizedLocation(String normalizedLocation) {
		this.normalizedLocation = normalizedLocation;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStagingId() {
		return stagingId;
	}

	public void setStagingId(int stagingId) {
		this.stagingId = stagingId;
	}

	@Override
	public String toString() {
		return "LocationAudit [id=" + id + ", locationName=" + locationName + ", normalizedLocation="
				+ normalizedLocation + ", status=" + status + ", logDate=" + logDate + ", stagingId=" + stagingId + "]";
	}
}

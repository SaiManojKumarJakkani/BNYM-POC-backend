package com.bnym.poc.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="LOCATION_STAGING")
public class LocationStaging {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "id",unique = true)
    private int id;

    @Column(name =  "location_name")
    private String locationName;

    @Column(name = "normalized_location")
    private String normalizedLocation;

    @Column(name = "status")
    private String status;

    @Column(name = "modified_date")
    private Date modifiedDate;
    
    @Column(name = "rejection_notes")
    private String rejectionNotes;
    
    public LocationStaging() {
		super();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Date d=new Date(dateFormat.format(date));
        setStatus("IN_DRAFT");
        setModifiedDate(d);
	}
    

	public LocationStaging(int id, String locationName, String normalizedLocation, String status, Date modifiedDate) {
		super();
		this.id = id;
		this.locationName = locationName;
		this.normalizedLocation = normalizedLocation;
		this.status = status;
		this.modifiedDate = modifiedDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public String getRejectionNotes() {
		return rejectionNotes;
	}


	public void setRejectionNotes(String rejectionNotes) {
		this.rejectionNotes = rejectionNotes;
	}


	@Override
	public String toString() {
		return "LocationStaging [id=" + id + ", locationName=" + locationName + ", normalizedLocation="
				+ normalizedLocation + ", status=" + status + ", modifiedDate=" + modifiedDate + ", rejectionNotes="
				+ rejectionNotes + "]";
	}
	
	

}

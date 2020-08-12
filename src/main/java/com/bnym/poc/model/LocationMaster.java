package com.bnym.poc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="LOCATION_MASTER")
public class LocationMaster {
	
		@Column(name= "Id",unique = true)
	    private int id;
	    
	    @Id
	    @Column(name =  "location_name")
	    private String locationName;

	    @Column(name = "normalized_location")
	    private String normalizedLocation;

	    @Column(name = "modified_date")
	    private Date modifiedDate;
		public LocationMaster() {
				super();
				// TODO Auto-generated constructor stub
			}
		public LocationMaster(int id, String locationName, String normalizedLocation, Date modifiedDate) {
			super();
			this.id = id;
			this.locationName = locationName;
			this.normalizedLocation = normalizedLocation;
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
		public Date getModifiedDate() {
			return modifiedDate;
		}
		public void setModifiedDate(Date modifiedDate) {
			this.modifiedDate = modifiedDate;
		}
		@Override
		public String toString() {
			return "LocationMaster [id=" + id + ", locationName=" + locationName + ", normalizedLocation="
					+ normalizedLocation + ", modifiedDate=" + modifiedDate + "]";
		}
	
}

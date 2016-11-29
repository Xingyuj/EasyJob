package easyjob;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Job {
	private String description;
	private Date datePosted;
	private String employmentType;
	private HiringOrganization hiringOrganization;
	private String title;
	private String url;
	private JobLocation jobLocation;
	
	public JobLocation getJobLocation() {
		return jobLocation;
	}
	public void setJobLocation(JobLocation jobLocation) {
		this.jobLocation = jobLocation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDatePosted() {
		return datePosted;
	}
	public void setDatePosted(Date datePosted) {
		this.datePosted = datePosted;
	}
	public String getEmploymentType() {
		return employmentType;
	}
	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}
	public HiringOrganization getHiringOrganization() {
		return hiringOrganization;
	}
	public void setHiringOrganization(HiringOrganization hiringOrganization) {
		this.hiringOrganization = hiringOrganization;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}

class HiringOrganization {
	@SerializedName("@type")
	private String type;
	private String name;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

class JobLocation {
	@SerializedName("@type")
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	private Address address;
}

class Address {
	@SerializedName("@type")
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddressRegion() {
		return addressRegion;
	}
	public void setAddressRegion(String addressRegion) {
		this.addressRegion = addressRegion;
	}
	private String addressRegion;
}
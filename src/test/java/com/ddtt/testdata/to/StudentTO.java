package com.ddtt.testdata.to;

import java.sql.Timestamp;
import java.sql.Date;

public class StudentTO {
	private Integer id;
	private String name;
	private Date dob;
	private Timestamp createdTm;
	private Integer schoolId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public Timestamp getCreatedTm() {
		return createdTm;
	}
	public void setCreatedTm(Timestamp createdTm) {
		this.createdTm = createdTm;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
}

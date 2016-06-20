package com.bwsoft.demo.springmvc.controller;

public class Employee {
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	private String name = "specify a name";
    private long id = -99999;
    private String contactNumber = "specify a contact";
}

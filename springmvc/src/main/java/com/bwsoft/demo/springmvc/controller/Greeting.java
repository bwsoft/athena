package com.bwsoft.demo.springmvc.controller;

public class Greeting {
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private long id = -999;
    private String content = "Hello";

}

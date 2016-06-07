package com.bwsoft.athena.jmx;

public class Hello implements HelloMBean {
	private String msg = "hello world!";
	
	@Override
	public String sayHello() {
		return msg;
	}

	@Override
	public void setHelloMessage(String msg) {
		this.msg = msg;
		
	}
	
}

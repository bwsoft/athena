package com.bwsoft.demo.storelocator;

public class StoreLocatorException extends RuntimeException {
	   private String exceptionMsg;
	   
	   public StoreLocatorException(String exceptionMsg) {
	      this.exceptionMsg = exceptionMsg;
	   }
	   
	   public String getExceptionMsg(){
	      return this.exceptionMsg;
	   }
	   
	   public void setExceptionMsg(String exceptionMsg) {
	      this.exceptionMsg = exceptionMsg;
	   }
}

package com.bwsoft.athena.jni;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObservableOfNativeByteArray extends Observable {
	private static final Logger logger = LoggerFactory.getLogger(ObservableOfNativeByteArray.class);
	
	static {
		try {
			System.loadLibrary("athena");
		} catch( UnsatisfiedLinkError e ) {
			logger.error("failed in loading native library: libathena.so", e);
		}
	}
	
	public native void startNativeObservable();
	
	private void setByteArray(byte[] arrayEvent) {
		logger.debug("Receive a native event");
		this.setChanged();
		this.notifyObservers(arrayEvent);
	}
}

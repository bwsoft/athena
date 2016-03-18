package com.bwsoft.athena.jni;

public class NativeUtil {
	public native static int getLXRandom() throws Exception;
	
	public static void main(String[] args) throws Exception {
		System.loadLibrary("athena");
		
		System.out.println("Random number from system: "+NativeUtil.getLXRandom());
	}
}

package com.bwsoft.athena.misc;

import java.util.concurrent.locks.LockSupport;

public class Lock {
	public static void main(String[] args) throws InterruptedException {
		Lock test = new Lock();
		Thread testThread = test.new Worker();
		// the unpark has to happen after the thread in the running state 
		LockSupport.unpark(testThread);
		testThread.start();
		System.out.println("pos 1");
		LockSupport.unpark(testThread);
		System.out.println("pos 2");
		Thread.sleep(1000);
		LockSupport.unpark(testThread);
		System.out.println("pos 3");
	}
	
	private class Worker extends Thread {
		@Override
		public void run() {
			while(true) {
				LockSupport.park();
				System.out.println("Executed");
			}
		}
	}
}

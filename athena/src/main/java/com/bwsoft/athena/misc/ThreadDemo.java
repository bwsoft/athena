package com.bwsoft.athena.misc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadDemo {
	private boolean isAlive = true;
	
	private final static ThreadLocal<String> localName = new ThreadLocal<String> () {
		@Override
		protected String initialValue() {
			return Thread.currentThread().getName();
		}
	};

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService service = Executors.newFixedThreadPool(2);
		
		ThreadDemo demo = new ThreadDemo();
		Callable<Boolean> worker = demo.new Worker();
		
 		Future<Boolean> retFuture = service.submit(worker);
		Thread.sleep(1000);
		System.out.println("The number of active threads now is: "+Thread.activeCount());

		/**
		 * Normally terminate the service
		 */
		demo.isAlive = false;
		if( retFuture.get() ) {
			System.out.println("Worker is normally terminated.");
		}
		
		/**
		 * Cancel the service. It results in an exception in get()
		 */
		demo.isAlive = true;
 		retFuture = service.submit(worker);
		Thread.sleep(1000);
		System.out.println("The number of active threads now is: "+Thread.activeCount());
		
		retFuture.cancel(true);
		System.out.println("The service is cancelld? "+retFuture.isCancelled());
		try {
			System.out.println("The service return value is "+retFuture.get());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		/**
		 * Interrupt the service thread
		 */
 		retFuture = service.submit(worker);
		Thread.sleep(1000);
		System.out.println("The number of active threads now is: "+Thread.activeCount());
		
		service.shutdownNow();
		System.out.println("The service is cancelld? "+retFuture.isCancelled());
		try {
			System.out.println("The service return value is "+retFuture.get());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private class Worker implements Callable<Boolean> {

		@Override
		public Boolean call() throws InterruptedException {
			System.out.println("My local name is: "+localName.get());
			System.out.println("Call trace is: ");
			Thread.dumpStack();
			while( ! Thread.currentThread().isInterrupted() && isAlive ) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// throw e;
					return false;
				} 
			}
			return true;
			
		}
	}
}

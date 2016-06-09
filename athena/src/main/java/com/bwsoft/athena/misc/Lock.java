package com.bwsoft.athena.misc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Lock {
	private boolean isRunning = true;
	private int releasingAttempt = 0;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Lock test = new Lock();
		ExecutorService exec = Executors.newSingleThreadExecutor();

		/**
		 * Use park and unpark to suspend and resume the Worker thread. 
		 */
		Thread testThread = test.new Worker();
		// the unpark or the grant of the permit has to happen after the thread in the running state.
		test.releasingAttempt ++;
		LockSupport.unpark(testThread);
		
		testThread.start();
		
		test.releasingAttempt ++;
		LockSupport.unpark(testThread);
		Thread.sleep(1000);
		
		test.isRunning = false;
		test.releasingAttempt ++;
		LockSupport.unpark(testThread);
		
		/**
		 * Use ReentrantLock to prevent other thread from accessing the lock. The other thread
		 * uses tryLock() to test lock and hence will not be blocked.
		 * 
		 * Use monitoring capability to display the current lock status.
		 */
		ReentrantLock newLock = new ReentrantLock();
		newLock.lock();		
		System.out.println("Lock count="+newLock.getHoldCount()+", isLocked="+newLock.isLocked());

		Future<Boolean> ret = exec.submit(()->{
			boolean state = newLock.tryLock();
			if( state ) newLock.unlock();
			return state;
		});
		boolean retVal = ret.get();
		System.out.println("The attempt to lock by another thread is "+ retVal);
		
		newLock.lock();
		System.out.println("Lock count="+newLock.getHoldCount()+", isLocked="+newLock.isLocked());

		newLock.unlock();
		System.out.println("Lock count="+newLock.getHoldCount()+", isLocked="+newLock.isLocked());

		ret = exec.submit(()->{
			boolean state = newLock.tryLock();
			if( state ) newLock.unlock();
			return state;
		});
		retVal = ret.get();
		System.out.println("The attempt to lock by another thread is "+ retVal);

		newLock.unlock();
		System.out.println("Lock count="+newLock.getHoldCount()+", isLocked="+newLock.isLocked());

		ret = exec.submit(()->{
			boolean state = newLock.tryLock();
			if( state ) newLock.unlock();
			return state;
		});
		retVal = ret.get();
		System.out.println("The attempt to lock by another thread is "+ retVal);

		exec.shutdown();
	}
	
	private class Worker extends Thread {
		@Override
		public void run() {
			while(isRunning) {
				System.out.print("Worker thread is parked");
				LockSupport.park();
				System.out.format(" ... and released by releasing attempt %d\n", releasingAttempt);
			}
			System.out.println("The worker thread exits");
		}
	}
}

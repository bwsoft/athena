package com.bwsoft.athena.misc;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CyclicBarrier {

	// cyclic barrier can be rest. It needs to be careful in using thread pool with cyclicbarrier 
	// due to potential dead lock since a barrier is typically to use to hold the current thread and 
	// wait for the completions of all other related threads. 
	
	private final java.util.concurrent.CyclicBarrier barrier;
	private final BitSet bitSet;
	
	public CyclicBarrier() {
		bitSet = new BitSet(8);
		barrier = new java.util.concurrent.CyclicBarrier(8, ()->{
			System.out.println("Done with value = "+bitSet.toLongArray()[0]);
		});
	}
	
	public void start() throws InterruptedException {
		// insufficient number of threads will trigger dead lock
		ExecutorService service = Executors.newFixedThreadPool(10);
		
		long startTime = System.currentTimeMillis();
		for( int i = 0; i < 8; i ++ ) {
			service.execute(new Worker(i));
		}
		
		service.shutdown();
		service.awaitTermination(10000, TimeUnit.SECONDS);
		System.out.println("Done with time duration of "+(System.currentTimeMillis()-startTime)+" and value "+bitSet.toLongArray()[0]);
	}
	
	private class Worker implements Runnable {
		private final int pos;
		
		public Worker(int pos) {
			this.pos = pos;
		}
		
		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			Random rand = new Random();
			try {
				Thread.sleep(rand.nextInt(1000));
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}
			
			bitSet.set(this.pos);
			try {
				Thread.sleep(rand.nextInt(1000));
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}

			System.out.println("work for, "+this.pos+", is completed after "+ (System.currentTimeMillis()-startTime));
			try{
				// hold this thread until all others are done. 
				barrier.await();
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new CyclicBarrier().start();
	}
}

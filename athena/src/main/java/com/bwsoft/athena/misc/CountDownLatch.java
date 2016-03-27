package com.bwsoft.athena.misc;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatch {
	// count down latch cannot be reset
	private final java.util.concurrent.CountDownLatch latch;
	private final BitSet bitSet;
	
	public CountDownLatch() {
		bitSet = new BitSet(8);
		latch = new java.util.concurrent.CountDownLatch(8);
	}
	
	public void start() throws InterruptedException {
		// insufficient number of threads to start all jobs at the same time
		ExecutorService service = Executors.newFixedThreadPool(2);
		
		long startTime = System.currentTimeMillis();
		for( int i = 0; i < 8; i ++ ) {
			service.execute(new Worker(i));
		}
		
		// block until count down to zero
		latch.await();
		System.out.println("Done with time duration of "+(System.currentTimeMillis()-startTime));
		
		// do not forget to shut down the executorservice
		service.shutdown();
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
			
			System.out.println("latch, "+this.pos+", is completed after "+ (System.currentTimeMillis()-startTime));
			latch.countDown();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch();
		latch.start();
	}
}

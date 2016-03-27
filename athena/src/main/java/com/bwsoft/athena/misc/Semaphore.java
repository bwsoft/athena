package com.bwsoft.athena.misc;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Semaphore {
	private final java.util.concurrent.Semaphore semaphore;
	private final BitSet bitSet;
	private volatile long starttime;
	
	public Semaphore() {
		bitSet = new BitSet(8);
		semaphore = new java.util.concurrent.Semaphore(2);
	}
	
	public void start() throws InterruptedException {
		// threads are more than available permits
		ExecutorService service = Executors.newFixedThreadPool(10);
		
		starttime = System.currentTimeMillis();
		for( int i = 0; i < 8; i ++ ) {
			service.execute(new Worker(i));
		}
		
		service.shutdown();
		service.awaitTermination(10000, TimeUnit.SECONDS);
		System.out.println("Done with time duration of "+(System.currentTimeMillis()-starttime)+" and value "+bitSet.toLongArray()[0]);
	}
	
	private class Worker implements Runnable {
		private final int pos;
		
		public Worker(int pos) {
			this.pos = pos;
		}
		
		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			try {
				semaphore.acquire();
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}
			
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
			semaphore.release();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Semaphore().start();
	}

}

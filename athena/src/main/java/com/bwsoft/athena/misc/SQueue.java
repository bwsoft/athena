package com.bwsoft.athena.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class SQueue {
	private SynchronousQueue<String> queue = new SynchronousQueue<String>();
	
	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(10);
		
		SQueue sq = new SQueue();
		service.execute(sq.new Publisher());
		
		service.execute(sq.new Subscriber());
		
		service.shutdown();
	}
	
	private class Subscriber implements Runnable {

		@Override
		public void run() {
			for( int i = 0; i < 5; i ++ ) {
				System.out.println("dequeue ...");
				try {
					System.out.println("got message: "+queue.take());
				} catch( Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class Publisher implements Runnable {

		@Override
		public void run() {
			for( int i = 0; i < 5; i ++ ) {
				System.out.println("publishing ...");
				try {
					queue.put("hello");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("published");
			}
		}
	}
}

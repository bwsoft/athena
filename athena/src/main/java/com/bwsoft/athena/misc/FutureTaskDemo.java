package com.bwsoft.athena.misc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTaskDemo {
	public static void main(String[] args) {
		ExecutorService service = Executors.newSingleThreadExecutor();
		
		FutureTask<Boolean> ft = new FutureTask<Boolean>(()->{
			for( int i = 0; i < 5; i ++) {
				System.out.println("sleeping");
				Thread.sleep(1000);
			}
			return true;
		});
		
		service.execute(ft);
		boolean rt = false;
		while( ! rt ) {
			try {
				rt = ft.get(1000, TimeUnit.MILLISECONDS);
			} catch( TimeoutException e ) {
				System.out.println("woke up but will be back to sleep");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Future is returned");
		
		service.shutdown();
	}
}

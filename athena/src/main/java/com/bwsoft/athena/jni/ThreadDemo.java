package com.bwsoft.athena.jni;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadDemo {
	private static boolean useNative;
	private int caseId;
	private int nonV = 0;
	private volatile int v= 0; 
	
	private ThreadLocal<LinkedList<Integer>> ilist = new ThreadLocal<LinkedList<Integer>>() {
		@Override
		protected LinkedList<Integer> initialValue() {
			return new LinkedList<Integer>();
		};
	};
	
	static {
		try {
			System.loadLibrary("athena");
			useNative = true;
		} catch( UnsatisfiedLinkError e ) {
			e.printStackTrace();
			useNative = false;
		}
	}
	
	public ThreadDemo(int id) {
		this.caseId = id;
	}
	
	public void localThreadTest() {
		ExecutorService service = Executors.newFixedThreadPool(2);
		service.execute(() -> popularOddNumber());
		service.execute(() -> popularEvenNumber());
		service.shutdown();
	}
	
	private native void pinThreadNative(int core);
	
	private void pinThread(int core) {
		if( useNative ) {
			pinThreadNative(core);
		} 
	}
	
	private void popularEvenNumber() {
		pinThread(4);
		nonV = 0;
		for( int i = 0; i <= 1000; i += 2) {
			ilist.get().add(i);
			nonV += 1;
			v += 1;
			Thread.yield();
		}
		
		System.out.println(Thread.currentThread().getName()+": even number population (expected 501) = " + ilist.get().size()+" sum( exptect 250*1000+500) is "+sum());
		System.out.println("non volatile variable value is: "+nonV);
		System.out.println("volatile variable value is: "+v);
	}

	private void popularOddNumber() {
		pinThread(3);
		nonV = 0;
		for( int i = 1; i <= 1000; i += 2) {
			ilist.get().add(i);
			nonV += 1;
			v += 1;
			Thread.yield();
		}
		
		System.out.println(Thread.currentThread().getName()+": odd number population (expected 500) = " + ilist.get().size()+" sum( exptect 250*1000) is "+sum());
		System.out.println("non volatile variable value is: "+nonV);
		System.out.println("volatile variable value is: "+v);
	}

	private int sum() {
		int sum = 0;
		for( Integer i : ilist.get() ) {
			sum += i;
		}
		return sum;
	}
	
	public static void main(String[] args) {
		new ThreadDemo(0).localThreadTest();
	}
}

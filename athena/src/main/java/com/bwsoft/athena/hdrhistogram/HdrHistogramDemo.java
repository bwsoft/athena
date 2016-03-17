package com.bwsoft.athena.hdrhistogram;

import java.net.DatagramSocket;

import org.HdrHistogram.Histogram;

public class HdrHistogramDemo {
	static Histogram histogram = new Histogram(3600000000000L, 3);
	
	public static volatile DatagramSocket socket;
	
	static long WARMUP_TIME_MSEC=5000;
	static long RUN_TIME_MSEC=20000;
	
	static void recordTimeToCreateAndCloseDatagramSocket() {
		long startTime = System.nanoTime();
		try {
			socket = new DatagramSocket();
		} catch( Exception e ) {
			
		} finally {
			socket.close();
		}
		long endTime = System.nanoTime();
		histogram.recordValue(endTime - startTime);
	}
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		long now;
		
		do {
			recordTimeToCreateAndCloseDatagramSocket();
			now = System.currentTimeMillis();
		} while( now - startTime < WARMUP_TIME_MSEC );
		histogram.reset();
		
		do {
			recordTimeToCreateAndCloseDatagramSocket();
			now = System.currentTimeMillis();
		} while( now - startTime < RUN_TIME_MSEC );
		
		System.out.println("Recorded latency");
		histogram.outputPercentileDistribution(System.out, 1000.0);
		
		System.out.println("Mean = "+histogram.getMean()+" std = "+histogram.getStdDeviation());
		System.out.println("95 pct = "+histogram.getValueAtPercentile(95));
		System.out.println(" total = "+histogram.getTotalCount());
	}
}
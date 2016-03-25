package com.bwsoft.athena.disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class LongEventMainJava8 {
	public static void main(String[] args) throws InterruptedException {
		Executor executor = Executors.newCachedThreadPool();
		
		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, 1024, executor);
		disruptor.handleEventsWith((event, sequence, EndOfBatch) -> System.out.println("1-Event: "+event.get()), 
				(event, sequence, EndOfBatch) -> System.out.println("2-Event: "+event.get()));
		disruptor.start();
		
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		
		ByteBuffer bb = ByteBuffer.allocate(8);
		for( long i=0; i < 100; i ++) {
			bb.putLong(0,i);
			ringBuffer.publishEvent((event,sequence, data)->event.set(data.getLong(0)), bb);
			Thread.sleep(1000);
		}
	}
}

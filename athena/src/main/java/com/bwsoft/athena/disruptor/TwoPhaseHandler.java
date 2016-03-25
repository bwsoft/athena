package com.bwsoft.athena.disruptor;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;

final class ValueEvent {
	private long value;
	public long getValue() {
		return value;
	}
	
	public void setValue(long value) {
		this.value = value;
	}
	
	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};
}

class MyWorkHandler implements WorkHandler<ValueEvent> {
	AtomicLong workDone;
	public MyWorkHandler(AtomicLong wd) {
		this.workDone = wd;
	}
	
	public void onEvent(final ValueEvent event) throws Exception {
		long seq = workDone.incrementAndGet();
		System.out.println("phase 1 handler: "+event.getValue()+", seq ="+seq+", thread name="+Thread.currentThread().getName());
	}
}

class My2ndPhaseWorkHandler implements WorkHandler<ValueEvent> {
	AtomicLong workDone;
	
	public My2ndPhaseWorkHandler(AtomicLong wd) {
		this.workDone = wd;
	}
	
	public void onEvent(final ValueEvent event) throws Exception {
		long seq = workDone.incrementAndGet();
		System.out.println("phase 2 handler: "+event.getValue()+", seq ="+seq+", thread name="+Thread.currentThread().getName());

			Thread.sleep(1000);
	}
}

class MyEventTranslator implements EventTranslatorOneArg<ValueEvent, Long> {
	@Override
	public void translateTo(ValueEvent event, long seq, Long value) {
		event.setValue(value);
	}
}

public class TwoPhaseHandler {
	static AtomicLong workDone = new AtomicLong(0);
	
	@SuppressWarnings("unchecked") 
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		
		int numOfHandlersInEachGroup = 1;
		long eventCount = 1024*2;
		int ringBufferSize = 1024;
		
		Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(ValueEvent.EVENT_FACTORY, ringBufferSize, exec);
		
		ArrayList<MyWorkHandler> handlers = new ArrayList<MyWorkHandler>();
		for( int i = 0; i < numOfHandlersInEachGroup; i ++) {
			handlers.add(new MyWorkHandler(workDone));
		}
		
		ArrayList<My2ndPhaseWorkHandler> phase2_handlers = new ArrayList<My2ndPhaseWorkHandler>();
		for( int i = 0; i < numOfHandlersInEachGroup; i ++ ) {
			phase2_handlers.add(new My2ndPhaseWorkHandler(workDone));
		}
		
		disruptor.handleEventsWithWorkerPool(handlers.toArray(new WorkHandler[handlers.size()]))
			.thenHandleEventsWithWorkerPool(phase2_handlers.toArray(new WorkHandler[phase2_handlers.size()]));
		
		long s = System.currentTimeMillis();
		disruptor.start();
		
		MyEventTranslator myEventTranslator = new MyEventTranslator();
		for( long i = 0; i < eventCount; i ++ ) {
			disruptor.publishEvent(myEventTranslator, i);
		}
		
		disruptor.shutdown();
		exec.shutdown();
		System.out.println("time spent: "+(System.currentTimeMillis()-s)+"ms");
		System.out.println("amount of work done: "+workDone.get());
	}
}

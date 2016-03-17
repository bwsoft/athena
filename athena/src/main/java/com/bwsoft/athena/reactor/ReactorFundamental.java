package com.bwsoft.athena.reactor;

import reactor.Environment;
import reactor.rx.Stream;
import reactor.rx.Streams;
import reactor.rx.broadcast.Broadcaster;

public class ReactorFundamental {
	static {
		Environment.initialize();
	}
	
	public static void main(String[] args) throws InterruptedException {
		// create a stream subclass to sink values into it
		Broadcaster<String> b = Broadcaster.create();
		b
		// dispatch onto a thread other than main
		.dispatchOn(Environment.cachedDispatcher())
		// transform input to upper case
		.map(String::toUpperCase)
		// only let certain value to pass through
		.filter(s->s.startsWith("HELLO"))
		// produce demand
		.consume(s-> System.out.println(Thread.currentThread().getName()+":" +s));
		
		// sink value into the broadcaster
		b.onNext("Hello world");
		
		// this will be filtered
		b.onNext("Goodbye");
		
		Stream<String> st = Streams.just("hello", "world", "!");
		st.dispatchOn(Environment.cachedDispatcher())
		.map(String::toUpperCase)
		.consume(s -> System.out.format("%s greeting = %s%n", Thread.currentThread(),s));
		
		Thread.sleep(1000);
	}
}

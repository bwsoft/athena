package com.bwsoft.athena.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import static reactor.bus.selector.Selectors.$;

public class ReactorBus {
	private static final Logger logger = LoggerFactory.getLogger(ReactorBus.class);
	
	public static void main(String[] args) throws InterruptedException {
		logger.info("Main thread: {}",Thread.currentThread().getName());
		
		// initialize the reactor environment such as its threading model.
		Environment env = Environment.initialize();
		
		// create an event bus
		EventBus bus = EventBus.create(env);
		
		// Set up a selector
		bus.on($("reply.sink"), (Event<String> s) -> {String msg = s.getData(); logger.info("reply.sink: {} (from thread {})", msg, Thread.currentThread().getName());});
		bus.on($("send-recv"), (Event<String> s) -> {String msg = s.getData(); logger.info("send-recv: {} (from thread {})", msg, Thread.currentThread().getName());});
		bus.receive($("send-recv"), ev->{return "Acknowledgement of the reception of a message: "+ev.getData();});
	
		// trigger an event
		bus.notify("reply.sink", Event.wrap("notify reply.sink"));
		
		// send a message with a reply topic
		bus.send("send-recv", Event.wrap("notify send-recv","reply.sink"));
		
		Thread.sleep(2000);
	}
}

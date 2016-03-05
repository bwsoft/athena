package com.bwsoft.athena.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bwsoft.athena.util.TraceCall;

/**
 * Selector is not thread-safe. It is a good practice to do all operations via a single thread. 
 * This class takes care of the registration and the NIO event selection. Both are done via the 
 * same thread.
 * 
 * Be aware of the potential dead lock if not handled by the same thread for the above two actions.
 * 
 * A method needs to be added to remove a selectable channel from the selector.  
 * 
 * @author yzhou
 *
 */
public class NIOSelector extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(NIOSelector.class);
	
	private Selector selector;
	
	private ExecutorService service = Executors.newSingleThreadExecutor();
	private Registration pendingRegistration = null;
	
	public NIOSelector() throws IOException {
		selector = Selector.open();
	}
	
	/**
	 * Add a channel to the current selector. 
	 * 
	 * @param channel The channel to be added. 
	 * @param interestSet The interest set. 
	 * @param callback Consumer to be activated with the SelectionKey upon an IO event.
	 * @return A future to indicate if the registration successful. 
	 */
	@TraceCall
	public Future<Boolean> register(SelectableChannel channel, int interestSet, Consumer<SelectionKey> callback) {
		return service.submit(new Registration(channel, interestSet, callback));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		logger.info("selector started");
		while( true ) {
			try {
				int readiness = selector.select();
				if( null != pendingRegistration ) {
					pendingRegistration.register();
					pendingRegistration = null;
				}
				if( readiness == 0 ) continue;
				
				logger.debug("{} channles with events", readiness);
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while( it.hasNext() ) {
					SelectionKey key = it.next();
					it.remove();
					
					try {
						((Consumer<SelectionKey>) key.attachment()).accept(key);
					} catch( Exception e ) {
						logger.error("exception in handling the SelectionKey", e);
					}
				}
			} catch ( Exception e ) {
				logger.error("selector loop exception",e);
			}
		}
	}
	
	private class Registration implements Callable<Boolean> {
		private final SelectableChannel channel;
		private final int interestSet;
		private final Consumer<SelectionKey> callback;
		
		private boolean isSuccess;
		
		Registration(SelectableChannel channel, int interestSet, Consumer<SelectionKey> callback) {
			this.channel = channel;
			this.interestSet = interestSet;
			this.callback = callback;
			
			this.isSuccess = false;
		}
		
		void register() {
			try {
				channel.register(selector, interestSet, callback);
				isSuccess = true;
				logger.info("channel is registered in the selector");
			} catch (ClosedChannelException e) {
				logger.error("failed in registration", e);
				isSuccess = false;
			} 
		}

		@Override
		public Boolean call() throws Exception {
			pendingRegistration = this;
			while( null != pendingRegistration ) {
				selector.wakeup();
				LockSupport.parkNanos(10000000);
			}
			return isSuccess;
		}
	}
}

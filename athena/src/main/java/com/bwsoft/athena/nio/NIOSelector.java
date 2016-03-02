package com.bwsoft.athena.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Registration pendingRegistration;
	
	private final ReentrantLock lock = new ReentrantLock(true);
	
	public NIOSelector() throws IOException {
		selector = Selector.open();
	}
	
	/**
	 * Add a channel to the current selector. 
	 * 
	 * @param channel The channel to be added. 
	 * @param interestSet The interest set. 
	 * @param callback Consumer to be activated with the SelectionKey upon an IO event.
	 * @return The SelectionKey associated with the registration. 
	 * @throws ClosedChannelException
	 */
	public SelectionKey register(SelectableChannel channel, int interestSet, Consumer<SelectionKey> callback) throws ClosedChannelException {
		try {
			lock.lock();
			logger.debug("lock the registration lock.");
			
			// TODO: change this to use Future
			pendingRegistration = new Registration(channel, interestSet, callback);
			selector.wakeup();
			while( ! pendingRegistration.isDone() ) {
				LockSupport.parkNanos(1000000);
			}

			SelectionKey key = pendingRegistration.getSelectionKey();
			ClosedChannelException e = pendingRegistration.getException();
			pendingRegistration = null;

			if( null == e ) {
				return key;
			} else {
				throw e;
			}
		} finally {
			logger.debug("release the registration lock.");
			lock.unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while( true ) {
			try {
				int readiness = selector.select();
				
				// do channel registration
				if( null != pendingRegistration ) {
					pendingRegistration.register();
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
	
	private class Registration {
		private final SelectableChannel channel;
		private final int interestSet;
		private final Consumer<SelectionKey> callback;
		
		private SelectionKey key;
		private ClosedChannelException e;
		private boolean isRegistered;
		
		Registration(SelectableChannel channel, int interestSet, Consumer<SelectionKey> callback) {
			this.channel = channel;
			this.callback = callback;
			this.interestSet = interestSet;
			this.isRegistered = false;
		}
		
		void register() {
			try {
				key = channel.register(selector, interestSet, callback);
				logger.info("a channel is added to the selector");
			} catch (ClosedChannelException e) {
				this.e = e;
			} finally {
				isRegistered = true;
			}
		}
		
		SelectionKey getSelectionKey() {
			return null == e ? key : null;
		}
	
		ClosedChannelException getException() {
			return e;
		}
		
		boolean isDone() {
			return isRegistered;
		}
	}
}

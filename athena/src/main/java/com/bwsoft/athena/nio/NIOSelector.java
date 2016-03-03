package com.bwsoft.athena.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
	
	private final ReentrantLock lock = new ReentrantLock(true);
	
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	public NIOSelector() throws IOException {
		selector = Selector.open();
		logger.info("NIOSelector is constructor");
	}
	
	/**
	 * Add a channel to the current selector. 
	 * 
	 * @param channel The channel to be added. 
	 * @param interestSet The interest set. 
	 * @param callback Consumer to be activated with the SelectionKey upon an IO event.
	 * @return A future to indicate if the registration successful. 
	 * @throws ClosedChannelException
	 */
	public Future<Boolean> register(SelectableChannel channel, int interestSet, Consumer<SelectionKey> callback) throws ClosedChannelException {
		try {
			// TODO: no lock is needed
			lock.lock();
			logger.debug("lock the registration lock.");
			
			return service.submit(()->{
				try {
					channel.register(selector, interestSet, callback);
				} catch (Exception e) {
					logger.error("failed to register to selector",e);
					return false;
				}
				selector.wakeup();
				logger.info("a channel is added to the selector");
				return true;
			});
		} finally {
			logger.debug("release the registration lock.");
			lock.unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		logger.info("selector started");
		while( true ) {
			try {
				int readiness = selector.select();
				
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
}

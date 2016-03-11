package com.bwsoft.athena.jni;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an Observable for an event in the native layer.  
 * 
 * There are two types of the design. One is to use a native thread to push the event back into java layer. Another 
 * is to use a java thread to invoke a native blocking call that only returns upon a native event.
 * 
 * One has to be careful in saving the java object references in the first design. By default, all java object
 * references are local references in the native layer. One has to specifically declare an Object to be a 
 * JNIEnv->NewGlobalRef to save it and call JNIEnv->DeleteGlobalRef to release the memory once no longer 
 * needed. In addition, be aware that JNIEnv is per thread. One can only save JavaJVM obtained from 
 * JNIEnv->GetJavaVM and use AttachCurrentThread to regenerate the JNIEnv for the native thread to use.  
 *  
 * The second one is much cleaner in design and in controlling the object creation and destruction. 
 * 
 * Add "-Xcheck:jni" to java command line to request the VM to do additional validation on the arguments 
 * passed to JNI functions. Note: The option is not guaranteed to find all invalid arguments or diagnose 
 * logic bugs in the application code, but it can help diagnose a large number of such problems.
 * 
 * All jobect allocations in the native code will be GC'ed by JVM when not needed.
 * 
 * @author yzhou
 *
 */
public class ObservableOfNativeByteArray extends Observable implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ObservableOfNativeByteArray.class);

	private boolean isRunning = false;
	private ExecutorService service = Executors.newSingleThreadExecutor();
	private Future<?> future = null;
	
	static {
		try {
			System.loadLibrary("athena");
		} catch( UnsatisfiedLinkError e ) {
			logger.error("failed in loading native library: libathena.so. check the setting of java.library.path.", e);
			System.exit(-1);
		}
	}
	
	/**
	 * Start a native thread that observes the change of an observable.
	 */
	public native void startNativeObservable();
	
	/**
	 * Stops the native thread and releases all referenced java objects. 
	 */
	public native void stopNativeObservable();
	
	/**
	 * Native thread pushes the event back by calling this method. 
	 * 
	 * @param arrayEvent
	 */
	private void setByteArray(byte[] arrayEvent) {
		logger.trace("Receive an event from native thread: {}", new String(arrayEvent));
		this.setChanged();
		this.notifyObservers(arrayEvent);
		this.clearChanged();
	}
	
	/**
	 * Start a java thread to poll the native event. 
	 */
	public void startObservable() {
		isRunning = true;
		future = service.submit(this);
	}
	
	/**
	 * Stop the java thread from polling the native event.
	 * 
	 * @throws InterruptedException
	 */
	public void stopObservable() throws InterruptedException {
		service.shutdown();
		future.cancel(true);
		isRunning = false;
		service.awaitTermination(10, TimeUnit.SECONDS);
	}

	/*
	 * Blocked callback that returns only upon a native event. 
	 */
	private native byte[] getNativeEvent();
	
	@Override
	public void run() {
		System.out.println("running");
		while( isRunning ) {
			byte[] event = this.getNativeEvent();
			logger.trace("Retrieve an event: {}",new String(event));
			this.setChanged();
			this.notifyObservers(event);
			this.clearChanged();
		}
		logger.info("observable exists");
	}

	public static void main(String[] args) throws InterruptedException {
		
		ObservableOfNativeByteArray observable = new ObservableOfNativeByteArray();
		
		// test the event pushed back by the native thread
		observable.startNativeObservable();
		Thread.sleep(5000); 
		// if not stopped, the native thread keeps pushing back the event even the java main thread exits.
		observable.stopNativeObservable();  
		
		// test polling event using a java thread
		observable.startObservable();
		Thread.sleep(5000);
		observable.stopObservable();
		Thread.sleep(1000); // give time for thread to be interrupted and exit. 
	}
}

package com.bwsoft.athena.misc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * FileLock is between multiple processes. Never use this in the same JVM. Instead, using the 
 * ReentrantReadWriteLock among different threads in the same JVM.
 * 
 * @author yzhou
 *
 */
public class FileLockDemo {
	private final Path filename = FileSystems.getDefault().getPath("target", "lockfile.lock");
	
	public FileLockDemo() {
	}
	
	/**
	 * For read, the file is locked with share. It allows concurrent read by multiple processes. 
	 * 
	 * @throws IOException
	 */
	public void readFile() throws IOException {
		try(FileChannel channel = FileChannel.open(filename, StandardOpenOption.READ)){
			// block till file is locked
			FileLock lock = channel.lock(0, Long.MAX_VALUE, true);
			try{
				System.out.println("Locked file for shared read by thread: "+Thread.currentThread().getName());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ByteBuffer buffer = ByteBuffer.allocate(20);
				int noOfBytes = 0;
				
				do {
					noOfBytes = channel.read(buffer);
					if( noOfBytes > 0 ) {
						buffer.flip();
						while( buffer.hasRemaining() ) {
							System.out.print((char) buffer.get());
						}
						buffer.clear();
					}
				} while( noOfBytes > 0 );
			} finally {
				lock.release();
				System.out.println("Complete file read by thread: "+Thread.currentThread().getName());
			}
		}
	}
	
	/**
	 * It uses exclusive lock to ensure this is the only process to write and nothing is read either. 
	 * 
	 * @throws IOException
	 */
	public void writeFile() throws IOException {
		try(FileChannel channel = FileChannel.open(filename, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			FileLock lock = channel.lock(0, Long.MAX_VALUE, false);
			try{
				System.out.println(new Date()+": lock the file for exclusive write by thread: "+Thread.currentThread().getName());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// truncate file only after the obtain of the exclusive lock
				// so that the read will never encounter an empty file. 
				channel.truncate(0);
				
				ByteBuffer buffer = ByteBuffer.allocate(512);
				buffer.put((new Date()+": Write done by thread: "+Thread.currentThread().getName()+System.getProperty("line.separator").toString()).getBytes());
				buffer.flip();
				while( buffer.hasRemaining() ) {
					channel.write(buffer);
				}
				channel.force(true);
			} finally {
				lock.release();
				System.out.println("complete file write by thread:  "+Thread.currentThread().getName());
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		if( args.length == 0 ) {
			System.out.println("Please specify the read or write mode.");
			System.exit(1);
		}
		
		boolean readMode = Boolean.parseBoolean(args[0]);
		System.out.println("Read mode is "+readMode);
		FileLockDemo demo = new FileLockDemo();
		
		ExecutorService service = Executors.newFixedThreadPool(2);
		
		if( ! readMode ) {
			service.execute(()->{
				for( int i = 0; i < 10; i ++ ) {
					try {
						demo.writeFile();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} else {
			service.execute(()->{
				for( int i = 0; i < 10; i ++ ) {
					try {
						demo.readFile();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		
		service.shutdown();
		service.awaitTermination(5000, TimeUnit.SECONDS);
	}
}

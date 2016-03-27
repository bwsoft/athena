package com.bwsoft.athena.misc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileLockDemo {
	private FileLock lock;
	private final Path filename = FileSystems.getDefault().getPath("target", "lockfile.lock");
	
	public FileLockDemo() {
	}
	
	/**
	 * For read, the file is locked with share. It allows multiple threads to read concurrently. 
	 * But only one thread with exclusive lock can write.
	 * @throws IOException
	 */
	public void readFile() throws IOException {
		try(FileChannel channel = FileChannel.open(filename, StandardOpenOption.READ)){
			// block till file is locked
			lock = channel.lock(0, Long.MAX_VALUE, true);
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
							System.out.println((char) buffer.get());
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
	
	public void writeFile() throws IOException {
		try(FileChannel channel = FileChannel.open(filename, StandardOpenOption.CREATE)) {
			lock = channel.lock(0, Long.MAX_VALUE, false);
			try{
				System.out.println("lock the file for exclusive write by thread: "+Thread.currentThread().getName());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ByteBuffer buffer = ByteBuffer.allocate(512);
				buffer.put(("Write done by thread: "+Thread.currentThread().getName()+System.getProperty("line.separator").toString()).getBytes());
				buffer.flip();
				while( buffer.hasRemaining() ) {
					channel.write(buffer);
				}
			} finally {
				lock.release();
				System.out.println("complete file write by thread:  "+Thread.currentThread().getName());
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		FileLockDemo demo = new FileLockDemo();
		
		ExecutorService service = Executors.newFixedThreadPool(2);
		
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
		
		service.shutdown();
		service.awaitTermination(5000, TimeUnit.SECONDS);
	}
}

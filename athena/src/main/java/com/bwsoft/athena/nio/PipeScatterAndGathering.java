package com.bwsoft.athena.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class PipeScatterAndGathering {
	
	private Pipe pipe;
	
	private ByteBuffer[] readBuffers = {ByteBuffer.allocate(4), ByteBuffer.allocate(256)};
	private ByteBuffer[] writeBuffers = {ByteBuffer.allocate(4), ByteBuffer.allocate(256)};
	
	public PipeScatterAndGathering() throws IOException {
		pipe = Pipe.open();
	}
	
	public void sinkData(String msg) throws IOException {
		int length = msg.getBytes().length;
		writeBuffers[0].putInt(length);
		writeBuffers[1].put(msg.getBytes());
		writeBuffers[0].flip();
		writeBuffers[1].flip();
		SinkChannel channel = pipe.sink();
		long num = channel.write(writeBuffers);
		while( num != length + 4 )
			num += channel.write(writeBuffers);
	}
	
	public void sourceData() throws IOException {
		SourceChannel channel = pipe.source();
		System.out.println("Channel blocking is: "+channel.isBlocking());
		long num = channel.read(readBuffers); 
		int toBeRead = 4;
		while( num < toBeRead ) {
			num += channel.read(readBuffers);
		}
		readBuffers[0].flip();
		toBeRead = readBuffers[0].asIntBuffer().get();
		toBeRead += 4;
		System.out.println("Data to be read: "+toBeRead);
		while( num < toBeRead ) {
			num += channel.read(readBuffers);
		}
		readBuffers[1].flip();
		byte[] data = new byte[1024];
		readBuffers[1].get(data, 0, toBeRead-4);
		System.out.println("Message: "+new String(data));
	}
	
	public static void main(String[] args) throws IOException {
		PipeScatterAndGathering demo = new PipeScatterAndGathering();

		ExecutorService service = Executors.newSingleThreadExecutor();
		service.submit(()->{
			try {
				demo.sourceData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		});
		
		LockSupport.parkNanos(1000);
		demo.sinkData("Hello from demo");
		demo.sourceData();
		
		service.shutdown();
	}
}

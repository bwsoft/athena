package com.bwsoft.athena.nio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class Unicast {
	private final String host;
	private final short port;
	
	public Unicast(String host, String port) {
		this.host = host;
		this.port = Short.parseShort(port);
	}
	
	public void send() throws IOException {
		String msg = "Hello world";
		DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName(host), port);
		try( DatagramSocket socket = new DatagramSocket() ) {
			int count = 0;
			while( count ++ < 10 ) {
				System.out.println("sending ...");
				socket.send(packet);
				LockSupport.parkNanos(1000000000);
			}
		}
		System.out.println("sending complete");
	}
	
	public void recv() throws IOException {
		System.out.println("starting receiver ...");
		byte[] buffer = new byte[4096];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName(host));
//		InetSocketAddress iaddr = new InetSocketAddress(host,0);
//		socket.bind(iaddr);
		while(true) {
			socket.receive(packet);
			System.out.println(new String(packet.getData()));
		}
	}
	
	public static void main(String[] args) {
		Unicast demo = new Unicast("127.0.0.1", "20005");
		ExecutorService service = Executors.newFixedThreadPool(2);
		
		service.execute(()->{
			try{
				demo.recv();
			} catch( Exception e ) {
				e.printStackTrace();
			}
		});
		
		service.execute(()->{
			try{
				demo.send();
			} catch( Exception e ) {
				e.printStackTrace();
			}
		});
		
		service.shutdown();
		
	}
}

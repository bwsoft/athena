package com.bwsoft.athena.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulticastSender {
	private static final Logger logger = LoggerFactory.getLogger(AsyncMulticastListener.class);
	
	private NetworkInterface nic;
	private InetAddress group;
	private int port;

	// size of MTU should be allocated
	private final ByteBuffer buffer = ByteBuffer.allocate(1500);

	private int repeat;
	private long sendingInterval;
	private String msg = "Hello from multicast/broadcast";
	
	public MulticastSender(String nic, String group, int port) throws SocketException, UnknownHostException {
		this.nic = NetworkInterface.getByName(nic);
		this.group = InetAddress.getByName(group);
		this.port = port;
		
		this.repeat = 1;
		this.sendingInterval = 1000;
	}
	
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public void setSendingInterval(long sendingInterval) {
		this.sendingInterval = sendingInterval;
	}
	
	public void setMessage(String msg) {
		this.msg = msg;
	}
	
	public void start() throws IOException {
		try (DatagramChannel channel = DatagramChannel.open()
				.setOption(StandardSocketOptions.IP_MULTICAST_IF, nic)
				.setOption(StandardSocketOptions.SO_REUSEADDR, true)
				.setOption(StandardSocketOptions.IP_MULTICAST_TTL, 128)
				.bind(new InetSocketAddress(port)) ) {

			for( int i = 0; i < repeat; i ++ ) {
				buffer.put((new Date() +" : "+msg).getBytes());
				buffer.flip();
				channel.send(buffer, new InetSocketAddress(group, port));
				buffer.clear();
				try {
					TimeUnit.MILLISECONDS.sleep(sendingInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

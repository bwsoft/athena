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
import java.nio.channels.SelectionKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncMulticastListener {
	private static final Logger logger = LoggerFactory.getLogger(AsyncMulticastListener.class);
	
	private NetworkInterface nic;
	private InetAddress group;
	private int port;
	private DatagramChannel channel;
	
	// size of MTU should be allocated
	private final ByteBuffer buffer = ByteBuffer.allocate(1500);
	private byte[] array = new byte[1500];
	
	private NIOSelector selector;
	
	public AsyncMulticastListener(String nic, String group, int port) throws SocketException, UnknownHostException {
		this.nic = NetworkInterface.getByName(nic);
		this.group = InetAddress.getByName(group);
		this.port = port;
	}
	
	public void setNioSelector(NIOSelector selector) {
		this.selector = selector;
	}
	
	public void init() throws IOException {
		logger.info("init async multicast listener");
		if( null == selector )
			throw new NullPointerException("selector is not defined");
		
		channel = DatagramChannel.open()
				.setOption(StandardSocketOptions.IP_MULTICAST_IF, nic)
				.setOption(StandardSocketOptions.SO_REUSEADDR, true)
				.bind(new InetSocketAddress(port));
		
		channel.configureBlocking(false);
		
		// register to the selector
		selector.register(channel, SelectionKey.OP_READ, key -> {
			if( key.isReadable() ) {
				try {
					((DatagramChannel)key.channel()).receive(buffer);
				} catch (Exception e) {
					logger.error("failed in receiving data",e);
				}
				buffer.flip();
				buffer.get(array, 0, buffer.limit());
				
				logger.info("Receive a msg: {}",new String(array,0,buffer.limit()));
				buffer.clear();
			} else {
				logger.warn("Unregistered interests");
			}
		});
		
		// join the group to receive multicast or broadcast (ipv4 only)
		channel.join(group,nic);
	}
	
	public void destroy() {
		logger.info("destroy async multicast listener");		
	}
}

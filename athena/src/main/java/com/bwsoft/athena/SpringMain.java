package com.bwsoft.athena;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bwsoft.athena.nio.MulticastSender;
import com.bwsoft.athena.nio.NIOSelector;

public class SpringMain {
	public static void main(String[] args) throws BeansException, IOException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");

		NIOSelector selector = (NIOSelector) ctx.getBean("nioSelector");
		selector.start();
		
		// bean needs to be got specifically since they are being specified to be lazy-init
		ctx.getBean("asyncMulticastListener");
		((MulticastSender) ctx.getBean("multicastSender")).start();
	}
}

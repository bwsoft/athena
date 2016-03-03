package com.bwsoft.athena;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bwsoft.athena.nio.NIOSelector;

public class SpringMain {
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");

		NIOSelector selector = (NIOSelector) ctx.getBean("nioSelector");
		selector.start();
		
		ctx.getBean("asyncMulticastListener");
	}
}

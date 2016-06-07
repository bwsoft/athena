package com.bwsoft.athena.jmx;

import java.lang.management.ManagementFactory;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class JMXMain {
	public static void main(String[] args) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, InterruptedException {
		System.setProperty("com.sun.management.jmxremote.port","9999");
		System.setProperty("com.sun.management.jmxremote.authenticate", "false");
		System.setProperty("com.sun.management.jmxremote.ssl", "false");
				
		MBeanServer agent = ManagementFactory.getPlatformMBeanServer();
		Hello hello = new Hello();
		
		ObjectName name = new ObjectName("com.bwsoft.athena.jmx:type=Hello");
		agent.registerMBean(hello, name);
		
		Queue<String> queue = new ArrayBlockingQueue<String>(10);
		queue.add("first");
		queue.add("second");
		
		QueueSampler sampler = new QueueSampler(queue);
		agent.registerMBean(sampler, new ObjectName("com.bwsoft.athena.jmx:type=QueueSampler"));
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}

package com.bwsoft.poc.hazelcast;

import java.util.concurrent.locks.LockSupport;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;

/**
 * Hazelcast storage:
 * 	Partition based: There are 271 partitions by default.
 * 	Partitions are equally distributed among members. A member will be primary 
 * 	for some partition but backup for others.
 * 	Data is distriubuted to a partition based upon the hash of its key.
 *  Starting with Hazelcast 3.6, a light member is introduced which does not
 *  own any partition.
 *  
 * Partition Table:
 * 	When a member is started, a partition table is created within it. The table
 * 	stores the partition IDs and the cluster members to which they belong. 
 * 	The purpose of this table is to make all members, including lite members, 
 * 	in the cluster aware of this information, making sure each 
 * 	member knows where the data is.
 * 
 * Repartition:
 * 	Repartition is the process of redistribution of partition ownership. It 
 * 	is performed in the following scenarios:
 * 		When a member joins to a cluster.
 * 		When a member leaves a cluster.
 * 	
 * Discovering cluster members via multicast, TCP, EC2 cloud, or jcloud.
 * 
 * @see <a href="http://docs.hazelcast.org/docs/3.7/manual/html-single/index.html">
 * http://docs.hazelcast.org/docs/3.7/manual/html-single/index.html
 * </a>
 * 
 * @author yzhou
 *
 */
public class HazelcastDemo {
	public static void main(String[] args) {
		HazelcastInstance instance1 = Hazelcast.getHazelcastInstanceByName("instance1");
		if( null == instance1 )
			instance1 = createMember("instance1", 6001, "127.0.0.1:6002");
		
		HazelcastInstance instance2 = Hazelcast.getHazelcastInstanceByName("instance2");
			instance2 = createMember("instance2", 6002, "127.0.0.1:6001");
		
		// monitor the instance1 activities via instance2
		addMapListener(instance2);
		
		// populate map using instance 1
		populateMap(instance1);
		populateMap(instance1);
		
		// create a hazelcast client
		HazelcastInstance client = createHazelcastClient("127.0.0.1:6001","127.0.0.1:6002");
		
		// use client to display the current map
		displayMap(client);
		
		System.out.println("Shutting down instance 1 ...");
		instance1.shutdown();
		LockSupport.parkNanos(10000000000l);
		
		// add another entry to the map via instance 2
		populateMap(instance2);
		
		displayMap(client);
		
		System.out.println("Bring back instance 1 and shut down instance 2 ...");
		instance1 = Hazelcast.getHazelcastInstanceByName("instance1");
		if( null == instance1 )
			instance1 = createMember("instance1", 6001, "127.0.0.1:6002");
		LockSupport.parkNanos(10000000000l);
		instance2.shutdown();
		LockSupport.parkNanos(10000000000l);

		displayMap(client);
		
		System.out.println("Shuting down instance 1 ...");
		instance1.shutdown();
		LockSupport.parkNanos(10000000000l);

		displayMap(client);
	}
	
	// create a hazelcast member
	public static HazelcastInstance createMember(String instanceName, int port, String peerList) {
		Config cfg = new Config();
		
		// set property to enable the add/update/delete events
		cfg.setProperty("hazelcast.map.entry.filtering.natural.event.types", "true");
		cfg.setInstanceName(instanceName);
		
		NetworkConfig networkCfg = cfg.getNetworkConfig();
		networkCfg.setPort(port);
		networkCfg.setPortAutoIncrement(false);
		
		JoinConfig joinCfg = networkCfg.getJoin();
		joinCfg.getMulticastConfig().setEnabled(false);
		joinCfg.getAwsConfig().setEnabled(false);
		TcpIpConfig tcpipCfg = joinCfg.getTcpIpConfig();
		tcpipCfg.addMember(peerList);
		tcpipCfg.setEnabled(true);
		
		return Hazelcast.newHazelcastInstance(cfg);
	}
	
	/**
	 * Each call, a customer will be added to the "customers" map with an
	 * increasing integer prepended. 
	 * 
	 * @param instance
	 */
	public static void populateMap(HazelcastInstance instance) {
		IMap<Integer, String> mapCustomers = instance.getMap("customers");
		int idx = mapCustomers.size() + 1;
		mapCustomers.put(idx, "Joe"+idx);
		
		System.out.println("Customer with key 1: "+mapCustomers.get(1));
		System.out.println("Map size: "+mapCustomers.size());
	}
	
	public static void displayMap(HazelcastInstance instance) {
		try {
			IMap<Integer, String> mapCustomers = instance.getMap("customers");
			System.out.println("Customer with key 2: "+mapCustomers.get(2));
			System.out.println("Map size: "+mapCustomers.size());
		} catch( Exception e) {
			e.printStackTrace();
			instance.shutdown();
		}
	}
	
	public static void addMapListener(HazelcastInstance instance) {
		IMap<Integer, String> mapCustomers = instance.getMap("customers");
		mapCustomers.addEntryListener(new MyEntryListener(), false);
	}
	
	public static HazelcastInstance createHazelcastClient(String ... clusters) {
		ClientConfig clientCfg = new ClientConfig();
		clientCfg.getNetworkConfig().addAddress(clusters);
		return HazelcastClient.newHazelcastClient(clientCfg);
		
	}
	
	static class MyEntryListener implements EntryAddedListener<String, String>, EntryUpdatedListener<String, String>, EntryRemovedListener<String, String> {

		public void entryRemoved(EntryEvent<String, String> arg0) {
			// TODO Auto-generated method stub
			System.out.println("Entry removed: "+arg0);
			
		}

		public void entryUpdated(EntryEvent<String, String> arg0) {
			System.out.println("Entry updated: "+arg0);
			
		}

		public void entryAdded(EntryEvent<String, String> arg0) {
			System.out.println("Entry added: "+arg0);
			
		}
		
	}
}

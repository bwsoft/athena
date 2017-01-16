package com.bwsoft.sample.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class EventProducer {
	public static void main(String[] args) {
		Properties props = new Properties();
		
		// require to start kafka server first using config/server.properties
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		// topic my-replicated-topic has to be pre-created using
		// bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic
		try( Producer<String, String> producer = new KafkaProducer<String, String>(props); ) {
			for( int i = 0; i < 100; i ++ ) {
				producer.send(new ProducerRecord<String, String>("my-replicated-topic", Integer.toString(i), "This is string "+i));
			}
		}
	}
}

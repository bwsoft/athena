package com.bwsoft.athena.misc;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class WatchServiceDemo extends Thread {
	private Path dirToWatch;
	private WatchKey key;
	private WatchService service;
	
	public WatchServiceDemo() throws IOException {
		dirToWatch = FileSystems.getDefault().getPath("target");
		service = FileSystems.getDefault().newWatchService();
		
		key = dirToWatch.register(service, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY, OVERFLOW);
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				WatchKey key = service.take();
				List<WatchEvent<?>> events = key.pollEvents();
				for( WatchEvent<?> event : events ) {
					WatchEvent<Path> fileEvent = (WatchEvent<Path>) event;
					Path filename = fileEvent.context();
					System.out.println(fileEvent.kind().name()+" on "+filename+" is detected.");
				}
				key.reset();
			} catch( Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new WatchServiceDemo().start();
	}
}

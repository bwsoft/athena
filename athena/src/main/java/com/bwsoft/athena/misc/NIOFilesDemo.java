package com.bwsoft.athena.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class NIOFilesDemo {

	public static void main(String[] args) throws IOException {
		FileSystem fs = FileSystems.getDefault();
		
		Path path = fs.getPath("src", "main");
		if( Files.exists(path)) {
			System.out.println("exists as directory: "+Files.isDirectory(path));
			try ( DirectoryStream<Path> ds = Files.newDirectoryStream(path) ) {
				ds.forEach(t->{
					System.out.println("Name: "+t.toFile().getName());
					System.out.println("Is directory? "+Files.isDirectory(t));
				});				
			}
		}
		
		path = fs.getPath("src", "main","java","com","bwsoft","athena","misc","NIOFilesDemo.java");
		if( Files.exists(path)) {
			if( Files.isRegularFile(path)) {
				try (BufferedReader br = Files.newBufferedReader(path)){
					br.lines().forEach(t->System.out.println(t));
				}
			}
		}
	}
}

package com.bwsoft.athena.misc;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IteratorDemo {
	
	public static void main(String[] args) {
		IntStream intStream = IntStream.range(1, 10);
		List<Integer> intList = intStream.boxed().collect(Collectors.toList());
		
		Iterator<Integer> it = intList.iterator();
		while( it.hasNext() ) {
			Integer value = it.next();
			if( value % 2 == 0 )
				it.remove();
		}
		
		intList.forEach(t->System.out.println("Value is: "+t));
		
		String[] array = {"abd","cgf","klm","abcd","87ang"};
		Supplier<Stream<String>> supplier = ()->Stream.of(array);
		supplier.get().filter(t->t.contains("a")).forEach(t->System.out.println(t));
		
		// use a new stream to do something else
		supplier.get().map(t->t.replace("a", "AAA")).forEach(t->System.out.println(t));
	}
}

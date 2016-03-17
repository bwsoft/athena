package com.bwsoft.athena.misc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GenericDemo {
	static <T> T pick(T a1, T a2) {
		return a2;
	}
	
	static double sumOfList(List<? extends Number> list) {
		double sum = 0;
		for( Number num : list ) {
			sum += num.doubleValue();
		}
		
		return sum;
	}
	
	public static void main(String[] args) {
		// Type inference: The most common type, Serializable, is picked as the type.
		Serializable pickResult = pick("A",2);
		System.out.format("Type inference: %d \n", (int) pickResult);
		
		System.out.format("Type inference: %f, %f\n", sumOfList(Arrays.asList(1,2,3)), sumOfList(Arrays.asList(1.1,1.2)));
	}
}

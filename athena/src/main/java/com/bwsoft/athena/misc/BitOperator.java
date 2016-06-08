package com.bwsoft.athena.misc;

import java.io.UnsupportedEncodingException;

/**
 * Two's complement: switching all ones to zeros and all zeros to ones 
 * and then adding one to the result.
 * 
 * A negative integer is the two's complement of the corresponding positive number. 
 * 
 * Java bit operator: & | ^ ~
 * 
 * @author yzhou
 *
 */
public class BitOperator {
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
		/**
		 * A character can have value between \u0000 t0 \uFFFF. However the range between
		 * \uD800 to \uDFFF is reserved for surrogate pair. Between \uD800 t0 \uDBFF is the
		 * range for high surrogate. Between \uDC00 to \uDFFF is low surrogate.
		 */
		char unicode = '\u0041'; // 'A'
		
		/**
		 * String can contain surrogate pair which is within the range between \u10000 to \u10FFFF.
		 * It has to be specified by two characters but will be displayed as 1.
		 * 
		 * String, "\u0041\uD801\uDC37", contains two literals, three characters, and hence the string length
		 * is 3. 
		 * 
		 * UTF-8 encoded array size is 5 which is one byte for character 'A' and 4 bytes for the 
		 * surrogate pair.
		 * 
		 * UTF-16 encoded array size is 8 which include a 2 byte UTF-16 marker and the sum of 
		 * 2 bytes of every characters. 
		 */
		String str = "\u0041\uD801\uDC37"; 
		
		System.out.println("String: "+str+", string.length()="+str.length() + 
				", UTF-8 encoded array size=" + str.getBytes("utf-8").length + 
				", UTF-16 encoded array size=" + str.getBytes("utf-16").length);
		
		/**
		 * To convert a number to its opposite sign, add 1 to the bitwise compliment of the value.
		 */
		int i = 4356;
		int j = ~i + 1;
		int k = ~j + 1;
		
		System.out.format("%d %d %d\n", i, j, k);
		
		byte n = -128;
		System.out.println("byte n = -128: binary representation: "+ Integer.toBinaryString(0x000000ff & n));
		System.out.println("byte n = -128: binary representation when implictly converted to integer: " + Integer.toBinaryString(n));
		n = 127;
		System.out.println("byte n = 127: binary representation: "+ Integer.toBinaryString(0x000000ff & n));
		System.out.println("byte n = 127: binary representation when implictly converted to integer: " + Integer.toBinaryString(n));
		
		/**
		 * Integer literals: 
		 * An integer value can be declared in decimal, octal, or hexadecimal notation. 
		 * Any sequence of digits that does not start with a zero is considered a decimal 
		 * integer value. If the sequence is all digits but starts with a zero, it's assumed 
		 * to be an octal value. If the literal starts with 0X or 0x, it's taken to be a 
		 * hexadecimal value.
		 */
		int hexcode = 0x0041;
		int octcode = 0101;
		int bincode = 0b1000001;
		
		System.out.format("Integer value of '\u0041', 0x0041, 0101, 0b1000001: %d, %d, %d %d\n",
				(int) unicode, hexcode, octcode, bincode);


		int a = 60;	/* 60 = 0011 1100 */  
		int b = 13;	/* 13 = 0000 1101 */
		int c = 0;

		c = a & b;       /* 12 = 0000 1100 */ 
		System.out.println("a & b = " + c );

		c = a | b;       /* 61 = 0011 1101 */
		System.out.println("a | b = " + c );

		c = a ^ b;       /* 49 = 0011 0001 */
		System.out.println("Bitwise xor of a ^ b = " + c );

		c = ~a;          /*-61 = 1100 0011 */
		System.out.println("Bitwise complement of ~a = " + c );

		c = a << 2;     /* 240 = 1111 0000 */
		System.out.println("a << 2 = " + c );

		c = a >> 2;     /* 15 = 1111 */
		System.out.println("a >> 2  = " + c );

		c = a >>> 2;     /* 15 = 0000 1111 */
		System.out.println("a >>> 2 = " + c );
	}
}

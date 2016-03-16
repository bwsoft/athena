package com.bwsoft.athena.misc;

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
	public static void main(String[] args) {
		// constant liter
		int unicode = '\u0041';
		int hexcode = 0x0041;
		int octcode = 0101;
		
		System.out.format("Integer value of '\u0041', 0x0041, 0101: %d, %d, %d\n",unicode, hexcode, octcode);

		byte n = -128;
		System.out.println("A byte of -128 when converted to integer will have a binary representation of: "+Integer.toBinaryString(n)+" and value of " + (int) n);
		System.out.println("However its bit patter in byte is: "+Integer.toBinaryString(0x000000ff & n));
		byte p = 127;
		System.out.println("A byte of 127 when converted to integer will have a binary representation of: "+Integer.toBinaryString(p)+" and value of " + (int) p);
		System.out.println("Its bit patter in byte is: "+Integer.toBinaryString(0x000000ff & p));

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

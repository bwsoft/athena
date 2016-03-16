package com.bwsoft.athena.misc;

import java.io.UnsupportedEncodingException;

/**
 * The char data type is a single 16-bit Unicode character. It has a minimum 
 * value of '\u0000' (or 0) and a maximum value of '\uffff' (or 65,535 inclusive). 
 * When chars are "serialized" using UTF-8, they may produce one, two or three bytes in 
 * the resulting byte array.
 * 
 * Java strings are encoded in memory using UTF-16 instead. Unicode codepoints 
 * that do not fit in a single 16-bit char will be encoded using a 2-char pair known 
 * as a surrogate pair.  string.length() is equal to the number of 16-bit Unicode 
 * characters in the string. 
 * 
 * It is a good practice to specify the character encoding in getBytes() or 
 * new string(byte[], charset) to avoid the uncertainty introduced by the 
 * platform dependency.
 *  
 * @author yzhou
 *
 */
public class CharEncoding {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "A\u0081\u0801B";
		System.out.println("A four character string: "+str);
		System.out.println("The utf-8 byte array representation is: ");
		byte[] utf8array = str.getBytes("utf-8");
		for( int i = 0; i < utf8array.length; i ++ ) {
			String binaryStr = Integer.toBinaryString(0x000000ff & utf8array[i]);
			while( binaryStr.length() < 8 ) {
				binaryStr = "0"+binaryStr;
			}
			System.out.print(binaryStr+" ");
		}
		System.out.println();
		System.out.println("It contains bits of code point 7, 11 (2 bytes), 16 (3 bytes), and 7.");
		
		System.out.println("UTF-16 representation of the same string: ");
		byte[] utf16array = str.getBytes("utf-16");
		for( int i = 0; i < utf16array.length; i ++ ) {
			String binaryStr = Integer.toBinaryString(0x000000ff & utf16array[i]);
			while( binaryStr.length() < 8 ) {
				binaryStr = "0"+binaryStr;
			}
			System.out.print(binaryStr+" ");
		}
		System.out.println();
		
		System.out.println("UTF 16 byte array is converted back into string using UTF-8: "+new String(utf16array,"utf-8"));
		System.out.println("UTF 16 byte array is converted back into string using UTF-16: "+new String(utf16array,"utf-16"));
	}
}

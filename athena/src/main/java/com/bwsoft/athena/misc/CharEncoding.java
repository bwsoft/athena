package com.bwsoft.athena.misc;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * The char data type is a single 16-bit Unicode character. It has a minimum 
 * value of '\u0000' (or 0) and a maximum value of '\uffff' (or 65,535 inclusive). 
 * When chars are "serialized" using UTF-8, they may produce one, two or three bytes in 
 * the resulting byte array.
 * 
 * Java strings are encoded in memory using UTF-16. Unicode codepoints 
 * that do not fit in a single 16-bit char will be encoded into two 16-bit code units called 
 * surrogate pairs.  String.length() is equal to the number of 16-bit Unicode 
 * characters in the string. A surrogate pair, though representing a character in the real world, 
 * will be counted as two characters in java. 
 * 
 * Platform default encoding will decide how the string will be represented by a byte array 
 * in case the default string.getBytes is invoked. Hence it is a good practice to specify 
 * the character encoding in getBytes() or new string(byte[], charset) to avoid the uncertainty 
 * introduced by the platform dependency.
 *  
 * @author yzhou
 * @see <a href="https://en.wikipedia.org/wiki/UTF-8"> UTF-8 </a>
 * @see <a href="https://en.wikipedia.org/wiki/UTF-16"> UTF-16 </a>
 *
 */
public class CharEncoding {
	
	/**
	 * It converts a string to its corresponding byte array representation based upon the specified encodingType. UTF-8 encoding type
	 * will return a byte array size from 1 to 4 bytes for a given character. UTF-16 encoding type will return a byte array size from 4 to 6 
	 * bytes per character. 
	 * 
	 * THe returned array can only be converted back into the original string by specifying the right encoding type.
	 * 
	 * @param str
	 * @param encodingType
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] byteArrayRepresentation(String str, String encodingType) throws UnsupportedEncodingException {
		byte[] utfArray = str.getBytes(encodingType);
		System.out.print("Encoding type: "+encodingType+", str.getBytes("+encodingType+").length: "+utfArray.length+", Binary representation: ");
		for( int i = 0; i < utfArray.length; i ++ ) {
			String binaryStr = Integer.toBinaryString(0x000000ff & utfArray[i]);
			while( binaryStr.length() < 8 ) {
				binaryStr = "0"+binaryStr;
			}
			System.out.print(binaryStr+" ");
		}
		System.out.println();		
		return utfArray;
	}
	
	public static void stringInfo(String unicodeDisplay, String str) {
		System.out.println("Unicode based representation: "+unicodeDisplay+", string display: "+str+", String.length(): "+str.length());
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("UTF-8 encoding has the following characteristics: ");
		System.out.println("  One byte encoding starts with 0");
		System.out.println("  two bytes encoding starts with 110 at the highest byte, and 10 at the next byte");
		System.out.println("  three bytes encoding starts with 1110 at the highest byte, and 10 at the next two bytes");
		System.out.println("  four bytes encoding starts with 11110 at the highest byte, and 10 at the remaining thress bytes");
		
		System.out.println("UTF-16 encoding has the following characteristics");
		System.out.println("  It starts with a UTF-16 marker: 11111110 11111111");
		System.out.println("  A character consists of 2 or 4 bytes (in the case of surrogate pair)");
		
		System.out.println();
		
		System.out.println("The default character encoding of the platform is: "+Charset.defaultCharset());

		System.out.println();

		String str = "A\u0081\u0801B";
		stringInfo("A\\u0081\\u0801B",str);
		byteArrayRepresentation(str, "utf-8");
		byte[] utf16 = byteArrayRepresentation(str, "utf-16");
		
		
		System.out.println("UTF 16 byte array is converted back into string using UTF-8 encoding: "+new String(utf16,"utf-8"));
		System.out.println("UTF 16 byte array is converted back into string using UTF-16 encoding: "+new String(utf16,"utf-16"));

		System.out.println();
		
		str = "\u0037";
		stringInfo("\\u0037", str);
		byteArrayRepresentation(str, "utf-8");
		byteArrayRepresentation(str, "utf-16");

		System.out.println();
		
		str = "\u0437";
		stringInfo("\\u0437", str);
		byteArrayRepresentation(str, "utf-8");
		byteArrayRepresentation(str, "utf-16");

		System.out.println();

		str = "\u10437";
		stringInfo("\\u10437",str);
		byteArrayRepresentation(str, "utf-8");
		byteArrayRepresentation(str, "utf-16");

		System.out.println();		
	}
}

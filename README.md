# athena
The project demonstrates usage of different technologies.

## Java string/character encoding 

The char data type is a single 16-bit Unicode character. It has a minimum value of '\u0000' (or 0) and a maximum value of '\uffff' (or 65,535 inclusive). When chars are "serialized" using UTF-8, they may produce one, two or three bytes in the resulting byte array.

Java strings are encoded in memory using UTF-16. Unicode codepoints that do not fit in a single 16-bit char will be encoded into two 16-bit code units called surrogate pairs.  String.length() is equal to the number of 16-bit Unicode characters in the string. A surrogate pair, though representing a character in the real world, will be counted as two characters in java. 

Platform default encoding will decide how the string will be represented by a byte array in case the default string.getBytes is invoked. Hence it is a good practice to specify the character encoding in getBytes() or new string(byte[], charset) to avoid the uncertainty introduced by the platform dependency.

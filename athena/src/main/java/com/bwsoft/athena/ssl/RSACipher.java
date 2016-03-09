package com.bwsoft.athena.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is for asymmetric public-private key based encryption. The reception side shares its public 
 * key with all publishers. Publish encrypts the message using the public key. And the message can
 * only be decoded by the receiver with the private key.
 * 
 * Public/private key can also be used in verifying the signature. Consider two parties, A and B.
 * They have the public key of the opposite side. A, when sending a message to B, can provide its
 * signature by encrypting its signature using its private key. The message along with encrypted 
 * signature is sent to B by encrypting the whole package using B's public key. This package can 
 * only be decoded by B's private key. And the encrypted signature in the package can only be 
 * decrypted by A's public key that is in B's possession.
 * 
 * Follow the following steps to generate public/private key
 * 1.) RSA private key
 * openssl genrsa -out "private.pem" 2048
 * 
 * 2.) Obtain public key
 * openssl rsa -in "private.pem" -inform pem -out "public.key" -outform der -pubout
 * 
 * 3.) Encrypt the private key
 * openssl pkcs8 -topk8 -inform pem -outform der -in private.pem -out private.der -nocrypt
 * 
 * 4.) Delete private.pem
 * 
 * @author yzhou
 *
 */
public class RSACipher {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private String transformation;
	
	public RSACipher() {
		
	}
	
	public void setPublicKeyFile(String filename) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		File file = new File(filename);
		
		// there is a limitation on the key file size.
		int fsize = (int) file.length();
		try(InputStream is = new FileInputStream(file) ) {
			byte[] keyBytes = new byte[fsize];
			is.read(keyBytes);
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
		}
	}
	
	public void setPrivateKeyFile(String filename) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		File file = new File(filename);
		
		// there is a limitation on the key file size.
		int fsize = (int) file.length();
		try(InputStream is = new FileInputStream(file) ) {
			byte[] keyBytes = new byte[fsize];
			is.read(keyBytes);
			privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
		}		
	}
	
	@Required
	public void setTransformation(String transformation) {
		this.transformation = transformation;
	}
	
	/**
	 * It encrypts a message using a defined transformation. The sender using the public key to 
	 * encrypt the message and only the receiver with the private key is able to decode it.
	 * 
	 * @param rawMsg
	 * @param encoding
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public String encrypt(String rawMsg, String encoding) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		return Base64.getEncoder().encodeToString(cipher.doFinal(rawMsg.getBytes(encoding)));
	}
	
	/**
	 * Decrypts the message using the private key. The only place to decrypt a message using public 
	 * key is in signature verification.
	 * 
	 * @param encodedMsg
	 * @param encoding
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public String decrypt(String encodedMsg, String encoding) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		
		return new String(cipher.doFinal(Base64.getDecoder().decode(encodedMsg.getBytes())), encoding);
	}
	
	public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
		
		RSACipher cipher = (RSACipher) ctx.getBean("rsaCipher");
		String encodedMessage = cipher.encrypt("Hello from the world", "UTF-8");
		System.out.println("Message is encrypted to: "+ encodedMessage);
		System.out.println("Message is decrypted to: "+cipher.decrypt(encodedMessage, "UTF-8"));
	}
}

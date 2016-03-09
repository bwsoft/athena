package com.bwsoft.athena.ssl;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keystore contains both the private key and public certificates. Keystore file fomart needs to
 * be specified correctly. Otherwise its type needs to be defined. 
 * 
 * Truststore is for a client to collect all public certificates that are accepted by the client. Java
 * is shipped with a default truststore at jre/lib/security/cacert.
 * 
 * Steps to create a self signed keystore and trust store as following:
 * 
 * 1.) Create a keystore
 * keytool -genkey -alias mykey -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -validity 365 -keypass changeit -keystore SSLKeystore.jks -storepass changeit
 * 
 * 2.) Export the public certificate
 * keytool -export -alias mykey -file root.cer -keystore SSLKeystore.jks -storepass changeit
 * 
 * 3.) Import the public certificate to the client trust store
 * keytool -import -alias mykey -file root.cer -keystore trust.jks -storepass changeme
 * 
 * @author yzhou
 *
 */
public class SSLServerSession {
	private static final Logger logger = LoggerFactory.getLogger(SSLServerSession.class);
	
	public static void main(String[] args) throws IOException {
		// Define a keystore.
		System.setProperty("javax.net.ssl.keyStore", "src/main/resources/SSLKeystore.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
		
		SSLServerSocket ss = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(5432);
		while( true ) {
			logger.info("Accepting incoming connection ...");
			Socket s = ss.accept();
			
			// once connection completed, verify the agreed SSL cipher
			SSLSession session = ((SSLSocket) s).getSession();
			Certificate[] certificates = session.getLocalCertificates();
			if( null == certificates ) {
				logger.error("no certificate sent to the peer during the handshake.");
				continue;
			}
			
			for( Certificate cert : certificates ) {
				logger.info("The principal of the certicate used in handshake is {}", ((X509Certificate) cert).getSubjectDN());
			}
			
			logger.info("Peer host: {}", session.getPeerHost());
			logger.info("Cipher is: {}", session.getCipherSuite());
			logger.info("Protocol is {}", session.getProtocol());
			logger.info("Session ID is {}", new BigInteger(session.getId()));
			logger.info("Session is created at: {}", session.getCreationTime());
			logger.info("The last access: {}", session.getLastAccessedTime());
			
			PrintStream ps = new PrintStream(s.getOutputStream());
			ps.println("Hi, welcome to the session");
			ps.flush();
			ps.close();
			s.close();
		}
	}
}

package com.bwsoft.athena.ssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Refer to the comment of SSLServerSession on creating a trust store.
 * 
 * @author yzhou
 *
 */
public class SSLClientSession {
	private static final Logger logger = LoggerFactory.getLogger(SSLClientSession.class);
	
	public static void main(String[] args) throws IOException {
		// define a trust store that contains acceptable certificates.
		System.setProperty("javax.net.ssl.trustStore", "src/main/resources/trust.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeme");
		
		SSLSocket s = (SSLSocket) SSLSocketFactory.getDefault().createSocket("localhost", 5432);
		SSLSession session = s.getSession();
		
		Certificate[] certificates = session.getPeerCertificates();
		if( null == certificates ) {
			logger.error("no certificates transimitted in the session handshake");
			System.exit(-1);
		}
		
		for( Certificate cert : certificates ) {
			logger.info("Certificate used in session handshake is {}", ((X509Certificate) cert).getSubjectDN());
		}
		
		logger.info("Peer host: {}", session.getPeerHost());
		logger.info("Cipher is: {}", session.getCipherSuite());
		logger.info("Protocol is {}", session.getProtocol());
		logger.info("Session ID is {}", new BigInteger(session.getId()));
		logger.info("Session is created at: {}", session.getCreationTime());
		logger.info("The last access: {}", session.getLastAccessedTime());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		logger.info("Get a message: {}", br.readLine());
		br.close();
		s.close();
	}
}

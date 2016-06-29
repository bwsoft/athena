package com.bwsoft.demo.storelocator;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StoreLocatorQuery {

	private String dsName;
	
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public String getGoogleAPIKey() throws Exception {
		Context ctx = new InitialContext();
		Context envCtx = (Context) ctx.lookup("java:comp/env");
		
		DataSource ds = (DataSource) envCtx.lookup(dsName);		
		try ( Connection conn = ds.getConnection() )
		{
			try( PreparedStatement stat = conn.prepareStatement("select value from google_resources where name='APIKey'") ) {
				try ( ResultSet rs = stat.executeQuery() ) {
					if( rs.next() ) {
						return rs.getString(1);
					} else {
						throw new Exception("Missing google resource - APIKey");
					}
				}
			}
		}		
	}
	
	public String queryStores(float lat, float lng, float radius) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.newDocument();
		Element root = document.createElement("markers");
		Node rootNode = document.appendChild(root);
		
		Context ctx = new InitialContext();
		Context envCtx = (Context) ctx.lookup("java:comp/env");
		
		DataSource ds = (DataSource) envCtx.lookup(dsName);		
		try ( Connection conn = ds.getConnection() )
		{
			// it is critical to close the statement
			try (PreparedStatement st = conn.prepareStatement("SELECT address, name, lat, lng, ( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(?) ) + sin( radians(?) ) * sin( radians( lat ) ) ) ) AS distance FROM markers HAVING distance < ? ORDER BY distance LIMIT 0 , 20"))
			{
				st.setFloat(1, lat);
				st.setFloat(2, lng);
				st.setFloat(3, lat);
				st.setFloat(4, radius);

				// it is critical to close the result set
				try ( ResultSet set = st.executeQuery() ) 
				{
					while( set.next() ) {
						Element elm = document.createElement("marker");
						elm.setAttribute("name", set.getString(1));
						elm.setAttribute("address", set.getString(2));
						elm.setAttribute("lat", set.getString(3));
						elm.setAttribute("lng", set.getString(4));
						elm.setAttribute("distance", set.getString(5));
						rootNode.appendChild(elm);
					}
				}
			}
		}
		return domToString(document);
	}
	
	private static String domToString(Document newDoc) throws Exception{
		TransformerFactory tranFactory = TransformerFactory.newInstance();
		Transformer aTransformer = tranFactory.newTransformer();
		Source src = new DOMSource(newDoc);
		StringWriter writer = new StringWriter();
		Result dest = new StreamResult(writer);
		aTransformer.transform(src, dest);
		return writer.toString();
	}
}

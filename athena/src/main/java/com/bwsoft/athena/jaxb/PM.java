package com.bwsoft.athena.jaxb;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.bwsoft.athena.jaxb.pm.MappingEntryType;
import com.bwsoft.athena.jaxb.pm.ParticipantMappingType;

/**
 * XML parser based upon pm.xsd. 
 * 
 * pm.xsd is converted into a java package using 
 *    xjc -p com.bwsoft.athena.jaxb.pm pm.xsd -d tempdir
 *    
 * @author yzhou
 *
 */
public class PM {
	public static void main(String[] args) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("com.bwsoft.athena.jaxb.pm");
		Unmarshaller unmarshaller = jc.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<ParticipantMappingType> o = (JAXBElement<ParticipantMappingType>) unmarshaller.unmarshal(new File("src/main/resources/remote_pm.xml"));
		
		System.out.println(o.getClass().getName()+" "+o.getName()+" "+o.getValue().getFromExchange());
		ParticipantMappingType pm = o.getValue();
		List<MappingEntryType> mes = pm.getMappingEntry();
		
		for( MappingEntryType me : mes ) {
			System.out.println(me.getILinkSenderCompID());
		}
	}
}

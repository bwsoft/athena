//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.25 at 06:50:24 PM CDT 
//


package com.bwsoft.athena.jaxb.pm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bwsoft.athena.jaxb.pm package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ParticipantMapping_QNAME = new QName("", "ParticipantMapping");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bwsoft.athena.jaxb.pm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ParticipantMappingType }
     * 
     */
    public ParticipantMappingType createParticipantMappingType() {
        return new ParticipantMappingType();
    }

    /**
     * Create an instance of {@link MappingEntryType }
     * 
     */
    public MappingEntryType createMappingEntryType() {
        return new MappingEntryType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParticipantMappingType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ParticipantMapping")
    public JAXBElement<ParticipantMappingType> createParticipantMapping(ParticipantMappingType value) {
        return new JAXBElement<ParticipantMappingType>(_ParticipantMapping_QNAME, ParticipantMappingType.class, null, value);
    }

}

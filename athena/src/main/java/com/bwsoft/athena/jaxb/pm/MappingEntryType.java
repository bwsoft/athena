//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.25 at 06:50:24 PM CDT 
//


package com.bwsoft.athena.jaxb.pm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MappingEntryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MappingEntryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="iLinkSenderCompID" use="required" type="{}NonZeroLengthString" />
 *       &lt;attribute name="CustomerID" use="required" type="{}NonZeroLengthString" />
 *       &lt;attribute name="MemberID" use="required" type="{}NonZeroLengthString" />
 *       &lt;attribute name="BranchID" use="required" type="{}NonZeroLengthString" />
 *       &lt;attribute name="TellerID" use="required" type="{}NonZeroLengthString" />
 *       &lt;attribute name="ConnectionID" use="required" type="{}NonZeroLengthString" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MappingEntryType")
public class MappingEntryType {

    @XmlAttribute(name = "iLinkSenderCompID", required = true)
    protected String iLinkSenderCompID;
    @XmlAttribute(name = "CustomerID", required = true)
    protected String customerID;
    @XmlAttribute(name = "MemberID", required = true)
    protected String memberID;
    @XmlAttribute(name = "BranchID", required = true)
    protected String branchID;
    @XmlAttribute(name = "TellerID", required = true)
    protected String tellerID;
    @XmlAttribute(name = "ConnectionID", required = true)
    protected String connectionID;

    /**
     * Gets the value of the iLinkSenderCompID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getILinkSenderCompID() {
        return iLinkSenderCompID;
    }

    /**
     * Sets the value of the iLinkSenderCompID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setILinkSenderCompID(String value) {
        this.iLinkSenderCompID = value;
    }

    /**
     * Gets the value of the customerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * Sets the value of the customerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerID(String value) {
        this.customerID = value;
    }

    /**
     * Gets the value of the memberID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberID() {
        return memberID;
    }

    /**
     * Sets the value of the memberID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberID(String value) {
        this.memberID = value;
    }

    /**
     * Gets the value of the branchID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchID() {
        return branchID;
    }

    /**
     * Sets the value of the branchID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchID(String value) {
        this.branchID = value;
    }

    /**
     * Gets the value of the tellerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTellerID() {
        return tellerID;
    }

    /**
     * Sets the value of the tellerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTellerID(String value) {
        this.tellerID = value;
    }

    /**
     * Gets the value of the connectionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionID() {
        return connectionID;
    }

    /**
     * Sets the value of the connectionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionID(String value) {
        this.connectionID = value;
    }

}
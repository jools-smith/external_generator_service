
package com.flexnet.external.type;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FulfillmentSourceENC.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FulfillmentSourceENC">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ONLINE"/>
 *     &lt;enumeration value="APPLICATION"/>
 *     &lt;enumeration value="LEGACY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FulfillmentSourceENC")
@XmlEnum
public enum FulfillmentSourceENC {

    ONLINE,
    APPLICATION,
    LEGACY;

    public String value() {
        return name();
    }

    public static FulfillmentSourceENC fromValue(String v) {
        return valueOf(v);
    }

}

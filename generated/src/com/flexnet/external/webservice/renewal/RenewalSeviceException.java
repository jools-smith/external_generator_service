
package com.flexnet.external.webservice.renewal;

import javax.xml.ws.WebFault;
import com.flexnet.external.type.SvcException;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "licGeneratorException", targetNamespace = "urn:com.flexnet.external.type")
public class RenewalSeviceException
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private SvcException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public RenewalSeviceException(String message, SvcException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public RenewalSeviceException(String message, SvcException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.flexnet.external.type.SvcException
     */
    public SvcException getFaultInfo() {
        return faultInfo;
    }

}

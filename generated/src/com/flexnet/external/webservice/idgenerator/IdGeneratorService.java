
package com.flexnet.external.webservice.idgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "IdGeneratorService", targetNamespace = "urn:idgenerator.webservice.external.flexnet.com", wsdlLocation = "file:/D:/development/external_generator_service/schema/IdGeneratorService.wsdl")
public class IdGeneratorService
    extends Service
{

    private final static URL IDGENERATORSERVICE_WSDL_LOCATION;
    private final static WebServiceException IDGENERATORSERVICE_EXCEPTION;
    private final static QName IDGENERATORSERVICE_QNAME = new QName("urn:idgenerator.webservice.external.flexnet.com", "IdGeneratorService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/D:/development/external_generator_service/schema/IdGeneratorService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        IDGENERATORSERVICE_WSDL_LOCATION = url;
        IDGENERATORSERVICE_EXCEPTION = e;
    }

    public IdGeneratorService() {
        super(__getWsdlLocation(), IDGENERATORSERVICE_QNAME);
    }

    public IdGeneratorService(WebServiceFeature... features) {
        super(__getWsdlLocation(), IDGENERATORSERVICE_QNAME, features);
    }

    public IdGeneratorService(URL wsdlLocation) {
        super(wsdlLocation, IDGENERATORSERVICE_QNAME);
    }

    public IdGeneratorService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, IDGENERATORSERVICE_QNAME, features);
    }

    public IdGeneratorService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IdGeneratorService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * External Interface to generate Ids like entitlement, activation, fulfillment, web-reg-keys, maintenance-item-id 
     * 
     * @return
     *     returns IdGeneratorServiceInterface
     */
    @WebEndpoint(name = "IdGeneratorServicePort")
    public IdGeneratorServiceInterface getIdGeneratorServicePort() {
        return super.getPort(new QName("urn:idgenerator.webservice.external.flexnet.com", "IdGeneratorServicePort"), IdGeneratorServiceInterface.class);
    }

    /**
     * External Interface to generate Ids like entitlement, activation, fulfillment, web-reg-keys, maintenance-item-id 
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IdGeneratorServiceInterface
     */
    @WebEndpoint(name = "IdGeneratorServicePort")
    public IdGeneratorServiceInterface getIdGeneratorServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("urn:idgenerator.webservice.external.flexnet.com", "IdGeneratorServicePort"), IdGeneratorServiceInterface.class, features);
    }

    private static URL __getWsdlLocation() {
        if (IDGENERATORSERVICE_EXCEPTION!= null) {
            throw IDGENERATORSERVICE_EXCEPTION;
        }
        return IDGENERATORSERVICE_WSDL_LOCATION;
    }

}

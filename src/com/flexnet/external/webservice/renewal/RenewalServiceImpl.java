
package com.flexnet.external.webservice.renewal;

import com.flexnet.external.type.PingRequest;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.type.RenewableEntitlementLineItems;
import com.flexnet.external.type.RenewalResponse;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.ServiceBase;

import javax.jws.WebService;

@WebService(
        endpointInterface = "com.flexnet.external.webservice.renewal.RenewalServiceInterface",
        wsdlLocation = "WEB-INF/wsdl/schema/RenewalService.wsdl")

public class RenewalServiceImpl extends ServiceBase implements RenewalServiceInterface {

  @Override
  public PingResponse ping(final PingRequest payload) throws RenewalSeviceException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = token();

    try {
      return getImplementorFactory().getDefaultImplementor(RenewalServiceInterface.class).ping(payload);
    }
    catch (final Throwable t) {
      throw new RenewalSeviceException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {

      token.commit();
    }
  }
  
  @Override
  public RenewalResponse request(final RenewableEntitlementLineItems payload) throws RenewalSeviceException {
    super.logger.in();
    super.logger.yaml(Log.Level.trace, payload);
    final Token token = token();

    try {
      final String tech = super.getLicenseTechnology(payload);

      return getImplementorFactory().getImplementor(tech, RenewalServiceInterface.class).request(payload);
    }
    catch (final Throwable t) {
      throw new RenewalSeviceException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {

      token.commit();
    }
  }
  
}

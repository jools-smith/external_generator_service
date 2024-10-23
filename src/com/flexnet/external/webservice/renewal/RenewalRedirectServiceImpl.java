package com.flexnet.external.webservice.renewal;

import com.flexnet.external.type.PingRequest;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.type.RenewableEntitlementLineItems;
import com.flexnet.external.type.RenewalResponse;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.ServiceBase;

import javax.jws.WebService;

@WebService(
        endpointInterface = "com.flexnet.external.webservice.renewal.RenewalServiceInterface",
        wsdlLocation = "WEB-INF/wsdl/schema/RenewalService.wsdl")
public class RenewalRedirectServiceImpl extends ServiceBase implements RenewalServiceInterface {

  public RenewalRedirectServiceImpl() {

    super.logger.me(this);
  }
  
  @Override
  public PingResponse ping(final PingRequest payload) throws RenewalSeviceException {
    super.logger.in();
    final Token token = token();

    try {
      return super.factory.getDefaultImplementor(RenewalServiceInterface.class).ping(payload);
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
    final Token token = token();

    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, RenewalServiceInterface.class).request(payload);
    }
    catch (final Throwable t) {
      throw new RenewalSeviceException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {

      token.commit();
    }
  }
}

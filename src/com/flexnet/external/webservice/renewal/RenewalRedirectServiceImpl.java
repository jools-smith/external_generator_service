package com.flexnet.external.webservice.renewal;

import javax.jws.WebService;

import com.flexnet.external.type.PingRequest;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.type.RenewableEntitlementLineItems;
import com.flexnet.external.type.RenewalResponse;
import com.flexnet.external.webservice.Diagnostics.Token;
import com.flexnet.external.webservice.ServiceBase;

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
    final Executor<PingRequest, Ping, PingResponse> executor = super.createExecutor();
    try {
      return executor
              .execute(Endpoint.ping, payload)
              .decode(Ping.class)
              .encode(Ping.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new RenewalSeviceException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
  @Override
  public RenewalResponse request(final RenewableEntitlementLineItems payload) throws RenewalSeviceException {
    super.logger.in();
    final Token token = token();
    try {
      return execute(Void.class, Endpoint.RSI_request, payload).decode(RenewalResponse.class);
    }
    catch (final Throwable t) {
      throw new RenewalSeviceException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
}


package com.flexnet.external.webservice.renewal;

import com.flexnet.external.type.PingRequest;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.type.RenewableEntitlementLineItems;
import com.flexnet.external.type.RenewalResponse;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.ServiceBase;
import com.flexnet.external.webservice.remote.Dummy;
import com.flexnet.external.webservice.remote.Endpoint;
import com.flexnet.external.webservice.remote.Executor;
import com.flexnet.external.webservice.remote.Ping;

import javax.jws.WebService;

@WebService(
        endpointInterface = "com.flexnet.external.webservice.renewal.RenewalServiceInterface",
        wsdlLocation = "WEB-INF/wsdl/schema/RenewalService.wsdl")

public class RenewalServiceImpl extends ServiceBase implements RenewalServiceInterface {
  
  public RenewalServiceImpl() {
    super.logger.me(this);
  }
  
  @Override
  public PingResponse ping(final PingRequest payload) throws RenewalSeviceException {
    super.logger.in();
    final Token token = token();
    final Executor<PingRequest, Ping, PingResponse> executor = Executor.createExecutor();
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
      return Executor.execute(Dummy.class, Endpoint.RRI_request, payload).decode(RenewalResponse.class);
    }
    catch (final Throwable t) {
      throw new RenewalSeviceException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
}

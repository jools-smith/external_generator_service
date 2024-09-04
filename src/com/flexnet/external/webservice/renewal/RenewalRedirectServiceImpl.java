package com.flexnet.external.webservice.renewal;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.ServiceBase;
import com.flexnet.external.webservice.keygenerator.LicGeneratorException;
import com.flexnet.external.webservice.remote.UnknownReply;
import com.flexnet.external.webservice.remote.EndpointType;
import com.flexnet.external.webservice.remote.Executor;
import com.flexnet.external.webservice.remote.Ping;

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
    final Executor<PingRequest, Ping, PingResponse> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.ping, payload)
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
    final Executor<RenewableEntitlementLineItems, UnknownReply, RenewalResponse> executor = Executor.createExecutor();
    try {
      return executor
              .execute(EndpointType.RRI_request, payload)
              .decode(UnknownReply.class)
              .encode((v) -> new RenewalResponse())
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
}

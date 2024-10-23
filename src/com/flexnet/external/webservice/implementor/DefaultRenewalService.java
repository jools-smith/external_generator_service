package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.renewal.RenewalServiceInterface;
import com.flexnet.external.webservice.renewal.RenewalSeviceException;

public final class DefaultRenewalService extends ImplementorBase implements RenewalServiceInterface {
  private final static Log logger = Log.create(DefaultRenewalService.class);

  @Override
  public PingResponse ping(final PingRequest request) {
    logger.in();
    return super.ping(request);
  }

  @Override
  public RenewalResponse request(final RenewableEntitlementLineItems renewableEntitlementLineItems) throws RenewalSeviceException {
    logger.in();
    return new RenewalResponse() {
      {
        this.redirectUrl = DefaultRenewalService.super.serialize(renewableEntitlementLineItems);
        this.status = RenewalStatus.SUCCESS;
      }
    };
  }
}

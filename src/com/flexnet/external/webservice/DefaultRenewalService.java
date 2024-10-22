package com.flexnet.external.webservice;

import com.flexnet.external.type.*;
import com.flexnet.external.webservice.renewal.RenewalServiceInterface;
import com.flexnet.external.webservice.renewal.RenewalSeviceException;

public final class DefaultRenewalService extends ImplementorBase implements RenewalServiceInterface {

  @Override
  public PingResponse ping(final PingRequest request) {
    return super.ping(request);
  }

  @Override
  public RenewalResponse request(final RenewableEntitlementLineItems renewableEntitlementLineItems) throws RenewalSeviceException {
    return new RenewalResponse() {
      {
        this.redirectUrl = DefaultRenewalService.super.serialize(renewableEntitlementLineItems);
        this.status = RenewalStatus.SUCCESS;
      }
    };
  }
}

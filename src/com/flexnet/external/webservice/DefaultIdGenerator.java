package com.flexnet.external.webservice;

import com.flexnet.external.type.*;
import com.flexnet.external.webservice.idgenerator.IdGeneratorException;
import com.flexnet.external.webservice.idgenerator.IdGeneratorServiceInterface;

public final class DefaultIdGenerator extends ImplementorBase implements IdGeneratorServiceInterface {

  @Override
  public PingResponse ping(final PingRequest request) {
    return super.ping(request);
  }

  @Override
  public Id generateEntitlementID(final Entitlement entitlement) throws IdGeneratorException {
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateLineItemID(final EntitlementLineItem entitlementLineItem) throws IdGeneratorException {
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateWebRegKey(final BulkEntitlement bulkEntitlement) throws IdGeneratorException {
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateMaintenanceItemID(final MaintenanceItem maintenanceItem) throws IdGeneratorException {
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateFulfillmentID(final FulfillmentRecord fulfillmentRecord) throws IdGeneratorException {
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateConsolidatedLicenseID(final ConsolidatedLicenseRecord consolidatedLicenseRecord) throws IdGeneratorException {
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }
}

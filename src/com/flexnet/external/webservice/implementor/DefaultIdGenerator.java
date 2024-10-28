package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.idgenerator.IdGeneratorException;
import com.flexnet.external.webservice.idgenerator.IdGeneratorServiceInterface;

public final class DefaultIdGenerator extends ImplementorBase implements IdGeneratorServiceInterface {
  private final static Log logger = Log.create(DefaultIdGenerator.class);

  @Override
  public PingResponse ping(final PingRequest request) {
    logger.in();
    return super.ping(request);
  }

  @Override
  public String technologyName() {
    return ImplementorFactory.default_technology_name;
  }

  @Override
  public Id generateEntitlementID(final Entitlement entitlement) throws IdGeneratorException {
    logger.in();
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateLineItemID(final EntitlementLineItem entitlementLineItem) throws IdGeneratorException {
    logger.in();
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateWebRegKey(final BulkEntitlement bulkEntitlement) throws IdGeneratorException {
    logger.in();
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateMaintenanceItemID(final MaintenanceItem maintenanceItem) throws IdGeneratorException {
    logger.in();
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateFulfillmentID(final FulfillmentRecord fulfillmentRecord) throws IdGeneratorException {
    logger.in();
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }

  @Override
  public Id generateConsolidatedLicenseID(final ConsolidatedLicenseRecord consolidatedLicenseRecord) throws IdGeneratorException {
    logger.in();
    return new Id() {
      {
        this.id = DefaultIdGenerator.super.id();
      }
    };
  }
}

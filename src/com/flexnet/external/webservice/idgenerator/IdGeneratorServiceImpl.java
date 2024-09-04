package com.flexnet.external.webservice.idgenerator;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.ServiceBase;
import com.flexnet.external.webservice.remote.Endpoint;
import com.flexnet.external.webservice.remote.Executor;
import com.flexnet.external.webservice.remote.Identifier;
import com.flexnet.external.webservice.remote.Ping;

import javax.jws.WebService;

@WebService(
        endpointInterface = "com.flexnet.external.webservice.idgenerator.IdGeneratorServiceInterface",
        wsdlLocation = "WEB-INF/wsdl/schema/IdGeneratorService.wsdl"
)
public class IdGeneratorServiceImpl extends ServiceBase implements IdGeneratorServiceInterface {
  public IdGeneratorServiceImpl() {
    super.logger.me(this);
  }

  @Override
  public PingResponse ping(final PingRequest payload) throws IdGeneratorException {
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
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }

  @Override
  public Id generateEntitlementID(final Entitlement payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    final Executor<Entitlement, Identifier, Id> executor = Executor.createExecutor();
    try {
      return executor
              .execute(Endpoint.IGI_generateEntitlementID, payload)
              .decode(Identifier.class)
              .encode(Identifier.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
  @Override
  public Id generateLineItemID(final EntitlementLineItem payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    final Executor<EntitlementLineItem, Identifier, Id> executor = Executor.createExecutor();
    try {
      return executor
              .execute(Endpoint.IGI_generateLineItemID, payload)
              .decode(Identifier.class)
              .encode(Identifier.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
  @Override
  public Id generateWebRegKey(final BulkEntitlement payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    final Executor<BulkEntitlement, Identifier, Id> executor = Executor.createExecutor();
    try {
      return executor
              .execute(Endpoint.IGI_generateWebRegKey, payload)
              .decode(Identifier.class)
              .encode(Identifier.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
  @Override
  public Id generateMaintenanceItemID(final MaintenanceItem payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    final Executor<MaintenanceItem, Identifier, Id> executor = Executor.createExecutor();
    try {
      return executor
              .execute(Endpoint.IGI_generateMaintenanceItemID, payload)
              .decode(Identifier.class)
              .encode(Identifier.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
  @Override
  public Id generateFulfillmentID(final FulfillmentRecord payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    final Executor<FulfillmentRecord, Identifier, Id> executor = Executor.createExecutor();
    try {
      return executor
              .execute(Endpoint.IGI_generateFulfillmentID, payload)
              .decode(Identifier.class)
              .encode(Identifier.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
  @Override
  public Id generateConsolidatedLicenseID(final ConsolidatedLicenseRecord payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    final Executor<ConsolidatedLicenseRecord, Identifier, Id> executor = Executor.createExecutor();
    try {
      return executor
              .execute(Endpoint.IGI_generateConsolidatedLicenseID, payload)
              .decode(Identifier.class)
              .encode(Identifier.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
}

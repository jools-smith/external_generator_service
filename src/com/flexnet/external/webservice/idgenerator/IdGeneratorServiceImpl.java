package com.flexnet.external.webservice.idgenerator;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.DefaultIdGenerator;
import com.flexnet.external.webservice.ImplementorFactory;
import com.flexnet.external.webservice.ServiceBase;
import com.flexnet.external.webservice.renewal.RenewalServiceInterface;

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

    try {
      return super.factory.getImplementor("DEF", IdGeneratorServiceInterface.class).ping(payload);
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }

  @Override
  public Id generateEntitlementID(final Entitlement payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();

    try {
      return super.factory.getImplementor("DEF", IdGeneratorServiceInterface.class).generateEntitlementID(payload);
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {

      token.commit();
    }
  }
  
  @Override
  public Id generateLineItemID(final EntitlementLineItem payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, IdGeneratorServiceInterface.class).generateLineItemID(payload);
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public Id generateWebRegKey(final BulkEntitlement payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return super.factory.getImplementor("DEF", IdGeneratorServiceInterface.class).generateWebRegKey(payload);
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public Id generateMaintenanceItemID(final MaintenanceItem payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return super.factory.getImplementor("DEF", IdGeneratorServiceInterface.class).generateMaintenanceItemID(payload);
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public Id generateFulfillmentID(final FulfillmentRecord payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, IdGeneratorServiceInterface.class).generateFulfillmentID(payload);
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public Id generateConsolidatedLicenseID(final ConsolidatedLicenseRecord payload) throws IdGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      final String tech = super.getLicenseTechnology(payload);

      return super.factory.getImplementor(tech, IdGeneratorServiceInterface.class).generateConsolidatedLicenseID(payload);
    }
    catch (final Throwable t) {
      throw new IdGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
}

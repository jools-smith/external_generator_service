package com.flexnet.external.webservice.keygenerator;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.ServiceBase;
import com.flexnet.external.webservice.remote.*;

import javax.jws.WebService;

@WebService(
        endpointInterface = "com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface",
        wsdlLocation = "WEB-INF/wsdl/schema/LicenseGeneratorService.wsdl")
public class LicenseGeneratorServiceImpl extends ServiceBase implements LicenseGeneratorServiceInterface {
  
  public LicenseGeneratorServiceImpl() {

    super.logger.me(this);
  }
  
  @Override
  public PingResponse ping(final PingRequest payload) throws LicGeneratorException {
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
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
  @Override
  public Status validateProduct(final ProductRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return Executor.execute(Dummy.class, Endpoint.LGI_validateProduct, payload).decode(Status.class);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public Status validateLicenseModel(final LicenseModelRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return Executor.execute(Dummy.class, Endpoint.LGI_validateLicenseModel, payload).decode(Status.class);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public GeneratorResponse generateLicense(final GeneratorRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    final Executor<GeneratorRequest, License, GeneratorResponse> executor = Executor.createExecutor();
    try {
      return executor
              .execute(Endpoint.LGI_generateLicense, payload)
              .decode(License.class)
              .encode(License.encode)
              .value();
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      executor.commit();
      token.commit();
    }
  }
  
  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return Executor.execute(Dummy.class, Endpoint.LGI_consolidateFulfillments, payload).decode(ConsolidatedLicense.class);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public LicenseFileDefinitionMap generateLicenseFilenames(final GeneratorRequest payload)
      throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return Executor.execute(Dummy.class, Endpoint.LGI_generateLicenseFilenames, payload).decode(LicenseFileDefinitionMap.class);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public LicenseFileDefinitionMap generateConsolidatedLicenseFilenames(final ConsolidatedLicenseResquest payload)
      throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return Executor.execute(Dummy.class, Endpoint.LGI_generateConsolidatedLicenseFilenames, payload)
          .decode(LicenseFileDefinitionMap.class);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
  
  @Override
  public String generateCustomHostIdentifier(final HostIdRequest payload) throws LicGeneratorException {
    super.logger.in();
    final Token token = token();
    try {
      return Executor.execute(Dummy.class, Endpoint.LGI_generateCustomHostIdentifier, payload).decode(String.class);
    }
    catch (final Throwable t) {
      throw new LicGeneratorException(t.getMessage(), this.serviceException.apply(t));
    }
    finally {
      token.commit();
    }
  }
}

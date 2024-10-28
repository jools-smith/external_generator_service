package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.ServiceBase;
import com.flexnet.external.webservice.keygenerator.LicGeneratorException;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

@Deprecated
public final class LmxLicenseGenerator extends ImplementorBase implements LicenseGeneratorServiceInterface {
  private final static Log logger = Log.create(LmxLicenseGenerator.class);

  private <T> T except(final Class<T> type, final String message) {
    throw new RuntimeException(message);
  }

  @Override
  public String technologyName() {
    return "LMX";
  }

  @Override
  public PingResponse ping(final PingRequest request) {
    logger.in();
    return super.ping(request);
  }

  @Override
  public Status validateProduct(final ProductRequest product) {
    logger.in();
    throw new RuntimeException("not implemented");
  }

  @Override
  public Status validateLicenseModel(final LicenseModelRequest model) {
    logger.in();
    return except(Status.class, "not implemented");
  }

  @Override
  public GeneratorResponse generateLicense(final GeneratorRequest request) {
    logger.in();
    return except(GeneratorResponse.class, "not implemented");
  }

  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet fulfillmentRecordset) {
    logger.in();
    return except(ConsolidatedLicense.class, "not implemented");
  }

  @Override
  public LicenseFileDefinitionMap generateLicenseFilenames(final GeneratorRequest fileRec) {
    logger.in();
    return except(LicenseFileDefinitionMap.class, "not implemented");
  }

  @Override
  public LicenseFileDefinitionMap generateConsolidatedLicenseFilenames(final ConsolidatedLicenseResquest clRec) {
    logger.in();
    return except(LicenseFileDefinitionMap.class, "not implemented");
  }

  @Override
  public String generateCustomHostIdentifier(final HostIdRequest hostIdReq) {
    logger.in();
    return except(String.class, "not implemented");
  }
}

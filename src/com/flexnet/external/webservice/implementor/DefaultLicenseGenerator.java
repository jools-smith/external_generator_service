package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.keygenerator.LicGeneratorException;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

import java.util.ArrayList;

public final class DefaultLicenseGenerator extends ImplementorBase implements LicenseGeneratorServiceInterface {
  private final static Log logger = Log.create(DefaultLicenseGenerator.class);

  @Override
  public PingResponse ping(final PingRequest request) {
    logger.in();
    return super.ping(request);
  }

  @Override
  public Status validateProduct(final ProductRequest product) {
    logger.in();
    return new Status() {
      {
        this.message = DefaultLicenseGenerator.super.serialize(product);
        this.code = 0;
      }
    };
  }

  @Override
  public Status validateLicenseModel(final LicenseModelRequest model) throws LicGeneratorException {
    logger.in();
    return new Status() {
      {
        this.message = DefaultLicenseGenerator.super.serialize(model);
        this.code = 0;
      }
    };
  }

  @Override
  public GeneratorResponse generateLicense(final GeneratorRequest request) throws LicGeneratorException {
    logger.in();
    return new GeneratorResponse() {
      {
        this.complete = true;
        this.licenseFileName = "license";
        this.licenseText = DefaultLicenseGenerator.super.serialize(request);
        this.message = "TBD";
      }
    };
  }

  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet fulfillmentRecordset) throws LicGeneratorException {
    logger.in();
    return new ConsolidatedLicense() {
      {
        this.licenseFileName = "license";
        this.licenseText = DefaultLicenseGenerator.super.serialize(fulfillmentRecordset);
      }
    };
  }

  @Override
  public LicenseFileDefinitionMap generateLicenseFilenames(final GeneratorRequest fileRec) throws LicGeneratorException {
    logger.in();
    return new LicenseFileDefinitionMap() {
      {
        this.item = new ArrayList<>();
      }
    };
  }

  @Override
  public LicenseFileDefinitionMap generateConsolidatedLicenseFilenames(final ConsolidatedLicenseResquest clRec) throws LicGeneratorException {
    logger.in();
    return new LicenseFileDefinitionMap() {
      {
        this.item = new ArrayList<>();
      }
    };
  }

  @Override
  public String generateCustomHostIdentifier(final HostIdRequest hostIdReq) throws LicGeneratorException {
    logger.in();
    return DefaultLicenseGenerator.super.serialize(hostIdReq);
  }
}

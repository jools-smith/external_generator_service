package com.flexnet.external.webservice;

import com.flexnet.external.type.*;
import com.flexnet.external.webservice.keygenerator.LicGeneratorException;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

import java.util.ArrayList;

public final class DefaultLicenseGenerator extends ImplementorBase implements LicenseGeneratorServiceInterface {

  @Override
  public PingResponse ping(final PingRequest request) {
    return super.ping(request);
  }

  @Override
  public Status validateProduct(final ProductRequest product) {
    return new Status() {
      {
        this.message = DefaultLicenseGenerator.super.serialize(product);
        this.code = 0;
      }
    };
  }

  @Override
  public Status validateLicenseModel(final LicenseModelRequest model) throws LicGeneratorException {
    return new Status() {
      {
        this.message = DefaultLicenseGenerator.super.serialize(model);
        this.code = 0;
      }
    };
  }

  @Override
  public GeneratorResponse generateLicense(final GeneratorRequest request) throws LicGeneratorException {
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
    return new ConsolidatedLicense() {
      {
        this.licenseFileName = "license";
        this.licenseText = DefaultLicenseGenerator.super.serialize(fulfillmentRecordset);
      }
    };
  }

  @Override
  public LicenseFileDefinitionMap generateLicenseFilenames(final GeneratorRequest fileRec) throws LicGeneratorException {
    return new LicenseFileDefinitionMap() {
      {
        this.item = new ArrayList<>();
      }
    };
  }

  @Override
  public LicenseFileDefinitionMap generateConsolidatedLicenseFilenames(final ConsolidatedLicenseResquest clRec) throws LicGeneratorException {
    return new LicenseFileDefinitionMap() {
      {
        this.item = new ArrayList<>();
      }
    };
  }

  @Override
  public String generateCustomHostIdentifier(final HostIdRequest hostIdReq) throws LicGeneratorException {
    return DefaultLicenseGenerator.super.serialize(hostIdReq);
  }
}

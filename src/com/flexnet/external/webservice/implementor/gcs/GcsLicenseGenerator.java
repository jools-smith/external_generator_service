package com.flexnet.external.webservice.implementor.gcs;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.GeneratorImplementor;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.implementor.ImplementorBase;
import com.flexnet.external.webservice.keygenerator.LicGeneratorException;

import java.time.Instant;
import java.util.ArrayList;

@GeneratorImplementor(technology = "GCS")
public final class GcsLicenseGenerator extends ImplementorBase {
  private final static Log logger = Log.create(GcsLicenseGenerator.class);

  @Override
  public String technologyName() {
    return "GCS";
  }

  @Override
  public Status validateProduct(final ProductRequest product) {
    logger.in();
    return new Status() {
      {
        this.message = "not implemented";
        this.code = 0;
      }
    };
  }

  @Override
  public Status validateLicenseModel(final LicenseModelRequest model) throws LicGeneratorException {
    logger.in();
    return new Status() {
      {
        this.message = "not implemented";
        this.code = 0;
      }
    };
  }

  @Override
  public GeneratorResponse generateLicense(GeneratorRequest request) throws LicGeneratorException {

    return new GeneratorResponse() {
      {
        this.licenseFiles = makeLicenseFiles(
                request.getLicenseTechnology().getLicenseFileDefinitions(),
                "not implemented",
                null);
      }
    };
  }

  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet fulfillmentRecordset) throws LicGeneratorException {
    logger.in();
    return new ConsolidatedLicense() {
      {
        this.licenseFileName = "Certificate";
        this.licenseText = "not implemented";
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
    return "not implemented";
  }
}

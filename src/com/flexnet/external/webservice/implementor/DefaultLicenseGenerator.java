package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.ServiceBase;
import com.flexnet.external.webservice.keygenerator.LicGeneratorException;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

import java.util.*;

public final class DefaultLicenseGenerator extends ImplementorBase implements LicenseGeneratorServiceInterface {
  private final static Log logger = Log.create(DefaultLicenseGenerator.class);

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
  public Status validateProduct(final ProductRequest product) {
    logger.in();
    return new Status() {
      {
        this.message = Utils.safeSerialize(product);
        this.code = 0;
      }
    };
  }

  @Override
  public Status validateLicenseModel(final LicenseModelRequest model) throws LicGeneratorException {
    logger.in();
    return new Status() {
      {
        this.message = Utils.safeSerialize(model);
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

//        this.licenseText = Base64.getEncoder().encodeToString(Utils.safeSerialize(request).getBytes());
//        this.message = Base64.getEncoder().encodeToString(Utils.safeSerialize(ServiceBase.getDiagnostics().serialize()).getBytes());

        this.licenseText = "this is a license...";
        this.message = "Hello world...";

//        licenseFiles = new ArrayList<LicenseFileMapItem>() {
//          {
//            add(new LicenseFileMapItem() {
//              {
//                this.name = licenseFileName;
//                this.value = licenseText;
//              }
//            });
//          }
//        };
      }
    };
  }

  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet fulfillmentRecordset) throws LicGeneratorException {
    logger.in();
    return new ConsolidatedLicense() {
      {
        this.licenseFileName = "license";
        this.licenseText = Utils.safeSerialize(fulfillmentRecordset);
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
    return Utils.safeSerialize(hostIdReq);
  }
}

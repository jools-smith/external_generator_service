package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.keygenerator.LicGeneratorException;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class DefaultLicenseGenerator extends ImplementorBase implements LicenseGeneratorServiceInterface {
  private final static Log logger = Log.create(DefaultLicenseGenerator.class);

  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

  private String getLicenseStorageType(String name, LicenseTechnology technology) {
    if (technology != null) {
      List<LicenseFileDefinition> licDefs = technology.getLicenseFileDefinitions();
      if (licDefs != null) {
        for (LicenseFileDefinition item : licDefs) {
          if (item.getName().equals("name")) {
            return item.getLicenseStorageType().toString();
          }
        }
      }
    }
    return "TEXT";
  }

  private void printCustomLicenseAttribute(List<CustomAttributeDescriptor> attribute, StringBuffer result, String type) {
    result.append("\n\t " + type + " Custom License Attribute ");
    if (attribute != null) {
      for (CustomAttributeDescriptor attr : attribute) {
        result.append("\n\t\t" + attr.getName() + ": " + attr.getValue());
      }
    }
  }

  private void printCustomHost(CustomHostId custHost, StringBuffer result) {
    result.append("\n\t CustomHost");
    if (custHost != null) {
      result.append("\n\t\t HostId : " + custHost.getHostIdentifier());
      result.append("\n\t\t License Technology : " + custHost.getLicenseTechnologyName());
      result.append("\n\t\t Custom License Host Attributes");
      List<StringPair> attrVals = custHost.getHostAttributeValues();
      for (StringPair pair : attrVals) {
        result.append("\n\t\t\t " + pair.getKey() + " : " + pair.getValue());
      }

    }
  }

  private void printFulfillments(List<FulfillmentRecord> pFrs, StringBuffer result) {
    if (pFrs != null && pFrs.size() > 0) {
      result.append("\nParent Fulfillments\n");
      for (FulfillmentRecord fr : pFrs) {
        result.append("\nFulfillment Id : ").append(fr.getFulfillmentId());
        result.append("\n Returned Count : ").append(fr.getReturnedCount());
      }
    }
  }

  private void printOrg(OrganizationUnit org, StringBuffer result) {
    result.append("\n\t OrganizationUnit");
    if (org != null) {
      result.append("\n\t\t DisplayName : " + org.getDisplayName());
      printCustomAttribute(org.getCustomAttributes(), result, "OrganizationUnit");
    }
  }

  private void printCustomAttribute(List<CustomAttribute> attribute, StringBuffer result, String type) {
    result.append("\n\t " + type + " Attribute");
    if (attribute != null) {
      for (CustomAttribute attr : attribute) {
        result.append("\n\t\t " + attr.getName()).append(": ");
        if (attr.getAttributes() != null) {
          for (String value : attr.getAttributes()) {
            result.append(value + ",");
          }
        }
      }
    }
  }

  private void printUsers(List<OrgUnitUser> users, StringBuffer result, String type) {
    if (users != null && users.size() > 0) {
      result.append("\n\t " + type);
      for (OrgUnitUser user : users) {
        result.append("\n\t\t DisplayName : " + user.getDisplayName());
        result.append("\n\t\t FirstName   : " + user.getFirstName());
        result.append("\n\t\t LastName    : " + user.getLastName());
        printCustomAttribute(user.getCustomAttributes(), result, "User");
      }
    }
  }

  private void printProduct(List<Product> products, StringBuffer result) {
    if (products != null && products.size() > 0) {
      result.append("\nProduct");
      for (Product product : products) {
        result.append("\n\t Name    : " + product.getName());
        result.append("\n\t Version : " + product.getVersion());
        result.append("\n\t Quantity Per Copy : " + product.getQuantityPerCopy());
        if (product.isSuite()) {
          printSuiteProductInfo(product.getSuiteProductInfo(), result);
        }
        else {
          printCustomAttribute(product.getCustomAttributes(), result, "Product");
        }
        printFeature(product.getFeatures(), result);

      }
    }

  }

  private void printSuiteProductInfo(List<SuiteProductInfo> products, StringBuffer result) {
    if (products != null) {
      for (SuiteProductInfo product : products) {
        result.append("\n\t Suite Product");
        result.append("\n\t\t Name    : " + product.getName());
        result.append("\n\t\t Version : " + product.getVersion());
        result.append("\n\t\t Quantity : " + product.getQuantity());
        if (product.getPackageName() != null && product.getPackageName().equals("")) {
          result.append("\n\t\t Package Name : " + product.getPackageName());
          result.append("\n\t\t Package Version : " + product.getPackageVersion());
          result.append("\n\t\t Package Version Format : " + product.getPackageVersionFormat());
        }
        printCustomAttribute(product.getCustomAttributes(), result, "Suite Product");

      }
    }
  }

  private void printFeature(List<Feature> features, StringBuffer result) {
    result.append("\n\t Features");
    if (features != null) {
      for (Feature feature : features) {
        result.append("\n\t\t " + feature.getName() + " : " + feature.getCount() + ", version : " + feature.getVersion() + ", isDateBased : " + feature.isIsDateBasedVersion() + "\n\t");
        List<FeatureOverride> overrides = feature.getFeatureOverride();
        result.append("\n\t\t Featur Overrides");
        for (FeatureOverride fos : overrides) {
          result.append("\n\t\t\t " + fos.getName() + " : " + fos.getValue());
        }
      }
    }
  }

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
  public GeneratorResponse generateLicense(GeneratorRequest request) throws LicGeneratorException {

    return new GeneratorResponse() {
      {
        this.licenseFiles = makeLicenseFiles(
                request.getLicenseTechnology().getLicenseFileDefinitions(),
                Utils.safeSerializeYaml(request),
                null);

        setLicenseText("#Generated by external server at " + Instant.now().toString());

        setLicenseFileName("nofile.txt");
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

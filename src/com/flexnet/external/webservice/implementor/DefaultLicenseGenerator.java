package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.keygenerator.LicGeneratorException;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

import java.text.SimpleDateFormat;
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
    final GeneratorResponse response = new GeneratorResponse();


    //      OrganizationUnit ou = request.getSoldTo();
    //      List <OrgUnitUser> soldTousers = request.getSoldToUsers();
    //      OrgUnitUser loggedInUser = request.getLoggedInUser();
    //      List<Product> products = request.getEntitledProducts();
    //      LicenseModel model = request.getLicenseModel();
    //      LicenseGeneratorConfig config = request.getLicenseGeneratorConfiguration();
    //      LicenseTechnology licTech = request.getLicenseTechnology();
    //      CustomHostId custHost  = request.getCustomHost();
    //      List<FulfillmentRecord> pFrs = request.getParentFulfillments(); // Parent fulfillment
    //      PartNumber partNo = request.getPartNumber();
    //
    //      List<LicenseFileDefinition> lfds = licTech.getLicenseFileDefinitions();
    //      SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    //
    //      StringBuffer result = new StringBuffer("Fulfillment\n");
    //
    //      // Start from Fulfilment
    //      result.append("\t fulfilment Id: " + request.getFulfilementID());
    //      result.append("\n\t Count: " + request.getFulfillCount());
    //      result.append("\n\t OverDraft : " + request.getOverdraftCount());
    //      if (request.getStartDate() != null && request.getStartDate().toGregorianCalendar() != null )
    //        result.append("\n\t StartDate : " + sdf.format(request.getStartDate().toGregorianCalendar().getTime()));
    //      result.append("\n\t ShipToEmail: " + request.getShipToEmail());
    //      printCustomHost(custHost, result);
    //      printFulfillments(pFrs, result);
    //      result.append("\n\t RequestType : " + request.getRequestType());
    //      result.append("\n\t isVerifyRequest : " + request.isVerifyRequest());
    //      if (model.getFulfillmentTimeAttributes() != null)
    //        printCustomLicenseAttribute(model.getFulfillmentTimeAttributes().getAttributes(), result, "FULFILMENT");
    //
    //      // <!--========== Line item details =============== -->
    //      result.append("\nActivationRecord\n\t Activation ID: " + request.getActivationID());
    //      if (request.getVersionDate() != null && request.getVersionDate().toGregorianCalendar() != null)
    //        result.append("\n\t Version Date : " + sdf.format(request.getVersionDate().toGregorianCalendar().getTime()));
    //      if (request.getExpirationDate() != null && request.getExpirationDate().toGregorianCalendar() != null)
    //        result.append("\n\t ExpirationDate : " + sdf.format(request.getExpirationDate().toGregorianCalendar().getTime()));
    //      printCustomAttribute(request.getEntitlementLineItemCustomAttributes(), result, "LineItem");
    //      if (model.getEntitlementTimeAttributes() != null)
    //        printCustomLicenseAttribute(model.getEntitlementTimeAttributes().getAttributes(), result, "ENTITLEMENT");
    //
    //      // <!--========== Entitlement details =============== -->
    //
    //      result.append("\nEntitlement\n\t EntitlementId : " + request.getEntitlementID());
    //      result.append("\n\t OrderId : " + request.getOrderId());
    //      result.append("\n\t OrderLineNumber: " + request.getOrderLineNumber());
    //      printOrg(ou, result);
    //      printUsers(soldTousers, result, "SoldToUser");
    //      printCustomAttribute(request.getEntitlementCustomAttributes(), result, "Entitlement");
    //
    //
    //      // <!--========== Product details =============== -->
    //      printProduct(products, result);
    //      if (partNo != null)
    //        result.append("\n PartNumber: " + partNo.getPartNumberName());
    //
    //      // <!--========== License Model details =============== -->
    //      result.append("\nLicense\n\t Model Name: " + model.getName());
    //      if (model.getModelTimeAttributes() != null)
    //        printCustomLicenseAttribute(model.getModelTimeAttributes().getAttributes(), result, "MODEL");
    //
    //      // <!--========== License Generator Config details =============== -->
    //      if(config != null)  {
    //        result.append("\nLicense Generator Config\n\t Name: " + config.getName());
    //        if (config.getAttributes() != null )
    //          printCustomLicenseAttribute(config.getAttributes().getAttributes(), result, "Generator");
    //      }
    //
    //      // <!--========== License Technology details =============== -->
    //      result.append("\nLicense Technolgy \n\t Name: " + licTech.getName());
    //      if (lfds != null && lfds.size() > 0)  {
    //        for (LicenseFileDefinition lfd :lfds)  {
    //          result.append("\n\t License file defnition Name : " + lfd.getName());
    //          result.append("\n\t License file defnition Type : " + lfd.getLicenseStorageType().toString());
    //        }
    //      }
    //      result.append("\n\t LicenseFileType : " + request.getLicenseFileType());
    //
    //
    //      // <!--========== Misc. details =============== -->
    //      //request.getCurrentLicenseOnHost();
    //      result.append("\nLogged In User : ").append(loggedInUser.getDisplayName());
    //      printCustomAttribute(loggedInUser.getCustomAttributes(), result, "LoggedIn User");
    //
    //
    //      // TODO: populate the license files in the response as per the license file definitions
    //      //LicenseFileMap licFiles = new LicenseFileMap();

    //      List<LicenseFileDefinition> lfds = request.getLicenseTechnology().getLicenseFileDefinitions();

    List<LicenseFileMapItem> licFiles = new ArrayList<LicenseFileMapItem>();

    for (LicenseFileDefinition lfd : request.getLicenseTechnology().getLicenseFileDefinitions()) {

      switch (lfd.getLicenseStorageType()) {
        case TEXT:
          licFiles.add(new LicenseFileMapItem() {
            {
              this.name = lfd.getName();
              this.value = Utils.safeSerializeYaml(request);
            }
          });
          break;
        case BINARY:
          licFiles.add(new LicenseFileMapItem() {
            {
              this.name = lfd.getName();
              this.value = "this is a license!".getBytes();
            }
          });
          break;
        default:
          throw new RuntimeException("invalid license file type");
      }
    }
    response.setLicenseFiles(licFiles);

    response.setLicenseText("#Generated by external server at " + dateFormat.format(new Date()));
    response.setLicenseFileName("nofile.txt");

    System.out.println("Processed generateLicense at " + dateFormat.format(new Date()));

    return response;
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

  //  @Override
  public GeneratorResponse generateLicense_xxx(final GeneratorRequest request) throws LicGeneratorException {
    logger.in();
    return new GeneratorResponse() {
      {
        //this.complete = true;
        this.licenseFileName = "license";

        //        this.licenseText = Base64.getEncoder().encodeToString(Utils.safeSerialize(request).getBytes());
        //        this.message = Base64.getEncoder().encodeToString(Utils.safeSerialize(ServiceBase.getDiagnostics().serialize()).getBytes());

        this.licenseText = "this is a license...";
        this.message = "true";

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
}

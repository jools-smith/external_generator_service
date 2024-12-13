package com.flexnet.external.webservice.implementor.lmx;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.implementor.ImplementorBase;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public final class LmxLicenseGenerator extends ImplementorBase implements LicenseGeneratorServiceInterface {

  private final static Log logger = Log.create(LmxLicenseGenerator.class);

//  static String gregorianCalendarToYYYYMMDD(final XMLGregorianCalendar calendar) {
//    return Utils.yyyy_mm_dd.format(Utils.gregorianCalendarToDate(calendar));
//  }

  static Optional<Map<String, String>> get_attributes(final List<CustomAttribute> attributes) {
    logger.in();
    if (attributes == null) {
      return Optional.empty();
    }
    logger.log(Log.Level.info, Utils.safeSerializeYaml(attributes));

    return Optional.of(attributes.stream().collect(Collectors.toMap(CustomAttribute::getName, att -> String.join(" | ", att.getAttributes()))));
  }

  static Optional<Map<String, String>> from_product_category_attribute_list(final List<ProductCategoryAttributeValue> list) {
//    logger.in();
    if (list == null) {
      return Optional.empty();
    }

    logger.log(Log.Level.info, Utils.safeSerializeYaml(list));

    return Optional.of(list.stream().collect(Collectors.toMap(ProductCategoryAttributeValue::getProductCategoryName, ProductCategoryAttributeValue::getValue)));
  }

  static Optional<Map<String, String>> from_attribute_list(final List<CustomAttribute> list) {
//    logger.in();
    if (list == null) {
      return Optional.empty();
    }

    logger.log(Log.Level.info, Utils.safeSerializeYaml(list));

    return Optional.of(list.stream().collect(Collectors.toMap(CustomAttribute::getName, att -> String.join("|", att.getAttributes()))));
  }

  // EXCEPTION
  private <T> T except(final Class<T> type, final String message) {
    throw new RuntimeException(message);
  }

  @Override
  public PingResponse ping(final PingRequest request) {
    logger.in();
    return super.ping(request);
  }

  @Override
  public String technologyName() {
    return "LMX";
  }

  @Override
  public Status validateProduct(final ProductRequest product) {
    return new Status() {
      {
        this.code = 0;
        this.message = String.format("product %s:%s is valid", product.getName(), product.getVersion());
      }
    };
  }

  @Override
  public Status validateLicenseModel(final LicenseModelRequest model) {
    return new Status() {
      {
        this.code = 0;
        this.message = String.format("license model %s is valid", model.getName());
      }
    };
  }

  @Override
  public GeneratorResponse generateLicense(final GeneratorRequest request) {
    final LicenseBuilder licenseBuilder = new LicenseBuilder();

    licenseBuilder
            .setAccount(request.getSoldTo().getDisplayName())
            .setLicense(request.getActivationID())
            .setCreatedBy(request.getLoggedInUser().getDisplayName())
            .setGeneratedOn(Instant.now().toString())
            .setCreationDate(LocalDate.now().toString());

    Optional.ofNullable(request.getCustomHost()).flatMap(host -> host.getHostAttributeValues().stream().findFirst()).ifPresent(att -> {
      licenseBuilder.setType(att.getValue());
    });

    Optional.ofNullable(request.getSoldToUsers()).flatMap(users -> users.stream().findFirst()).ifPresent(user -> {
      licenseBuilder.setCustomer(user.getDisplayName());
    });

    final Map<String, String> attributes = new HashMap<>();

    fromAttributeSet(request.getLicenseModel().getEntitlementTimeAttributes()).ifPresent(attributes::putAll);
    fromAttributeSet(request.getLicenseModel().getFulfillmentTimeAttributes()).ifPresent(attributes::putAll);
    fromAttributeSet(request.getLicenseModel().getModelTimeAttributes()).ifPresent(attributes::putAll);
    //TODO:DEBUG
//    logger.log(Log.Level.debug, "attributes: " + attributes);

    final String startDate = Utils.yyyy_mm_dd.format(Utils.gregorianCalendarToDate(request.getStartDate()));
    //TODO:DEBUG
//    logger.log(Log.Level.debug, "startDate: " + startDate);

    final String endDate = Utils.yyyy_mm_dd.format(Utils.gregorianCalendarToDate(request.getExpirationDate()));
    //TODO:DEBUG
//    logger.log(Log.Level.debug, "endDate: " + endDate);

    //TODO:DEBUG
//    logger.log(Log.Level.debug, "processing products");
    request.getEntitledProducts().forEach(prod -> {
      //TODO:DEBUG
//      logger.log(Log.Level.debug, "product: " + prod.getName());

      //quantity per copy
      final int multiplier = prod.getQuantityPerCopy();
      //TODO:DEBUG
//      logger.log(Log.Level.debug, "multiplier: " + multiplier);

      prod.getFeatures().forEach(feature -> {
        //TODO:DEBUG
//        logger.log(Log.Level.debug, "feature: " + feature.getName());

        final FeatureBuilder featureBuilder = licenseBuilder.createFeatureBuilder();

        attributes.forEach(featureBuilder::withMetadataAsString);

        featureBuilder.withFeatureName(feature.getName());
        featureBuilder.withFeatureVersion(feature.getVersion());
        featureBuilder.withFeatureCount(feature.getCount() * multiplier);
        featureBuilder.withStartDate(startDate);
        featureBuilder.withEndDate(endDate);
        featureBuilder.seal();
      });
    });

    return new GeneratorResponse() {
      {
        this.licenseFiles = makeLicenseFiles(
                request.getLicenseTechnology().getLicenseFileDefinitions(),
                licenseBuilder.generate(),
                null);

        setLicenseText("#Generated by external server at " + Instant.now().toString());

        setLicenseFileName("nofile.txt");
      }
    };
  }

  static Optional<Map<String, String>> fromAttributeSet(final AttributeSet set) {
//    logger.in();

    try {
      if (set == null) {
        return Optional.empty();
      }

      return Optional.of(set.getAttributes().stream().filter(att -> att.getValue() != null).collect(Collectors.toMap(CustomAttributeDescriptor::getName, att -> att.getValue().replace(',', '|'))));
    }
    finally {
//      logger.out();
    }
  }



  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet fulfillmentRecordset) {

    final String license = fulfillmentRecordset.getFulfillments().stream().flatMap(fulfilment -> fulfilment.getLicenseFiles().stream()).filter(lfd -> String.class.isAssignableFrom(lfd.getValue().getClass())).map(lfd -> lfd.getValue().toString()).collect(Collectors.joining("\n"));

    return new ConsolidatedLicense() {
      {
        this.fulfillments = fulfillmentRecordset.getFulfillments();

        fulfillmentRecordset.getFulfillments().stream().findAny().ifPresent(fid -> {
          this.licFiles = makeLicenseFiles(fid.getLicenseTechnology().getLicenseFileDefinitions(), license, null);
        });
      }
    };

  }

  @Override
  public LicenseFileDefinitionMap generateLicenseFilenames(final GeneratorRequest fileRec) {
    return except(LicenseFileDefinitionMap.class, "generateLicenseFilenames not implemented");
  }

  @Override
  public LicenseFileDefinitionMap generateConsolidatedLicenseFilenames(final ConsolidatedLicenseResquest clRec) {
    return except(LicenseFileDefinitionMap.class, "generateConsolidatedLicenseFilenames not implemented");
  }

  @Override
  public String generateCustomHostIdentifier(final HostIdRequest hostIdReq) {
    return except(String.class, "generateCustomHostIdentifier not implemented");
  }

}

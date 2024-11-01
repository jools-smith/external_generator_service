package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.XMLGregorianCalendar;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LmxLicenseGenerator extends ImplementorBase implements LicenseGeneratorServiceInterface {
  static final SimpleDateFormat yyyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
  private final static Log logger = Log.create(LmxLicenseGenerator.class);

  static String gregorian_calendar_to_yyymmdd(final XMLGregorianCalendar calendar) {
    logger.in();
    if (calendar == null) {
      return null;
    }
    else {
      return yyyyymmdd.format(calendar.toGregorianCalendar().getTime());
    }
  }

  static Optional<Map<String, String>> get_attributes(final List<CustomAttribute> attributes) {
    logger.in();
    if (attributes == null) {
      return Optional.empty();
    }
    logger.log(Log.Level.info, Utils.safeSerializeYaml(attributes));

    return Optional.of(attributes.stream().collect(Collectors.toMap(CustomAttribute::getName, att -> String.join(" | ", att.getAttributes()))));
  }

  static Optional<Map<String, String>> from_product_category_attribute_list(final List<ProductCategoryAttributeValue> list) {
    logger.in();
    if (list == null) {
      return Optional.empty();
    }

    logger.log(Log.Level.info, Utils.safeSerializeYaml(list));

    return Optional.of(list.stream().collect(Collectors.toMap(ProductCategoryAttributeValue::getProductCategoryName, ProductCategoryAttributeValue::getValue)));
  }

  static Optional<Map<String, String>> from_attribute_list(final List<CustomAttribute> list) {
    logger.in();
    if (list == null) {
      return Optional.empty();
    }

    logger.log(Log.Level.info, Utils.safeSerializeYaml(list));

    return Optional.of(list.stream().collect(Collectors.toMap(CustomAttribute::getName, att -> String.join("|", att.getAttributes()))));
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
    logger.in();
    return new Status() {
      {
        this.code = 0;
        this.message = String.format("product %s:%s is valid", product.getName(), product.getVersion());
      }
    };
  }

  @Override
  public Status validateLicenseModel(final LicenseModelRequest model) {
    logger.in();
    return new Status() {
      {
        this.code = 0;
        this.message = String.format("license model %s is valid", model.getName());
      }
    };
  }

  @Override
  public GeneratorResponse generateLicense(final GeneratorRequest request) {
    logger.in();

    logger.log(Log.Level.info, "here we go...");

    final Map<String, String> attributes = new HashMap<>();
    logger.log(Log.Level.info, "attributes: " + attributes);

    fromAttributeSet(request.getLicenseModel().getEntitlementTimeAttributes()).ifPresent(attributes::putAll);
    logger.log(Log.Level.info, "attributes: " + attributes);

    fromAttributeSet(request.getLicenseModel().getFulfillmentTimeAttributes()).ifPresent(attributes::putAll);
    logger.log(Log.Level.info, "attributes: " + attributes);

    fromAttributeSet(request.getLicenseModel().getModelTimeAttributes()).ifPresent(attributes::putAll);
    logger.log(Log.Level.info, "attributes: " + attributes);

    final String startDate = gregorian_calendar_to_yyymmdd(request.getStartDate());
    logger.log(Log.Level.info, "startDate: " + startDate);

    final String endDate = gregorian_calendar_to_yyymmdd(request.getExpirationDate());
    logger.log(Log.Level.info, "endDate: " + endDate);

    final List<LicenseBuilder> licenses = new ArrayList<>();

    logger.log(Log.Level.info, "processing products");
    request.getEntitledProducts().forEach(prod -> {
      logger.log(Log.Level.info, "product: " + prod.getName());

      //quantity per copy
      final int multiplier = prod.getQuantityPerCopy();
      logger.log(Log.Level.info, "multiplier: " + multiplier);

      prod.getFeatures().forEach(feature -> {
        logger.log(Log.Level.info, "feature: " + feature.getName());

        licenses.add(new LicenseBuilder() {
          {
            attributes.forEach(this::withMetadata);

            this.withFeatureName(feature.getName())
                    .withFeatureVersion(feature.getVersion())
                    .withFeatureCount(feature.getCount() * multiplier)
                    .withStartDate(startDate)
                    .withEndDate(endDate)
                    .seal();
          }
        });
      });
    });

    return new GeneratorResponse() {
      {
        this.licenseFiles = makeLicenseFiles(
                request.getLicenseTechnology().getLicenseFileDefinitions(),
                licenses.stream().map(LicenseBuilder::build).collect(Collectors.joining("\n\n")),
                null);

        setLicenseText("#Generated by external server at " + Instant.now().toString());

        setLicenseFileName("nofile.txt");
      }
    };
  }

  static Optional<Map<String, String>> fromAttributeSet(final AttributeSet set) {
    logger.in();

    try {
      if (set == null) {
        return Optional.empty();
      }

//      logger.log(Log.Level.info, Utils.safeSerializeYaml(set));

      return Optional.of(set.getAttributes().stream()
              .filter(att -> att.getValue() != null)
              .collect(Collectors.toMap(CustomAttributeDescriptor::getName, att -> att.getValue().replace(',','|'))));
    }
    finally {
      logger.out();
    }
  }

  static List<LicenseFileMapItem> makeLicenseFiles(final List<LicenseFileDefinition> files, final String text, final byte[] bytes) {
    return new ArrayList<LicenseFileMapItem>() {
      {
        files.forEach(lfd -> {
          switch (lfd.getLicenseStorageType()) {
            case TEXT:
              Optional.ofNullable(text).ifPresent(license -> {
                this.add(new LicenseFileMapItem() {
                  {
                    this.name = lfd.getName();
                    this.value = license;
                  }
                });
              });
              break;
            case BINARY:
              Optional.ofNullable(bytes).ifPresent(license -> {
                this.add(new LicenseFileMapItem() {
                  {
                    this.name = lfd.getName();
                    this.value = license;
                  }
                });
              });
              break;
            default:
              throw new RuntimeException("invalid license file type");
          }
        });
      }
    };
  }

  @Override
  public ConsolidatedLicense consolidateFulfillments(final FulfillmentRecordSet fulfillmentRecordset) {

    final AtomicReference<String> separator = new AtomicReference<>("default");

    final String license = fulfillmentRecordset.getFulfillments()
            .stream()
            .filter(fulfilment -> separator.getAndSet(fulfilment.getFulfillmentId()) != null)
            .flatMap(fulfilment -> fulfilment.getLicenseFiles().stream())
            .filter(lfd -> String.class.isAssignableFrom(lfd.getValue().getClass()))
            .map(lfd -> lfd.getValue().toString())
            .collect(Collectors.joining(String.format("\n### %s ###\n\n", separator.get())));

    return new ConsolidatedLicense() {
      {
        this.fulfillments = fulfillmentRecordset.getFulfillments();

        fulfillmentRecordset.getFulfillments().stream().findAny().ifPresent(fid -> {
          this.licFiles = makeLicenseFiles(fid.getLicenseTechnology().getLicenseFileDefinitions(), license, null);
        });
      }
    };

  }

  private <T> T except(final Class<T> type, final String message) {
    throw new RuntimeException(message);
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

  static class LicenseBuilder {
    private static final String KEYTYPE = "KEYTYPE";
    private static final String COMMENT = "COMMENT";
    private static final String SN = "SN";
    private static final String KEY = "KEY";

    private final static String lf = System.lineSeparator();
    private final static String lbrace = "{";
    private final static String rbrace = "}";

    private final Map<String, String> metadata = new HashMap<>();

    private String name;
    private String version = null;
    private String start = null;
    private String end = null;
    private Long count = null;

    LicenseBuilder() {

    }

    public void seal() {

      try {
        this.metadata.remove(KEY);

        final String key = DatatypeConverter.printHexBinary(MessageDigest
                .getInstance("MD5")
                .digest(build().getBytes(Charset.defaultCharset())));


        this.metadata.put(KEY, key);
      }
      catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }

    }
    public LicenseBuilder withFeatureName(final String value) {
      this.name = value;
      return this;
    }

    public LicenseBuilder withFeatureVersion(final String value) {
      this.version = value;
      return this;
    }

    public LicenseBuilder withStartDate(final String value) {
      this.start = value;
      return this;
    }

    public LicenseBuilder withEndDate(final String value) {
      this.end = value;
      return this;
    }

    public LicenseBuilder withFeatureCount(final long value) {
      this.count = value;
      return this;
    }

    public LicenseBuilder withMetadata(final String key, final String value) {
      this.metadata.put(key, value);
      return this;
    }

    public String build() {
      final StringBuilder bfr = new StringBuilder();

      final Function<String, Void> add_metadata = (key) -> {
        if (this.metadata.containsKey(key)) {
          bfr.append(String.format("%s=%s ", key, this.metadata.get(key)));
        }
        return null;
      };

      bfr.append(String.format("FEATURE %s", this.name)).append(lf);
      bfr.append(lbrace).append(lf);
      bfr.append("VENDOR=HBMUK ");

      if (count != null) {
        bfr.append(String.format("COUNT=%s ", count));
      }
      else {
        bfr.append("COUNT=UNLIMITED ");
      }

      add_metadata.apply(KEYTYPE);

      if (this.version != null) {
        bfr.append(String.format("VERSION=%s ", version));
      }

      if (this.start != null) {
        bfr.append(String.format("START=%s ", start));
      }

      if (this.end != null) {
        bfr.append(String.format("END=%s ", end));
      }

      // new line
      bfr.append(lf);

      add_metadata.apply(COMMENT);

      add_metadata.apply(SN);

      // new line
      bfr.append(lf);

      add_metadata.apply(KEY);

      bfr.append(lf).append(rbrace).append(lf);

      return bfr.toString();
    }
  }
}

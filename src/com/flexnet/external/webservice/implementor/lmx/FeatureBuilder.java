package com.flexnet.external.webservice.implementor.lmx;

import com.flexnet.external.utils.Log;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

class FeatureBuilder {
  private final static Log logger = Log.create(FeatureBuilder.class);

  // constants
  private final static String lf = System.lineSeparator();
  private final static String lbrace = "{";
  private final static String rbrace = "}";
  private final static String vendor = "HBMUK";
  private final static String unlimited = "UNLIMITED";

  /**
   VERSION = (Version number of the feature.)
   LICENSEE = (User or company to whom the license was issued.)
   START = (Date on which the feature becomes valid.)
   END = (Date on which the feature expires.)
   MAINTENANCE_START = (Date the license maintenance plan begins.)
   MAINTENANCE_END = (Date the license maintenance plan expires.)
   ISSUED = (Date the license was created.)
   SN = (Custom serial number for the license.)
   DATA = (Additional information regarding the license.)
   COMMENT = (Additional information regarding the license.)
   OPTIONS = (Additional licensing options.)
   PLATFORMS = (Platform(s) to which usage is restricted.)
   COUNT = (Number of network licenses that can be issued simultaneously.)
   TOKEN_DEPENDENCY = (Reference to a real license upon which a token license depends.)
   SOFTLIMIT = (Number of "overdraft" licenses.)
   HAL_SERVERS = 3 (Enables redundant servers, or High Availability Servers.)
   BORROW = (Number of hours a borrowed license may be used.)
   GRACE = (Number of hours a grace license may be used.)
   HOLD = (Number of minutes licenses are held before being checked in.)
   USERBASED = (Number of licenses reserved for named users.)
   HOSTBASED = (Number of licenses reserved for named hosts.)
   TIME_ZONES = -12 to 13 (Allowed time zones, relative to GTM.)
   SHARE = HOST|USER|CUSTOM or TERMINALSERVER and/or VIRTUAL (Type of license sharing in use.)
   SYSTEMCLOCKCHECK = TRUE|FALSE (Enables/disables the system clock check.)
   HOSTID_MATCH_RATE = (Percentage of hostids that must match for successful hostid verification.)
   */
  // attribute values
  public enum Attributes {
    BORROW,
    COMMENT,
    COUNT,
    DATA,
    END,
    FEATURE,
    GRACE,
    HAL_SERVERS,
    HOLD,
    HOSTBASED,
    HOSTID_MATCH_RATE,
    ISSUED,
    KEYTYPE,
    LICENSEE,
    MAINTENANCE_END,
    MAINTENANCE_START,
    OPTIONS,
    PLATFORMS,
    SN,
    KEY,
    SOFTLIMIT,
    START,
    SYSTEMCLOCKCHECK,
    TIME_ZONES,
    TOKEN_DEPENDENCY,
    USERBASED,
    VENDOR,
    VERSION
  }

  static class Header {
    private String type;
    private String account;
    private String customer;
    private String createdBy;
    private String creationDate;
    private String country;

    private String license;

    public Header setType(final String value) {
      this.type = value;
      return this;
    }
    public Header setAccount(final String value) {
      this.account = value;
      return this;
    }
    public Header setCustomer(final String value) {
      this.customer = value;
      return this;
    }
    public Header setCreatedBy(final String value) {
      this.createdBy = value;
      return this;
    }
    public Header setCreationDate(final String value) {
      this.creationDate = value;
      return this;
    }
    public Header setActivationId(final String value) {
      this.license = value;
      return this;
    }
    public Header setCountry(final String value) {
      this.country = value;
      return this;
    }

    public String generate() {

      final String bfr = String.format("# Type:%s", this.type) + lf + String.format("# Account:%s", this.account) + lf + String.format("# Creation Date:%s", this.creationDate) + lf + String.format("# Customer:%s", this.customer) + lf + String.format("# License Created:%s", this.createdBy) + lf + String.format("# License:%s", this.license) + lf + String.format("# Country:%s", this.country) + lf + String.format("# Generated on:%s", this.type) + lf;

      return bfr;
    }
  }

  private final Header header = new Header();

  // class properties
  private String name;
  private String version = null;
  private String start = null;
  private String end = null;
  private Long count = null;
  private final Map<Attributes, String> metadata = new HashMap<>();

  // keep it in the family
  FeatureBuilder() {

  }

  public void seal() {
    logger.in();
    try {
      this.metadata.remove(Attributes.KEY);

      final String key = DatatypeConverter.printHexBinary(MessageDigest
              .getInstance("MD5")
              .digest(build().getBytes(Charset.defaultCharset())));

      //TODO:DEBUG
      logger.log(Log.Level.info, key);

      this.metadata.put(Attributes.KEY, key);
    }
    catch (NoSuchAlgorithmException e) {
      logger.exception(e);
      throw new RuntimeException(e);
    }
  }

  public Header getHeader() {
    return this.header;
  }

  public FeatureBuilder withFeatureName(final String value) {
    logger.log(Log.Level.trace, value);
    this.name = value;
    return this;
  }

  public FeatureBuilder withFeatureVersion(final String value) {
    logger.log(Log.Level.trace, value);
    this.version = value;
    return this;
  }

  public FeatureBuilder withStartDate(final String value) {
    logger.log(Log.Level.trace, value);
    this.start = value;
    return this;
  }

  public FeatureBuilder withEndDate(final String value) {
    logger.log(Log.Level.trace, value);
    this.end = value;
    return this;
  }

  public FeatureBuilder withFeatureCount(final long value) {
    logger.log(Log.Level.trace, ""+value);
    this.count = value;
    return this;
  }

  public FeatureBuilder withMetadata(final Attributes key, final String value) {
    logger.log(Log.Level.trace, key + "|" + value);
    this.metadata.put(key, value);
    return this;
  }

  public FeatureBuilder withMetadataAsString(final String key, final String value) {
    logger.log(Log.Level.trace, key + "|" + value);

    try {
      return withMetadata(Attributes.valueOf(key), value);
    }
    catch (final IllegalArgumentException e) {
      logger.exception(e);
      return this;
    }
  }

  public String build() {
    final StringBuilder bfr = new StringBuilder();

    try {
      final Function<Attributes, Void> add_metadata = (key) -> {
        if (this.metadata.containsKey(key)) {
          bfr.append(String.format("%s=%s ", key.toString(), this.metadata.get(key)));
        }
        return null;
      };

      final BiFunction<Attributes, Object, Void> add_value = (key, value) -> {
        bfr.append(String.format("%s=%s ", key, value));
        return null;
      };

      bfr.append(String.format("%s %s", Attributes.FEATURE, this.name)).append(lf);
      bfr.append(lbrace).append(lf);
      add_value.apply(Attributes.VENDOR, vendor);
      //        bfr.append("%s=HBMUK ", Attributes.VENDOR);

      if (count != null) {
        add_value.apply(Attributes.COUNT, count);
        //          bfr.append(String.format("COUNT=%s ", count));
      }
      else {
        add_value.apply(Attributes.COUNT, unlimited);
        //          bfr.append("%s=UNLIMITED ", Attributes.COUNT);
      }

      add_metadata.apply(Attributes.KEYTYPE);

      if (this.version != null) {
        add_value.apply(Attributes.VERSION, count);
        //          bfr.append(String.format("VERSION=%s ", version));
      }

      if (this.start != null) {
        //          bfr.append(String.format("START=%s ", start));
        add_value.apply(Attributes.START, start);
      }

      if (this.end != null) {
        //          bfr.append(String.format("END=%s ", end));
        add_value.apply(Attributes.END, end);
      }

      // new line
      bfr.append(lf);

      add_metadata.apply(Attributes.COMMENT);

      add_metadata.apply(Attributes.SN);

      // new line
      bfr.append(lf);

      add_metadata.apply(Attributes.KEY);

      bfr.append(lf).append(rbrace).append(lf);

      return bfr.toString();
    }
    catch (final Exception e) {
      logger.exception(e);
      throw new RuntimeException(e);
    }
  }
}
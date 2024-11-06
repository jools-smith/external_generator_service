package com.flexnet.external.webservice.implementor.lmx;

import com.flexnet.external.utils.Log;

import java.util.LinkedList;

class LicenseBuilder {
  private final static Log logger = Log.create(LicenseBuilder.class);

  private final static String lf = System.lineSeparator();

  private final LinkedList<FeatureBuilder> features = new LinkedList<>();
  private String type;
  private String account;
  private String creationDate;
  private String customer;
  private String createdBy;
  private String license;
  private String country;
  private String generatedOn;

  public LicenseBuilder setType(final String value) {
    this.type = value;
    return this;
  }
  public LicenseBuilder setAccount(final String value) {
    this.account = value;
    return this;
  }
  public LicenseBuilder setCreationDate(final String value) {
    this.creationDate = value;
    return this;
  }
  public LicenseBuilder setCustomer(final String value) {
    this.customer = value;
    return this;
  }
  public LicenseBuilder setCreatedBy(final String value) {
    this.createdBy = value;
    return this;
  }
  public LicenseBuilder setLicense(final String value) {
    this.license = value;
    return this;
  }
  public LicenseBuilder setCountry(final String value) {
    this.country = value;
    return this;
  }
  public LicenseBuilder setGeneratedOn(final String value) {
    this.generatedOn = value;
    return this;
  }

  public FeatureBuilder createFeatureBuilder() {
    this.features.add(new FeatureBuilder());

    return this.features.getLast();
  }


  public String generate() {
    final StringBuilder bfr = new StringBuilder();

    bfr.append(String.format("# Type:%s", this.type)).append(lf);
    bfr.append(String.format("# Account:%s", this.account)).append(lf);
    bfr.append(String.format("# Creation Date:%s", this.creationDate)).append(lf);
    bfr.append(String.format("# Customer:%s", this.customer)).append(lf);
    bfr.append(String.format("# License Created:%s", this.createdBy)).append(lf);
    bfr.append(String.format("# License:%s", this.license)).append(lf);
    bfr.append(String.format("# Country:%s", this.country)).append(lf);
    bfr.append(String.format("# Generated on:%s", this.generatedOn)).append(lf);
    bfr.append(lf);

    this.features.forEach(builder -> {
      bfr.append(builder.build());
      bfr.append(lf);
    });

    return bfr.toString();
  }
}
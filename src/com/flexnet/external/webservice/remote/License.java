package com.flexnet.external.webservice.remote;

import com.flexnet.external.type.GeneratorResponse;

import java.util.function.Function;

public class License extends ResponseBase {
  public Boolean isComplete;
  public Boolean isBinary;
  public String filename;
  public String text;
  public byte[] binary;
  public String message;

  public static final Function<License, GeneratorResponse> encode = (response) -> new GeneratorResponse() {
    {
      this.complete = response.isComplete;
      this.licenseFileName = response.filename;
      if (response.isBinary) {
        this.binaryLicense = response.binary;
      }
      else {
        this.licenseText = response.text;
      }
      this.message = response.message;
    }
  };
}
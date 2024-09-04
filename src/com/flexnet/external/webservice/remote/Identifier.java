package com.flexnet.external.webservice.remote;

import com.flexnet.external.type.Id;

import java.util.function.Function;

public class Identifier extends ResponseBase {
  public String id;

  public static final Function<Identifier, Id> encode = (response) -> new Id() {
    {
      this.id = response.id;
    }
  };
}

package com.flexnet.external.webservice.remote;

public class Dummy extends ResponseBase {
  public <T> T decode(final Class<T> clazz) throws InstantiationException, IllegalAccessException {
    return clazz.newInstance();
  }
}

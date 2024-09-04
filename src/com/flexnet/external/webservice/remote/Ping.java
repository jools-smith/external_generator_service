package com.flexnet.external.webservice.remote;

import com.flexnet.external.type.PingResponse;

import java.util.function.Function;

public class Ping extends ResponseBase {
  public String info;
  public String data;
  public static final Function<Ping, PingResponse> encode = (response) -> new PingResponse() {
    {
      this.processedTime = response.timestamp;
      this.str = response.data;
      this.info = response.info;
    }
  };
}

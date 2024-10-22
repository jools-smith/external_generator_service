package com.flexnet.external.webservice;

import com.flexnet.external.type.PingRequest;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;

import java.time.Instant;
import java.util.UUID;

public abstract class ImplementorBase {
  /** CLASS **/
  protected final Log logger = new Log(this.getClass());
  
  protected String serialize(final Object payload) {
    try {
      return Utils.json_mapper.writeValueAsString(payload);
    }
    catch (final Throwable t) {
      throw new RuntimeException(t);
    }
  }

  protected String id() {
    return String.join("-", UUID.randomUUID().toString().replace("-","").split("(?<=\\G.{4})"));
  }

  protected PingResponse ping(final PingRequest request) {
    return new PingResponse() {
      {
        this.info = "TBD";
        this.str = request.getStr();
        this.processedTime = Instant.now().toString();
      }
    };
  }
}

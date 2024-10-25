package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.PingRequest;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.ServiceBase;

import java.time.Instant;
import java.util.UUID;

public abstract class ImplementorBase {

  static final String build = "0013";

  static final String version = "2024.10.25";

  /** CLASS **/
  protected final Log logger = Log.create(this.getClass());
  
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
        this.info = serialize(ServiceBase.diagnostics.serialize());
        this.str = String.format("version | %s | %s", version, build);
        this.processedTime = Instant.now().toString();
      }
    };
  }

  public String technologyName() {
    return ImplementorFactory.default_technology_name;
  }
}

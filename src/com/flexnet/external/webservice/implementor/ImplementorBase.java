package com.flexnet.external.webservice.implementor;

import com.flexnet.external.type.PingRequest;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.ServiceBase;

import java.time.Instant;
import java.util.UUID;

public abstract class ImplementorBase {

  protected final Log logger = Log.create(this.getClass());

  protected String id() {
    return String.join("-", UUID
            .randomUUID()
            .toString().replace("-","")
            .split("(?<=\\G.{4})"));
  }

  protected PingResponse ping(final PingRequest request) {
    return new PingResponse() {
      {
        this.info = Utils.safeSerialize(ServiceBase.getDiagnostics().serialize());
        this.str = String.format("version | %s", ServiceBase.getVersionBuild());
        this.processedTime = Instant.now().toString();
      }
    };
  }

  public abstract String technologyName();
}

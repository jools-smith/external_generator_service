package com.flexnet.external.webservice;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Diagnostics;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.implementor.ImplementorFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

class ServiceStatic {
  static final String build = "1025";

  static final String version = "2024.10.31";

  static final ImplementorFactory factory = new ImplementorFactory();

  static final Diagnostics diagnostics = new Diagnostics();

  public static String getVersion() {
    return version;
  }

  public static String getBuild() {
    return build;
  }

  public static Diagnostics getDiagnostics() {
    return diagnostics;
  }

  public static ImplementorFactory getImplementorFactory() {
    return factory;
  }

  // debuggery
  static {
    Log.create(ServiceStatic.class).log(Log.Level.info, String.format("version | %s | %s", version, build));
  }
}

public abstract class ServiceBase extends ServiceStatic {

  protected final Log logger = Log.create(this.getClass());

  protected ServiceBase() {
    this.logger.in();
  }

  protected String getLicenseTechnology(final Object obj) {
    final AtomicReference<LicenseTechnology> tech = new AtomicReference<>();

    if (obj instanceof ProductRequest) {
      tech.set(((ProductRequest) obj).getLicenseTechnology());
    }
    else if (obj instanceof LicenseModelRequest) {
      tech.set(((LicenseModelRequest) obj).getLicenseTechnology());
    }
    else if (obj instanceof GeneratorRequest) {
      tech.set(((GeneratorRequest) obj).getLicenseTechnology());
    }
    else if (obj instanceof ConsolidatedLicenseResquest) {
      ((ConsolidatedLicenseResquest) obj).getFulfillments().stream().findFirst().ifPresent(x -> {
        tech.set(x.getLicenseTechnology());
      });
    }
    else if (obj instanceof FulfillmentRecordSet) {
      ((FulfillmentRecordSet) obj).getFulfillments().stream().findFirst().ifPresent(x -> {
        tech.set(x.getLicenseTechnology());
      });
    }
    else if (obj instanceof RenewableEntitlementLineItems) {
      ((RenewableEntitlementLineItems) obj).getRenewableEntitlementLineItems().stream().findFirst().ifPresent(x -> {
        tech.set(x.getLicenseTechnology());
      });
    }
    else if (obj instanceof EntitlementLineItem) {
      tech.set(((EntitlementLineItem) obj).getLicenseTechnology());
    }
    else if (obj instanceof FulfillmentRecord) {
      tech.set(((FulfillmentRecord) obj).getLicenseTechnology());
    }
    else if (obj instanceof ConsolidatedLicenseRecord) {
      tech.set(((ConsolidatedLicenseRecord) obj).getLicenseTechnology());
    }

    if (tech.get() == null) {
      throw new RuntimeException(obj.getClass().getName() + " | cannot retrieve license technology");
    }
    else {
      this.logger.log(Log.Level.info, "license tech:" + tech.get().getName());
      return tech.get().getName();
    }
  }

  protected Token token() {
    final StackTraceElement frame = Thread.currentThread().getStackTrace()[2];
    
    return diagnostics.getToken(this.getClass(), frame.getMethodName());
  }
  
  public Function<Throwable, SvcException> serviceException = (throwable) -> new SvcException() {
    {
      this.setMessage(Utils.frameDetails(Thread.currentThread().getStackTrace()[3]));
      
      this.setName(throwable.getClass().getName());
    }
  };
}
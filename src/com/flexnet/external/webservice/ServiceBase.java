package com.flexnet.external.webservice;

import com.flexnet.external.type.*;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Diagnostics;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.webservice.implementor.ImplementorFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class ServiceBase {

  /** CLASS **/
  protected final Log logger = Log.create(this.getClass());

  /** STATIC **/

  protected final ImplementorFactory factory = new ImplementorFactory();

  public final static Diagnostics diagnostics = new Diagnostics();

  protected final static Log rootLogger = Log.create(ServiceBase.class);

  private static final Runnable diagnostics_housekeeping = () -> {
    try {
      rootLogger.yaml(diagnostics.serialize());
    }
    catch(final Throwable t) {
      rootLogger.exception(t);
    }
    finally {
      rootLogger.out();
    }
  };

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


    if (tech.get() != null) {
      logger.log(Log.Level.info, "license tech:" + tech.get().getName());
      return tech.get().getName();
    }

    throw new RuntimeException(obj.getClass().getName() + " | cannot retrieve license technology");
  }


  protected Token token() {
    final StackTraceElement frame = Thread.currentThread().getStackTrace()[2];
    
    return diagnostics.getToken(this.getClass(), frame.getMethodName());
  }
  
  protected static Function<StackTraceElement, String> frameDetails = (frame) ->
          String.join("|",
                  frame.getFileName(),
                  frame.getClassName(),
                  frame.getMethodName(),
                  String.valueOf(frame.getLineNumber()));
  
  protected Function<Throwable, SvcException> serviceException = (throwable) -> new SvcException() {
    {
      this.setMessage(frameDetails.apply(Thread.currentThread().getStackTrace()[3]));
      
      this.setName(throwable.getClass().getName());
    }
  };
}
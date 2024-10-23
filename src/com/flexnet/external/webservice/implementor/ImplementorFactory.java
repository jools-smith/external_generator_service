package com.flexnet.external.webservice.implementor;

import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.idgenerator.IdGeneratorServiceInterface;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;
import com.flexnet.external.webservice.renewal.RenewalServiceInterface;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ImplementorFactory {
  private final static Log logger = Log.create(ImplementorFactory.class);

  public final static String DEF = "DEF";

  private final List<Triple<String, Class<?>, ?>> implementors = new ArrayList<>();

  public ImplementorFactory() {
    logger.in();
    try {
      /* add specific technology implementors */
      this.implementors.add(Triple.of(DEF, IdGeneratorServiceInterface.class, new DefaultIdGenerator()));
      this.implementors.add(Triple.of(DEF, LicenseGeneratorServiceInterface.class, new DefaultLicenseGenerator()));
      this.implementors.add(Triple.of(DEF, RenewalServiceInterface.class, new DefaultRenewalService()));
    }
    catch (final Throwable t) {
      logger.exception(t);
    }
  }

  public <T> T getDefaultImplementor(final Class<T> type) {

    logger.array(Log.Level.info, "requested type", type.getSimpleName());

    return type.cast(implementors.stream()
            .filter(x -> x.getLeft().equals(DEF))
            .filter(x -> x.getMiddle() == type)
            .findAny()
            .get()
            .getRight());
  }

  public <T> T getImplementor(final String technology, final Class<T> type) {
    logger.array(Log.Level.info, "requested type", technology, type.getSimpleName());

    final AtomicReference<T> implementor = new AtomicReference<>(getDefaultImplementor(type));

    implementors.stream().filter(x -> x.getLeft().equals(technology)).filter(x -> x.getMiddle() == type).findAny().ifPresent(x -> {
      logger.array(Log.Level.debug, "found type", x.getRight().getClass().getSimpleName());

      implementor.set( type.cast( x.getRight() ) );
    });

    return implementor.get();
  }
}

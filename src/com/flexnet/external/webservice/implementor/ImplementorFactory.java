package com.flexnet.external.webservice.implementor;

import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.idgenerator.IdGeneratorServiceInterface;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;
import com.flexnet.external.webservice.renewal.RenewalServiceInterface;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ImplementorFactory {
  private final static Log logger = Log.create(ImplementorFactory.class);

  public final static String default_technology_name = "DEF";

  private final List<Triple<String, Class<?>, ImplementorBase>> implementors = new ArrayList<>();

  private void addImplementor(final Class<?> type, final ImplementorBase imp) {
    if (type.isAssignableFrom(imp.getClass())) {
      this.implementors.add(Triple.of(imp.technologyName(), type, imp));
    }
    else {
      throw new RuntimeException(imp.getClass().getSimpleName() + " does not implement " + type.getSimpleName());
    }
  }

  public ImplementorFactory() {
    logger.in();
    try {
      /* add default technology implementors */
      addImplementor(IdGeneratorServiceInterface.class, new DefaultIdGenerator());
      addImplementor(LicenseGeneratorServiceInterface.class, new DefaultLicenseGenerator());
      addImplementor(RenewalServiceInterface.class, new DefaultRenewalService());

      /* LMX */
      addImplementor(LicenseGeneratorServiceInterface.class, new LmxLicenseGenerator());
    }
    catch (final Throwable t) {
      logger.exception(t);
    }
  }

  public <T> T getDefaultImplementor(final Class<T> type) {

    logger.array(Log.Level.info, "requested type", type.getSimpleName());

    return type.cast(implementors.stream()
            .filter(x -> x.getLeft().equals(default_technology_name))
            .filter(x -> x.getMiddle() == type)
            .findAny()
            .get()
            .getRight());
  }

  public <T> T getImplementor(final String technology, final Class<T> type) {
    logger.array(Log.Level.info, "requested type", technology, type.getSimpleName());

    final AtomicReference<T> implementor = new AtomicReference<>();

    implementors.stream().filter(x -> x.getLeft().equals(technology)).filter(x -> x.getMiddle() == type).findAny().ifPresent(x -> {
      logger.array(Log.Level.debug, "found type", x.getRight().getClass().getSimpleName());

      implementor.set( type.cast( x.getRight() ) );
    });

    return implementor.get() != null ? implementor.get() : getDefaultImplementor(type);
  }
}

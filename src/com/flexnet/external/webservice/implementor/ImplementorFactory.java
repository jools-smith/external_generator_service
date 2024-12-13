package com.flexnet.external.webservice.implementor;

import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.implementor.lmx.LmxLicenseGenerator;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ImplementorFactory {
  private final static Log logger = Log.create(ImplementorFactory.class);

  public final static String default_technology_name = "DEF";

  private final Map<String, LicenseGeneratorServiceInterface> implementors = new LinkedHashMap<>();

  private void addImplementor(final ImplementorBase imp) {

    logger.log(Log.Level.debug, imp.technologyName() + " -> " + imp.getClass().getSimpleName());

    this.implementors.put(imp.technologyName(), (LicenseGeneratorServiceInterface) imp);
  }

  public ImplementorFactory() {
    logger.in();
    try {
      /* add default technology implementors */
      addImplementor(new DefaultLicenseGenerator());

      /* LMX */
      addImplementor(new LmxLicenseGenerator());
    }
    catch (final Throwable t) {
      logger.exception(t);
    }
  }

  public LicenseGeneratorServiceInterface getDefaultImplementor() {
    return this.implementors.get(default_technology_name);
  }

  public LicenseGeneratorServiceInterface getImplementor(final String technology) {
    if (this.implementors.containsKey(technology)) {
      return this.implementors.get(technology);
    }
    else {
      return getDefaultImplementor();
    }
  }
}

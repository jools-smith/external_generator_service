package com.flexnet.external.webservice.implementor;

import com.flexnet.external.utils.AnnotationManager;
import com.flexnet.external.utils.GeneratorImplementor;
import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.keygenerator.LicenseGeneratorServiceInterface;

import java.util.*;

public class ImplementorFactory {
  private final static Log logger = Log.create(ImplementorFactory.class);

  public final static String default_technology_name = "DEF";

  private final Map<String, LicenseGeneratorServiceInterface> implementors = new HashMap<>();

  public void addImplementor(final ImplementorBase imp) {

    logger.log(Log.Level.debug, imp.technologyName() + " -> " + imp.getClass().getSimpleName());

    this.implementors.put(imp.technologyName(), (LicenseGeneratorServiceInterface) imp);
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

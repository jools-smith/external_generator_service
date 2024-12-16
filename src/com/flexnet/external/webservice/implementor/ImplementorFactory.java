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

  private void addImplementor(final ImplementorBase imp) {

    logger.log(Log.Level.debug, imp.technologyName() + " -> " + imp.getClass().getSimpleName());

    this.implementors.put(imp.technologyName(), (LicenseGeneratorServiceInterface) imp);
  }

  public ImplementorFactory() {
    logger.in();
    try {
      final AnnotationManager manager = new AnnotationManager();

      final List<String> files = manager.findClassFilesInPackage(ImplementorBase.class);

      for (final String typename : files) {

        final Class<?> type = Class.forName(typename);

        if (type.isAnnotationPresent(GeneratorImplementor.class)) {

          final GeneratorImplementor ann = type.getAnnotation(GeneratorImplementor.class);

          logger.array(Log.Level.debug, "located annotated implementor", ann.technology(), type.getName());

          if (ImplementorBase.class.isAssignableFrom(type)) {

            addImplementor((ImplementorBase) type.newInstance());
          }
        }
      }
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

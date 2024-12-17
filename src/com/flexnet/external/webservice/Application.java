package com.flexnet.external.webservice;

import com.flexnet.external.utils.AnnotationManager;
import com.flexnet.external.utils.Diagnostics;
import com.flexnet.external.utils.GeneratorImplementor;
import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.implementor.ImplementorBase;
import com.flexnet.external.webservice.implementor.ImplementorFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@WebListener
public class Application implements ServletContextListener {
  private static final AtomicReference<Application> singleton = new AtomicReference<>();
  private static final Log logger = Log.create(Application.class);

  private final String build;
  private final String version;
  private final ImplementorFactory implementorFactory = new ImplementorFactory();
  private final Diagnostics diagnostics = new Diagnostics();

  public Application() {
    logger.me(this);

    this.build = "3006";
    this.version = "2024.12.17";

    singleton.getAndSet(this);

    logger.log(Log.Level.info, String.format("version | %s | %s", version, build));
  }

  public static Application getInstance() {
    return singleton.get();
  }

  public Diagnostics getDiagnostics() {
    return diagnostics;
  }

  public final ImplementorFactory getImplementorFactory() {
    return implementorFactory;
  }

  public String getVersionDate() {
    return version;
  }

  public String getBuild() {
    return build;
  }

  @Override
  public void contextInitialized(final ServletContextEvent event) {
    logger.in();

    try {
      logAttributeNames(event);

      final AnnotationManager manager = new AnnotationManager();

      final List<String> files = manager.findClassFilesInPackage(ImplementorBase.class);

      for (final String typename : files) {

        final Class<?> type = Class.forName(typename);

        if (type.isAnnotationPresent(GeneratorImplementor.class)) {

          final GeneratorImplementor ann = type.getAnnotation(GeneratorImplementor.class);

          logger.array(Log.Level.debug, "located annotated implementor", ann.technology(), type.getName());

          if (ImplementorBase.class.isAssignableFrom(type)) {

            implementorFactory.addImplementor((ImplementorBase) type.newInstance());
          }
        }
      }
    }
    catch (final Throwable t) {
      logger.exception(t);
    }
    finally {
      logger.out();
    }
  }

  private void logAttributeNames(final ServletContextEvent event) {
    final Enumeration<String> itt = event.getServletContext().getAttributeNames();
    while (itt.hasMoreElements()) {
      logger.log(Log.Level.trace, itt.nextElement());
    }
  }

  @Override
  public void contextDestroyed(final ServletContextEvent event) {
    logger.in();
    try {
      logAttributeNames(event);
    }
    catch (final Throwable t) {
      logger.exception(t);
    }
    finally {
      logger.out();
    }
  }
}

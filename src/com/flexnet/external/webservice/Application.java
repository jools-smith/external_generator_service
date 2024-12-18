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
  private static final Log logger = Log.create(Application.class);

  /** instance */
  private static final AtomicReference<Application> singleton = new AtomicReference<>();
  public static Application getInstance() {
    return singleton.get();
  }

  /** build */
  private final String build;
  public String getBuildSequence() {
    return build;
  }

  /** version */
  private final String version;
  public String getVersionDate() {
    return version;
  }

  /** implementor factory */
  private final ImplementorFactory implementorFactory = new ImplementorFactory();
  public final ImplementorFactory getImplementorFactory() {
    return implementorFactory;
  }

  /** diagnostics */
  private final Diagnostics diagnostics = new Diagnostics();
  public Diagnostics getDiagnostics() {
    return diagnostics;
  }

  public Application() {
    /// we can reduce this potentially -- once levels have been assessed
    Log.setLoggingLevel(Log.Level.trace);

    logger.me(this);

    this.build = "3007";
    this.version = "2024.12.17";

    singleton.getAndSet(this);

    logger.log(Log.Level.info, String.format("version | %s | %s", version, build));
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

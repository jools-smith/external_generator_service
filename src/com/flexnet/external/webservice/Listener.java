package com.flexnet.external.webservice;

import com.flexnet.external.utils.Log;
import com.flexnet.external.webservice.transaction.TransactionException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Listener implements ServletContextListener {
  private final static Log logger = Log.create(ServiceBase.class);

  public static final AtomicReference<Listener> instance = new AtomicReference<>();

  private enum CallbackType {
    contextInitialized, contextDestroyed
  }

  private final Map<CallbackType, List<Runnable>> callbacks = new HashMap<>();

  public Listener() {
    logger.me(this);

    final Listener old = instance.getAndSet(this);

    TransactionException.assertNull(old);
  }

  private void registerContext(final CallbackType key,final Runnable action) {
    if (!this.callbacks.containsKey(key)) {
      this.callbacks.put(key, new ArrayList<>());
    }
    this.callbacks.get(key).add(action);
  }
  public void registerContextInitialized(final Runnable action) {
    registerContext(CallbackType.contextInitialized, action);
  }

  public void registerContextDestroyed(final Runnable action) {
    registerContext(CallbackType.contextDestroyed, action);
  }

  private void logAttributeNames(final ServletContextEvent event) {
    final Enumeration<String> itt = event.getServletContext().getAttributeNames();
    while (itt.hasMoreElements()) {
      logger.log(Log.Level.trace, itt.nextElement());
    }
  }

  @Override
  public void contextInitialized(final ServletContextEvent event) {
    logger.in();

    try {
      logAttributeNames(event);

      Optional.ofNullable(this.callbacks.get(CallbackType.contextInitialized)).ifPresent(list -> {
        list.forEach(Runnable::run);
      });
    }
    catch (final Throwable t){
      logger.exception(t);
    }
    finally {
      logger.out();
    }
  }

  @Override
  public void contextDestroyed(final ServletContextEvent event) {
    logger.in();
    try {
      logAttributeNames(event);

      Optional.ofNullable(this.callbacks.get(CallbackType.contextDestroyed)).ifPresent(list -> {
        list.forEach(Runnable::run);
      });
    }
    catch (final Throwable t){
      logger.exception(t);
    }
    finally {
      logger.out();
    }
  }
}

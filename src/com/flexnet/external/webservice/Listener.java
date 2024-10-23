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

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    logger.in();
    try {
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
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    logger.in();
    try {
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

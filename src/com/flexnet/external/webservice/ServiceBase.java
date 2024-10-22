package com.flexnet.external.webservice;

import com.flexnet.external.type.SvcException;
import com.flexnet.external.utils.Log;
import com.flexnet.external.utils.Diagnostics;
import com.flexnet.external.utils.Diagnostics.Token;
import com.flexnet.external.utils.Utils;
import com.flexnet.external.webservice.transaction.Transaction;
import org.apache.commons.io.FileUtils;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public abstract class ServiceBase {

  /** CLASS **/
  protected final Log logger = new Log(this.getClass());

  /** STATIC **/
  static AtomicBoolean go = new AtomicBoolean(true);
  static final Thread househeeping = new Thread() {

    public void run() {
      rootLogger.in();
      try {
        Thread.sleep(10000L);
        Optional.ofNullable(Listener.instance.get()).ifPresent(listener -> {
          //      listener.registerContextInitialized(context_initialized);
          listener.registerContextDestroyed(context_destroyed);
          rootLogger.log(Log.Level.info,"register context destroyed");
        });

        while(go.get()) {

          Thread.sleep(10000L);

          diagnostics_housekeeping.run();

//          transaction_housekeeping.run();
        }
      }
      catch (final Throwable t) {
        rootLogger.exception(t);
      }
      finally {
        rootLogger.out();
      }
    }
  };



  protected final static Diagnostics diagnostics = new Diagnostics();
//  protected final static Manager manager = new Manager();
  protected final static Log rootLogger = new Log(ServiceBase.class);

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

  static final String storage = "d:\\localhost\\extgenservice\\";

  private static final Runnable transaction_housekeeping = () -> {
    try {

      Optional.ofNullable(Transaction.removeAll()).ifPresent(list -> list.forEach(trans -> {

        final Path file = Paths.get(storage + trans.key() + ".json");
        try {
          rootLogger.log(Log.Level.trace, file.toAbsolutePath().toString());
          FileUtils.writeStringToFile(file.toFile(), Utils.json_mapper_indented.writeValueAsString(trans), Charset.defaultCharset());
        }
        catch (final Exception t) {
          rootLogger.exception(t);
        }
      }));
    }
    catch(final Throwable t) {
      rootLogger.exception(t);
    }
  };

  private static final Runnable context_destroyed = () -> {
    rootLogger.in();
    try {
      rootLogger.log(Log.Level.info,"context_destroyed");
      go.getAndSet(false);

      househeeping.interrupt();
    }
    catch(final Throwable t) {
      rootLogger.exception(t);
    }
    finally {
      rootLogger.out();
    }
  };


  static {
    househeeping.start();
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
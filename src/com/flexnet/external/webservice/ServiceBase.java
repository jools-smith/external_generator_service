package com.flexnet.external.webservice;

import com.flexnet.external.type.SvcException;
import com.flexnet.external.utils.ConsoleLogger;
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
import java.util.function.Function;

public abstract class ServiceBase {

  /** CLASS **/
  protected final ConsoleLogger logger = new ConsoleLogger(this.getClass());

  /** STATIC **/
//  protected final static ObjectMapper mapper = new ObjectMapper()
//          .enable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
//          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
//          .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//          .enable(SerializationFeature.INDENT_OUTPUT);

  protected final static Diagnostics diagnostics = new Diagnostics();
//  protected final static Manager manager = new Manager();
  protected final static ConsoleLogger rootLogger = new ConsoleLogger(ServiceBase.class);

  private static final Runnable diagnostics_housekeeping = () -> {
    try {
      rootLogger.yaml(diagnostics.serialize());
    }
    catch(final Throwable t) {
      rootLogger.error(t);
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
          rootLogger.trace(file.toAbsolutePath().toString());
          FileUtils.writeStringToFile(file.toFile(), Utils.json_mapper_indented.writeValueAsString(trans), Charset.defaultCharset());
        }
        catch (final Exception t) {
          rootLogger.error(t);
        }
      }));
    }
    catch(final Throwable t) {
      rootLogger.error(t);
    }
  };

  private static Timer timer;
  private static final LinkedList<TimerTask> jobs = new LinkedList<>();

  private static final Runnable context_initialized = () -> {
    rootLogger.in();
    try {
      rootLogger.info("context_initialized");
      timer = new Timer();
      timer.purge();
      jobs.add(new TimerTask() {
        @Override
        public void run() {
          diagnostics_housekeeping.run();
        }
      });
      timer.schedule(jobs.getLast(), 10000, 60000);

      jobs.add(new TimerTask() {
        @Override
        public void run() {
          transaction_housekeeping.run();
        }
      });
      timer.schedule(jobs.getLast(), 15000, 10000);
    }
    catch(final Throwable t) {
      rootLogger.error(t);
    }
    finally {
      rootLogger.out();
    }
  };

  private static final Runnable context_destroyed = () -> {
    rootLogger.in();
    try {
      rootLogger.info("context_destroyed");
      Optional.ofNullable(timer).ifPresent(t -> {
        t.purge();
        t.cancel();
      });
    }
    catch(final Throwable t) {
      rootLogger.error(t);
    }
    finally {
      rootLogger.out();
    }
  };

  static {
//    mapper.findAndRegisterModules();

    Optional.ofNullable(Listener.instance.get()).ifPresent(listener -> {
//      listener.registerContextInitialized(context_initialized);
      listener.registerContextDestroyed(context_destroyed);
    });

    context_initialized.run();
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
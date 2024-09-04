package com.flexnet.external.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flexnet.external.type.GeneratorResponse;
import com.flexnet.external.type.Id;
import com.flexnet.external.type.PingResponse;
import com.flexnet.external.type.SvcException;
import com.flexnet.external.webservice.Diagnostics.Token;
import com.flexnet.external.webservice.transaction.Manager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;


public abstract class ServiceBase {

  public enum Endpoint {
    health,
    ping,
    //IGI_
    IGI_generateEntitlementID,
    IGI_generateLineItemID,
    IGI_generateWebRegKey,
    IGI_generateMaintenanceItemID,
    IGI_generateFulfillmentID,
    IGI_generateConsolidatedLicenseID,
    IGI_generateConsolidatedItemID,
    //LGI_
    LGI_validateProduct,
    LGI_validateLicenseModel,
    LGI_generateLicense,
    LGI_consolidateFulfillments,
    LGI_generateLicenseFilenames,
    LGI_generateConsolidatedLicenseFilenames,
    LGI_generateCustomHostIdentifier,
    //RenewalServiceInterface
    RSI_request,
    //RenewalRedirectServiceInterface
    RRI_request
  }

  protected <V,T,S> Executor<V, T,S> createExecutor() {
    return new Executor<>();
  }
  public class Executor<V,T,S> {
    Manager.Transaction transaction = manager.createTransaction(this.getClass().getSimpleName());
    private T response;
    private V payload;
    private S value;
    private byte[] content;

    public S value() {
      return this.value;
    }

    public V payload() {
      return this.payload;
    }

    public T response() {
      return this.response;
    }

    Executor() {
      logger.me(this);
    }
    public Manager.Transaction createTransaction() {
      return transaction;
    }

    public void commit() {
      this.transaction.commit();
    }
    public Executor<V,T,S> encode(Function<T, S> processor) {
      this.value = processor.apply(this.response);
      this.transaction.addResponse(this.value);
      return this;
    }
    public Executor<V,T,S> decode(final Class<T> clazz) throws IOException {
      this.response = mapper.readValue(content, clazz);
      this.transaction.lastElement().addResponse(this.response);
      return this;
    }
    public Executor<V,T,S> execute(final Endpoint endpoint, final V payload) throws URISyntaxException, IOException {

      this.transaction.createElement();
      final HttpPost request = new HttpPost(new URIBuilder()
              .setScheme("http")
              .setHost("localhost")
              .setPort(5245)
              .setPath("revenera/api/1.0/process/" + endpoint.toString()).build());

      if (payload != null) {
        this.transaction.lastElement().addRequest(payload);

        final String content = mapper.writeValueAsString(this.payload = payload);

        final StringEntity entity = new StringEntity(content);

        request.setEntity(entity);
      }

      request.setHeader("Content-type", "application/json");
      request.setHeader("Accept", "application/json");

      try (final CloseableHttpClient client = HttpClients.createDefault();) {
        final CloseableHttpResponse response = client.execute(request);

        this.content = IOUtils.toByteArray(response.getEntity().getContent());

        if (response.getCode() != 200) {
          throw new RuntimeException(String.format("code:%d %s", response.getCode(), this.content == null ? "" : new String(this.content)));
        }

        return this;
      }
    }
  }
  static class ResponseBase {
    public String timestamp;
  }
  
  public static class Health extends ResponseBase {

  }
  
  public static class Ping extends ResponseBase {
    public String info;
    public String data;
    public static final Function<Ping, PingResponse> encode = (response) -> new PingResponse() {
      {
        this.processedTime = response.timestamp;
        this.str = response.data;
        this.info = response.info;
      }
    };
  }
  
  public static class Identifier extends ResponseBase {
    public String id;

    public static final Function<Identifier, Id> encode = (response) -> new Id() {
      {
        this.id = response.id;
      }
    };
  }
  
  public static class License extends ResponseBase {
    public Boolean isComplete;
    public Boolean isBinary;
    public String filename;
    public String text;
    public byte[] binary;
    public String message;

    public static final Function<License, GeneratorResponse> encode = (response) -> new GeneratorResponse() {
      {
        this.complete = response.isComplete;
        this.licenseFileName = response.filename;
        if (response.isBinary) {
          this.binaryLicense = response.binary;
        }
        else {
          this.licenseText = response.text;
        }
        this.message = response.message;
      }
    };
  }
  
  public static class Void extends ResponseBase {
    public <T> T decode(final Class<T> clazz) throws InstantiationException, IllegalAccessException {
      return clazz.newInstance();
    }
  }

  /** CLASS **/
  protected final ConsoleLogger logger = new ConsoleLogger(this.getClass());

  /** STATIC **/
  protected final static ObjectMapper mapper = new ObjectMapper()
          .enable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.INDENT_OUTPUT);

  protected final static Diagnostics diagnostics = new Diagnostics();
  protected final static Manager manager = new Manager();
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

      Optional.ofNullable(manager.removeAllTransactions()).ifPresent(list -> {
        list.forEach(trans -> {

          final Path file = Paths.get(storage + trans.key() + ".json");
          try {
            rootLogger.trace(file.toAbsolutePath().toString());
            FileUtils.writeStringToFile(file.toFile(), mapper.writeValueAsString(trans), Charset.defaultCharset());
          }
          catch (final Exception t) {
            rootLogger.error(t);
          }
        });
      });
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
    mapper.findAndRegisterModules();

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

  protected <T> T execute(final Class<T> clazz, final Endpoint endpoint, final Object payload)
      throws URISyntaxException, IOException {
    
    final HttpPost request = new HttpPost(new URIBuilder()
        .setScheme("http")
        .setHost("localhost")
        .setPort(5245)
        .setPath("revenera/api/1.0/process/" + endpoint.toString()).build());
    
    if (payload != null) {
      final String content = mapper.writeValueAsString(payload);
      
      final StringEntity entity = new StringEntity(content);
      
      request.setEntity(entity);
    }

    request.setHeader("Content-type", "application/json");
    request.setHeader("Accept", "application/json");
    
    try (final CloseableHttpClient client = HttpClients.createDefault();) {
      final CloseableHttpResponse response = client.execute(request);
      
      final byte[] content = IOUtils.toByteArray(response.getEntity().getContent());
      
      if (response.getCode() == 200) {
        return mapper.readValue(content, clazz);
      }
      
      throw new RuntimeException(String.format("code:%d %s", response.getCode(), content == null ? "" : new String(content)));
    }
  }
}
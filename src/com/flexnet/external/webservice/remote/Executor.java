package com.flexnet.external.webservice.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flexnet.external.utils.ConsoleLogger;
import com.flexnet.external.webservice.transaction.Transaction;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Function;

public class Executor<V,T,S> {
  private final static ConsoleLogger logger = new ConsoleLogger(Executor.class);

  private final static ObjectMapper mapper = new ObjectMapper()
          .enable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.INDENT_OUTPUT);

  Transaction transaction = Transaction.create(this.getClass().getSimpleName());

  private T response;
  private V payload;
  private S value;
  private byte[] content;

  /**
   *
   */
  protected Executor() {
    logger.me(this);
  }

  /**
   *
   */
  public void commit() {
    this.transaction.commit();
  }

  /**
   *
   * @return
   */
  public S value() {
    return this.value;
  }

  /**
   *
   * @return
   */
  public V payload() {
    return this.payload;
  }

  /**
   *
   * @return
   */
  public T response() {
    return this.response;
  }
  /**
   *
   * @param processor
   * @return
   */
  public Executor<V,T,S> encode(Function<T, S> processor) {
    this.value = processor.apply(this.response);
    this.transaction.addResponse(this.value);
    return this;
  }

  /**
   *
   * @param clazz
   * @return
   * @throws IOException
   */
  public Executor<V,T,S> decode(final Class<T> clazz) throws IOException {
    this.response = mapper.readValue(content, clazz);
    this.transaction.lastElement().addResponse(this.response);
    return this;
  }

  /**
   *
   * @param endpoint
   * @param payload
   * @return
   * @throws URISyntaxException
   * @throws IOException
   */
  public Executor<V,T,S> execute(final EndpointType endpoint, final V payload) throws URISyntaxException, IOException {

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

  /**
   *
   * @param clazz
   * @param endpoint
   * @param payload
   * @return
   * @param <T>
   * @throws URISyntaxException
   * @throws IOException
   */
//  public static <T> T execute(final Class<T> clazz, final EndpointType endpoint, final Object payload)
//          throws URISyntaxException, IOException {
//
//    final HttpPost request = new HttpPost(new URIBuilder()
//            .setScheme("http")
//            .setHost("localhost")
//            .setPort(5245)
//            .setPath("revenera/api/1.0/process/" + endpoint.toString()).build());
//
//    if (payload != null) {
//      final String content = mapper.writeValueAsString(payload);
//
//      final StringEntity entity = new StringEntity(content);
//
//      request.setEntity(entity);
//    }
//
//    request.setHeader("Content-type", "application/json");
//    request.setHeader("Accept", "application/json");
//
//    try (final CloseableHttpClient client = HttpClients.createDefault();) {
//      final CloseableHttpResponse response = client.execute(request);
//
//      final byte[] content = IOUtils.toByteArray(response.getEntity().getContent());
//
//      if (response.getCode() == 200) {
//        return mapper.readValue(content, clazz);
//      }
//
//      throw new RuntimeException(String.format("code:%d %s", response.getCode(), content == null ? "" : new String(content)));
//    }
//  }

  /**
   *
   * @return
   * @param <V>
   * @param <T>
   * @param <S>
   */
  public static <V,T,S> Executor<V, T,S> createExecutor() {
    return new Executor<>();
  }
}

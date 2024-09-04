package com.flexnet.external.webservice.transaction;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexnet.external.webservice.ConsoleLogger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public final class Manager {

  public static final ConsoleLogger logger = new ConsoleLogger(Manager.class);

  public Manager() {
    logger.me(this);
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
  public class Transaction extends NamedEntity {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public class Element extends TimedEntity {
      Object request;
      Object response;
      private Element() {
        logger.me(this);
      }
      @JsonIgnore
      public Element addRequest(final Object request) {
        // debug
        TransactionException.assertNotNull(request);
        TransactionException.assertNull(this.request);
        // debug
        this.request = request;
        return this;
      }

      @JsonIgnore
      public Element addResponse(final Object response) {
        // debug
        TransactionException.assertNotNull(response);
        TransactionException.assertNull(this.response);
        // debug
        this.response = response;
        super.stop();
        return this;
      }
    }

    private final LinkedList<Element> elements = new LinkedList<>();
    private Object response;

    Transaction(final String name) {
      super(name);
      logger.me(this);
    }

    @JsonIgnore
    public Transaction addResponse(final Object response) {
      // debug
      TransactionException.assertNotNull(response);
      TransactionException.assertNull(this.response);
      // debug
      super.stop();
      this.response = response;
      return this;
    }

    @JsonIgnore
    public Element lastElement() {
      return this.elements.getLast();
    }

    @JsonIgnore
    public Transaction commit()  {
      addTransaction(this);
      return this;
    }

    @JsonIgnore
    public Element createElement() {
      final Element element = new Element();
      this.elements.add(element);
      return element;
    }
  }

  private final LinkedBlockingQueue<Transaction> transactions = new LinkedBlockingQueue<>();

  public void addTransaction(final Transaction transaction)  {
    this.transactions.add(transaction);
    logger.array(logger.info, "transaction size", this.transactions.size());
  }
  public Transaction removeTransaction() {
    return this.transactions.poll();
  }
  public List<Transaction> removeAllTransactions() {
    if (!this.transactions.isEmpty()) {
      final List<Transaction> list = new ArrayList<>();
      this.transactions.drainTo(list);
      return list;
    }
    return null;
  }
  public Transaction createTransaction(final String name) {
    return new Transaction(name);
  }
}

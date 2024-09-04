package com.flexnet.external.webservice;

import com.flexnet.external.webservice.transaction.Manager;

public abstract class Context<R, S, T> {
  private static final Manager transactions = new Manager();

  private static final Diagnostics diagnostics = new Diagnostics();
  public abstract T execute(R r, S s);

  public static Manager getManager() {
    return transactions;
  }

  public static Diagnostics getDiagnostics() {
    return diagnostics;
  }
}

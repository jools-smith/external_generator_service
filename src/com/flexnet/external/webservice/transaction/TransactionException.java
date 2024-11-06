package com.flexnet.external.webservice.transaction;

import com.flexnet.external.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TransactionException extends RuntimeException {
//  public static Function<Integer, String[]> frameDetails = (offset) -> {
//    final StackTraceElement frame = Thread.currentThread().getStackTrace()[offset];
//
//    return new String[] {
//            frame.getClassName(),
//            frame.getMethodName(),
//            String.valueOf(frame.getLineNumber())
//    };
//  };

  private static void assertNow(final String... parameters) {

    final List<String> list = new ArrayList<>();

    list.addAll(Arrays.asList(parameters));
    list.addAll(Arrays.asList(Utils.frameDetails.apply(4)));

    throw new TransactionException(String.join(" | ", list));
  }

  public TransactionException(final String message) {
    super(message);
  }

  public static void assertNotNull(final Object obj) {
    if (obj == null) {

      assertNow("null value not allowed");
    }
  }

  public static void assertNull(final Object obj) {
    if (obj != null) {
      assertNow("null value required", obj.getClass().getSimpleName());
    }
  }
}

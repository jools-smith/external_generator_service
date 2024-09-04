package com.flexnet.external.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.function.Function;

public class Utils {
  public static final Function<Integer, String[]> frameDetails = (offset) -> {
    final StackTraceElement frame = Thread.currentThread().getStackTrace()[offset];

    return new String[] {
            frame.getClassName(),
            frame.getMethodName(),
            String.valueOf(frame.getLineNumber())
    };
  };

  public static final  ObjectMapper json_mapper_indented = new ObjectMapper()
          .enable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.INDENT_OUTPUT);

  public static final  ObjectMapper json_mapper = new ObjectMapper()
          .enable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.INDENT_OUTPUT);

  static {
    json_mapper_indented.findAndRegisterModules();
    json_mapper.findAndRegisterModules();
  }
}

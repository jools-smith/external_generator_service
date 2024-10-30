package com.flexnet.external.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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

  public static String abbreviatePackageName(final String name, final int limit) {

    String str = name;

    if (str.length() > limit) {
      final String[] parts = name.split("\\.");

      for (int i = 0; i < parts.length; i++) {
        parts[i] = parts[i].substring(0, 1);

        str = String.join(".", parts);
        if (str.length() <= limit) {
          break;
        }
      }
    }

    return str;
  }

  public static final  ObjectMapper json_mapper_indented = new ObjectMapper()
          .enable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.INDENT_OUTPUT);

  public static final  ObjectMapper json_mapper = new ObjectMapper()
          .enable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public static final  ObjectMapper yaml_mapper = new ObjectMapper(new YAMLFactory())
          .enable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public static String safeSerialize(final Object payload) {
    try {
      return json_mapper.writeValueAsString(payload);
    }
    catch (final Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static String safeSerializeYaml(final Object payload) {
    try {
      return yaml_mapper.writeValueAsString(payload);
    }
    catch (final Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static String frameDetails(final StackTraceElement frame) {
    return  String.join("|",
            frame.getFileName(),
            frame.getClassName(),
            frame.getMethodName(),
            java.lang.String.valueOf(frame.getLineNumber()));
  }

  static {
    json_mapper_indented.findAndRegisterModules();
    json_mapper.findAndRegisterModules();
  }
}

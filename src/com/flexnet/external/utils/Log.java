package com.flexnet.external.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

/**
 * 
 * @author juliansmith
 *
 */
public class Log {
  private static final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss.zzz");
  
  public enum Level {
    trace, debug, info, warning, error, severe
  }

  class Context {
    final StackTraceElement element = new Throwable().getStackTrace()[3];
    final Calendar calendar = GregorianCalendar.getInstance();
    
    String getTime() {
      return df.format(this.calendar.getTime());
    }
    
    String getPackage() {
      return this.element.getClassName();
    }
    
    String getMethod() {
      return this.element.getMethodName();
    }
    
    int getLine() {
      return this.element.getLineNumber();
    }
  }
  
  private final static ObjectMapper json = new ObjectMapper()
      .enable(SerializationFeature.WRAP_EXCEPTIONS)
      .enable(SerializationFeature.INDENT_OUTPUT)
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  
  final static ObjectMapper yaml = new ObjectMapper(new YAMLFactory())
      .configure(SerializationFeature.INDENT_OUTPUT, true)
      .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
      .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  private final Class<?> clazz;
  
  public Log(final Class<?> cls) {
    this.clazz = cls;
  }
  
  public void log(final Level level, final String message) {
    final Context context = new Context();
    
    System.out.printf("%s %s [%s] {%s} %s.%s(%d) %s\n", 
        context.getTime(), 
        level.toString().toUpperCase(),
        Thread.currentThread().getName(), 
        this.clazz.getSimpleName(), 
        context.getPackage(), 
        context.getMethod(),
        context.getLine(), 
        message);
  }
  
  public void json(final Object obj) {
    try {
      log(Level.trace, json.writeValueAsString(obj));
    }
    catch (final Throwable e) {
      exception(e);
    }
  }
  
  public void yaml(final Object obj) {
    try {
      log(Level.trace, yaml.writeValueAsString(obj));
    }
    catch (final Throwable e) {
      exception(e);
    }
  }
  
  public void in() {
    log(Level.trace, ">>>");
  }

  public void me(final Object self) {
    array(Level.trace, self.getClass().getSimpleName(), Integer.toHexString(self.hashCode()));
  }
  
  public void out() {
    log(Level.trace, "<<<");
  }
  
  public void exception(final Throwable t) {
    t.printStackTrace(System.err);

    final StackTraceElement frame = t.getStackTrace()[0];

    array(Level.severe,
            t.getClass().getName(),
            t.getLocalizedMessage(),
            frame.getFileName(),
            frame.getClassName(),
            frame.getMethodName(),
            frame.getLineNumber());
  }

  public void array(final Level level, final Object... params) {

    log(level, Arrays.stream(params).map(Object::toString).collect(Collectors.joining(" | ")));
  }
}
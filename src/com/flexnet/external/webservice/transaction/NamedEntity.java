package com.flexnet.external.webservice.transaction;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class NamedEntity extends TimedEntity {
  @JsonInclude
  protected final String id = UUID.randomUUID().toString();
  @JsonInclude
  protected final String name;

  public NamedEntity(final String name) {
    this.name = name;
  }
}

package com.peshchuk.arrival.websocket;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.ToString;

@ToString
public class Payload {

  private Map<String, Object> properties = new HashMap<>();

  @JsonCreator
  private Payload() {
  }

  public Payload(Map<String, Object> properties) {
    this.properties.putAll(properties);
  }

  @JsonAnySetter
  private void setProperties(String name, Object value) {
    properties.put(name, value);
  }

  @JsonAnyGetter
  private Map<String, Object> getProperties() {
    return properties;
  }

  public Object get(String property) {
    return properties.get(property);
  }
}

package com.peshchuk.arrival.websocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Event {

  public enum Type {
    CREATE_TRACK, CREATED_TRACK, CREATED_CAR
  }

  private static AtomicInteger ID_GENERATOR = new AtomicInteger(0);

  private final int id = ID_GENERATOR.addAndGet(1);
  private final long timestamp = System.currentTimeMillis();
  private final Type type;
  private Payload payload;

  @JsonCreator
  public Event(@JsonProperty("type") Type type, @JsonProperty("payload") Payload payload) {
    this.type = type;
    this.payload = payload;
  }

  public static EventBuilder type(Type type) {
    return new EventBuilder().type(type);
  }
}

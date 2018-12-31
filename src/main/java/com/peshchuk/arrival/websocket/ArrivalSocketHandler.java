package com.peshchuk.arrival.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

public class ArrivalSocketHandler implements WebSocketHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ArrivalSocketHandler.class);

  private final UnicastProcessor<Event> eventPublisher;
  private final Flux<String> outputEvents;
  private final ObjectMapper mapper;

  public ArrivalSocketHandler(UnicastProcessor<Event> eventPublisher, Flux<Event> events) {
    this.eventPublisher = eventPublisher;
    this.outputEvents = Flux.from(events).log().map(this::toJson);
    this.mapper = new ObjectMapper();
  }

  @Override
  public Mono<Void> handle(WebSocketSession session) {
    final var input = session.receive().log()
        .map(WebSocketMessage::getPayloadAsText)
        .map(this::toEvent)
        .doOnNext(eventPublisher::onNext)
        .doOnError(error -> LOG.error("onError", error))
        .doOnComplete(() -> LOG.error("onComplete"))
        .then();
    final var output = session.send(outputEvents.map(session::textMessage)).log();

    return Mono.zip(input, output).then();
  }

  private Event toEvent(String json) {
    try {
      return mapper.readValue(json, Event.class);
    } catch (IOException e) {
      throw new RuntimeException("Invalid JSON:" + json, e);
    }
  }

  private String toJson(Event event) {
    try {
      return mapper.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
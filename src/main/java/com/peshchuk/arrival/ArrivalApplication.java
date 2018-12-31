package com.peshchuk.arrival;

import static org.springframework.web.reactive.function.BodyInserters.fromResource;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.peshchuk.arrival.entity.Car;
import com.peshchuk.arrival.entity.Track;
import com.peshchuk.arrival.spring.CarRepository;
import com.peshchuk.arrival.spring.TrackRepository;
import com.peshchuk.arrival.websocket.ArrivalSocketHandler;
import com.peshchuk.arrival.websocket.Event;
import com.peshchuk.arrival.websocket.TrackCarDbHandler;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@SpringBootApplication
public class ArrivalApplication {

  private static final Logger LOG = LoggerFactory.getLogger(ArrivalApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(ArrivalApplication.class, args);
  }

  @Bean
  public CommandLineRunner logDb(TrackRepository trackRepository, CarRepository carRepository) {
    return (args) -> {
      findAll(trackRepository, "Tracks");
      findAll(carRepository, "Cars");
    };
  }

  private <T> void findAll(ReactiveCrudRepository<T, Long> repository, String entityName) {
    LOG.info(entityName + " found with findAll():");
    LOG.info("-------------------------------");

    for (T entity : repository.findAll().toIterable()) {
      LOG.info(entity.toString());
    }

    LOG.info("-------------------------------");
  }

  @Bean
  public RouterFunction<ServerResponse> routes() {
    return RouterFunctions.route(GET("/"),
        request -> ok().body(fromResource(new ClassPathResource("public/index.html")))
    );
  }

  @Bean
  public UnicastProcessor<Event> eventPublisher() {
    return UnicastProcessor.create();
  }

  @Bean
  public Flux<Event> events(UnicastProcessor<Event> eventPublisher) {
    return eventPublisher.replay().autoConnect();
  }

  @Bean
  public HandlerMapping webSocketMapping(UnicastProcessor<Event> eventPublisher,
      Flux<Event> events) {
    final Map<String, Object> urlMap = new HashMap<>();
    urlMap.put("/websocket/arrival", new ArrivalSocketHandler(eventPublisher, events));

    final var simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
    simpleUrlHandlerMapping.setUrlMap(urlMap);

    // Omitting the order causes the mapping to clash with the RouterFunction configuration that
    // deals with HTTP requests
    simpleUrlHandlerMapping.setOrder(10);

    return simpleUrlHandlerMapping;
  }

  @Bean
  public WebSocketHandlerAdapter handlerAdapter() {
    return new WebSocketHandlerAdapter();
  }

  @Bean
  public TrackCarDbHandler trackCarDbHandler(TrackRepository trackRepository,
      CarRepository carRepository, UnicastProcessor<Event> eventPublisher, Flux<Event> events) {
    return new TrackCarDbHandler(trackRepository, carRepository, eventPublisher, events);
  }
}


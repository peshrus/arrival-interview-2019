package com.peshchuk.arrival.websocket;

import static com.peshchuk.arrival.websocket.Event.Type.CREATED_CAR;
import static com.peshchuk.arrival.websocket.Event.Type.CREATED_TRACK;
import static com.peshchuk.arrival.websocket.Event.Type.CREATE_TRACK;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

import com.peshchuk.arrival.entity.Car;
import com.peshchuk.arrival.entity.LengthUnit;
import com.peshchuk.arrival.entity.SpeedUnit;
import com.peshchuk.arrival.entity.Track;
import com.peshchuk.arrival.entity.TransmissionType;
import com.peshchuk.arrival.spring.CarRepository;
import com.peshchuk.arrival.spring.TrackRepository;
import com.peshchuk.arrival.websocket.Event.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

/**
 * @author Ruslan Peshchuk (peshrus@gmail.com)
 */
public class TrackCarDbHandler {

  private static final Logger LOG = LoggerFactory.getLogger(TrackCarDbHandler.class);

  private static final String PAYLOAD_TRACK = "track";
  private static final String PAYLOAD_CAR = "car";

  private static final String PROP_NAME = "name";
  private static final String PROP_DESCRIPTION = "description";
  private static final String PROP_LENGTH = "length";
  private static final String PROP_UNIT = "unit";
  private static final String PROP_VALUE = "value";
  private static final String PROP_CARS = "cars";
  private static final String PROP_CODE = "code";
  private static final String PROP_TRANSMISSION = "transmission";
  private static final String PROP_AI = "ai";
  private static final String PROP_MAX_SPEED = "maxSpeed";

  private static final String AI_ENABLED = "enabled";

  private final TrackRepository trackRepository;
  private final CarRepository carRepository;
  private final UnicastProcessor<Event> eventPublisher;

  public TrackCarDbHandler(TrackRepository trackRepository, CarRepository carRepository,
      UnicastProcessor<Event> eventPublisher, Flux<Event> events) {
    this.trackRepository = trackRepository;
    this.carRepository = carRepository;
    this.eventPublisher = eventPublisher;

    trackRepository.findAll().log().map(this::createdTrack).subscribe(eventPublisher::onNext);
    carRepository.findAll().log().map(this::createdCar)
        .subscribe(eventPublisher::onNext, this::onError);

    events.log().filter(type(CREATE_TRACK)).subscribe(this::onCreateTrack, this::onError);
  }

  private Event createdTrack(Track track) {
    return new Event(CREATED_TRACK, new Payload(singletonMap(PAYLOAD_TRACK, track.makeVo())));
  }

  private Event createdCar(Car car) {
    return new Event(CREATED_CAR, new Payload(singletonMap(PAYLOAD_CAR, car.makeVo())));
  }

  private static Predicate<Event> type(Type... types) {
    return event -> asList(types).contains(event.getType());
  }

  private void onError(Throwable throwable) {
    LOG.error("onError", throwable);
  }

  @SuppressWarnings("unchecked")
  private void onCreateTrack(Event event) {
    if (CREATE_TRACK == event.getType()) {
      // TODO proper JSON handling
      final var trackPropsMap = (Map<String, Object>) event.getPayload().get(PAYLOAD_TRACK);
      final var track = makeTrack(trackPropsMap);

      trackRepository.save(track).log().subscribe(savedTrack -> {
        eventPublisher.onNext(createdTrack(savedTrack));

        final var carsPropsList = (List<Map>) trackPropsMap.get(PROP_CARS);

        for (Map<String, Object> carPropsMap : carsPropsList) {
          final var car = makeCar(carPropsMap, savedTrack);

          carRepository.save(car).log().map(this::createdCar)
              .subscribe(eventPublisher::onNext, this::onError);
        }
      }, this::onError);
    }
  }

  @SuppressWarnings("unchecked")
  private Track makeTrack(Map<String, Object> trackPropsMap) {
    final var name = (String) trackPropsMap.get(PROP_NAME);
    final var description = (String) trackPropsMap.get(PROP_DESCRIPTION);
    final var lengthPropsMap = (Map<String, Object>) trackPropsMap.get(PROP_LENGTH);
    final var lengthUnit = LengthUnit.valueOf((String) lengthPropsMap.get(PROP_UNIT));
    final var lengthValue = Double.parseDouble((String) lengthPropsMap.get(PROP_VALUE));

    return new Track(name, description, lengthUnit, lengthValue);
  }

  @SuppressWarnings("unchecked")
  private Car makeCar(Map<String, Object> carPropsMap, Track savedTrack) {
    final var code = (String) carPropsMap.get(PROP_CODE);
    final var transmission = TransmissionType.valueOf((String) carPropsMap.get(PROP_TRANSMISSION));
    final var ai = AI_ENABLED.equals(carPropsMap.get(PROP_AI));
    final var maxSpeed = (Map<String, Object>) carPropsMap.get(PROP_MAX_SPEED);
    final var maxSpeedUnit = SpeedUnit.valueOf((String) maxSpeed.get(PROP_UNIT));
    final var maxSpeedValue = Double.parseDouble((String) maxSpeed.get(PROP_VALUE));

    return new Car(savedTrack.getId(), code, transmission, ai, maxSpeedUnit, maxSpeedValue);
  }
}

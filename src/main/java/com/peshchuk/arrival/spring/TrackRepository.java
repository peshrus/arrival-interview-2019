package com.peshchuk.arrival.spring;

import com.peshchuk.arrival.entity.Track;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author Ruslan Peshchuk (peshrus@gmail.com)
 */
public interface TrackRepository extends ReactiveCrudRepository<Track, Long> {

}
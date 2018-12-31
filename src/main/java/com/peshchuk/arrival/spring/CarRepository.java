package com.peshchuk.arrival.spring;

import com.peshchuk.arrival.entity.Car;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author Ruslan Peshchuk (peshrus@gmail.com)
 */
public interface CarRepository extends ReactiveCrudRepository<Car, Long> {

}

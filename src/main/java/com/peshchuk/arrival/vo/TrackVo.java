package com.peshchuk.arrival.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Ruslan Peshchuk (peshrus@gmail.com)
 */
@AllArgsConstructor
@ToString
@Getter
@Setter
public class TrackVo {
  private final Integer id;
  private final String name;
  private final String description;
  private final LengthVo length;
  private final List<CarVo> cars;
}

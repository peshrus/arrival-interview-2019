package com.peshchuk.arrival.vo;

import com.peshchuk.arrival.entity.LengthUnit;
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
public class LengthVo {
  private final LengthUnit unit;
  private final double value;
}

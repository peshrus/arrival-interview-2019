package com.peshchuk.arrival.vo;

import com.peshchuk.arrival.entity.SpeedUnit;
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
public class SpeedVo {
  private SpeedUnit unit;
  private double value;
}

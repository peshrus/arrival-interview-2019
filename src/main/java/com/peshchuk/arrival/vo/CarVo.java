package com.peshchuk.arrival.vo;

import com.peshchuk.arrival.entity.TransmissionType;
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
public class CarVo {
  private Integer id;
  private Integer trackId;
  private String code;
  private TransmissionType transmission;
  private String ai;
  private SpeedVo maxSpeed;
}

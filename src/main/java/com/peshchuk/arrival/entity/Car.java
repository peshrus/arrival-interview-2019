package com.peshchuk.arrival.entity;

import com.peshchuk.arrival.vo.CarVo;
import com.peshchuk.arrival.vo.SpeedVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

/**
 * @author Ruslan Peshchuk (peshrus@gmail.com)
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Car {

  @Id
  private Integer id;
  private Integer trackId;
  private String code;
  private TransmissionType transmission;
  private boolean ai;
  private SpeedUnit maxSpeedUnit;
  private double maxSpeedValue;

  public Car(Integer trackId, String code, TransmissionType transmission, boolean ai,
      SpeedUnit maxSpeedUnit, double maxSpeedValue) {
    this.trackId = trackId;
    this.code = code;
    this.transmission = transmission;
    this.ai = ai;
    this.maxSpeedUnit = maxSpeedUnit;
    this.maxSpeedValue = maxSpeedValue;
  }

  public CarVo makeVo() {
    return new CarVo(id, trackId, code, transmission, ai ? "enabled" : "disabled",
        new SpeedVo(maxSpeedUnit, maxSpeedValue));
  }
}

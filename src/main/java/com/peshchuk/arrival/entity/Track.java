package com.peshchuk.arrival.entity;

import static java.util.Collections.emptyList;

import com.peshchuk.arrival.vo.LengthVo;
import com.peshchuk.arrival.vo.TrackVo;
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
public class Track {

  @Id
  private Integer id;
  private String name;
  private String description;
  private LengthUnit lengthUnit;
  private double lengthValue;

  public Track(String name, String description, LengthUnit lengthUnit, double lengthValue) {
    this.name = name;
    this.description = description;
    this.lengthUnit = lengthUnit;
    this.lengthValue = lengthValue;
  }

  public TrackVo makeVo() {
    return new TrackVo(id, name, description, new LengthVo(lengthUnit, lengthValue), emptyList());
  }
}

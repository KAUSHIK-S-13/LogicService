package com.coherent.unnamed.logic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailsDTO {

  public int date;
  public long hours;
  private List<LoggedDetailsDTO> logs;

}

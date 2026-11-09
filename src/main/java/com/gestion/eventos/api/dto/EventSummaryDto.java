package com.gestion.eventos.api.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EventSummaryDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDto {

  private Long id;
  private String name;
  private LocalDate date;
  private String location;
}

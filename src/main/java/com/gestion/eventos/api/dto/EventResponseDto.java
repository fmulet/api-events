package com.gestion.eventos.api.dto;

import com.gestion.eventos.api.domain.Category;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

/**
 * EventResponseDto
 */

@Data
public class EventResponseDto {

  private Long id;
  private String name;
  private LocalDate date;
  private String location;
  private Category category;
  private List<SpeakerDto> speakersDtos;
}

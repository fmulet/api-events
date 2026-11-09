package com.gestion.eventos.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SpeakerDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerDto {

  private Long id;
  private String name;
  private String email;
  private String bio;
}

package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

/**
 * EventRequestDto
 */

@Data
@Schema(description = "Detalles de la solicitud para crear/actualizar un evento")
public class EventRequestDto {

  @NotBlank(message = "Name is required")
  private String name;

  @NotNull(message = "Date is required")
  private LocalDate date;

  @NotBlank(message = "Location is required")
  private String location;

  @NotNull(message = "Category is required")
  private Long categoryId;

  private Set<Long> speakersIds;
}

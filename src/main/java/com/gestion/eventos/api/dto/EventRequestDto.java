package com.gestion.eventos.api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * EventRequestDto
 */

@Data
public class EventRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Location is required")
    private String location;

}

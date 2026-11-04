package com.gestion.eventos.api.dto;

import java.time.LocalDate;

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
}

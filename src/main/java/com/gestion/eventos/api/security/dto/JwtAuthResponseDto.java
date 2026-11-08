package com.gestion.eventos.api.security.dto;

import lombok.Data;

/**
 * JwtAuthResponseDto
 */
@Data
public class JwtAuthResponseDto {

  private String accessToken;
  private String tokenType = "Bearer ";

  public JwtAuthResponseDto(String token) {
    this.accessToken = token;
  }
}

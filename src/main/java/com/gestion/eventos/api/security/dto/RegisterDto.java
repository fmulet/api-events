package com.gestion.eventos.api.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegisterDto
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

  @NotBlank(message = "Username is required")
  @Size(
    min = 4,
    max = 20,
    message = "Username must be between 4 and 20 characters"
  )
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

  @NotBlank(message = "Email is required")
  @Email(message = "Email is not valid")
  private String email;

  @NotBlank(message = "Name is required")
  private String name;

  private Set<String> roles;
}

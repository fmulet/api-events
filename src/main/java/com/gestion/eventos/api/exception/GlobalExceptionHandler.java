package com.gestion.eventos.api.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(
    MethodArgumentNotValidException ex
  ) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
      .getFieldErrors()
      .forEach(error -> {
        errors.put(error.getField(), error.getDefaultMessage());
      });

    Map<String, Object> body = new HashMap<>();

    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Bad Request");
    body.put("message", "Validation error");
    body.put("errors", errors);

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(
    ResourceNotFoundException ex
  ) {
    Map<String, Object> body = new HashMap<>();

    body.put("status", HttpStatus.NOT_FOUND.value());
    body.put("error", "Not Found");
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
    DataIntegrityViolationException ex
  ) {
    Map<String, String> errorDetails = new HashMap<>();

    errorDetails.put("error", "Data conflict");
    errorDetails.put(
      "message",
      "No complete operation was fermored due to a data conflict."
    );
    errorDetails.put(
      "details",
      ex.getRootCause() != null
        ? ex.getRootCause().getMessage()
        : "Data integrity violation."
    );

    return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
    System.out.println("Error: " + ex.getMessage());
    ex.printStackTrace();

    Map<String, String> errorDetails = new HashMap<>();

    errorDetails.put("error", "Internal Server Error");
    errorDetails.put("message", "An unexpected error occurred.");

    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

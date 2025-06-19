package com.example.declarations.tracking.customs_declaration_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(DeclarationNotFoundException.class)
  public ResponseEntity<String> handleDeclarationNotFound(DeclarationNotFoundException ex) {
    log.warn("Декларация не найдена: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
    log.warn("Неверный запрос: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericError(Exception ex) {
    log.error("Внутренняя ошибка сервера", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла внутренняя ошибка сервера");
  }
}
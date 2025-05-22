package fpc.app.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {
  private static final String DEFAULT_ERROR_MESSAGE = "Ocurrió un error inesperado";

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex) {
    log.error("No handled exception: {}", ex.getMessage());
    ErrorDetails errorDetails =
        ErrorDetails.builder()
            .timestamp(LocalDateTime.now())
            .message(DEFAULT_ERROR_MESSAGE)
            .details(ex.getMessage())
            .build();
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({DataNotFoundException.class})
  public ResponseEntity<ErrorDetails> handleResourceNotFoundException(DataNotFoundException ex) {
    log.error("Object or resource not found: {}", ex.getMessage());
    ErrorDetails errorDetails =
        new ErrorDetails(LocalDateTime.now(), "No se encontró el recurso", ex.getMessage());
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({TechnicalException.class})
  public ResponseEntity<ErrorDetails> handleTechnicalException(TechnicalException ex) {
    log.error("Technical exception: {}", ex.toString());
    ErrorDetails errorDetails =
        new ErrorDetails(LocalDateTime.now(), DEFAULT_ERROR_MESSAGE, "Error técnico");
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({NoSuchElementException.class})
  public ResponseEntity<ErrorDetails> handleNoSuchElementException(NoSuchElementException ex) {
    log.error("Invalid object access: {}", ex.getMessage());
    ErrorDetails errorDetails =
        new ErrorDetails(LocalDateTime.now(), DEFAULT_ERROR_MESSAGE, "Error interno");
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({NullPointerException.class})
  public ResponseEntity<ErrorDetails> handleNullPointerException(NullPointerException ex) {
    log.error("Null pointer: {}", ex.getMessage());
    ErrorDetails errorDetails =
        new ErrorDetails(LocalDateTime.now(), DEFAULT_ERROR_MESSAGE, ex.getMessage());
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ValidationException.class})
  public ResponseEntity<ErrorDetails> handleValidationException(ValidationException ex) {
    log.error("Validation error: {}", ex.getMessage());
    ErrorDetails errorDetails =
        new ErrorDetails(LocalDateTime.now(), "No se puede procesar la solicitud", ex.getMessage());
    return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({BadCredentialsException.class})
  public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException ex) {
    log.error("Bad credentials: {}", ex.getMessage());
    ErrorDetails errorDetails =
        new ErrorDetails(
            LocalDateTime.now(),
            "Usuario o contraseña incorrectos",
            "La combinación de usuario y contraseña no es válida");
    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<InputDataError> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.error("Method argument not valid: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    InputDataError inputDataError =
        InputDataError.builder()
            .timestamp(LocalDateTime.now())
            .message(DEFAULT_ERROR_MESSAGE)
            .details("La petición no cumple con las validaciones")
            .errors(errors)
            .build();

    return new ResponseEntity<>(inputDataError, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<InputDataError> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.error("Bad request: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();

    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String fieldName = violation.getPropertyPath().toString();
      String errorMessage = violation.getMessage();
      errors.put(fieldName, errorMessage);
    }

    InputDataError errorResponse = new InputDataError();
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setMessage(DEFAULT_ERROR_MESSAGE);
    errorResponse.setDetails("La petición contiene campos no válidos");
    errorResponse.setErrors(errors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException ex) {
    log.error("Access denied: {}", ex.getMessage());

    ErrorDetails errorDetails =
        new ErrorDetails(LocalDateTime.now(), DEFAULT_ERROR_MESSAGE, "Acceso denegado");
    return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
  }
}

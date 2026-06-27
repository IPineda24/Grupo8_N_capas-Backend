package com.uca.telemedicina.exception;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiError.builder().status(404).error("Not Found")
                .message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
    }
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessRuleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiError.builder().status(400).error("Business Rule Violation")
                .message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
    }
    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<ApiError> handleConflict(AppointmentConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiError.builder().status(409).error("Appointment Conflict")
                .message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiError.builder().status(400).error("Validation Error")
                .message(msg).timestamp(LocalDateTime.now()).build());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiError.builder().status(403).error("Forbidden")
                .message("No tienes permisos para realizar esta acción").timestamp(LocalDateTime.now()).build());
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuth(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiError.builder().status(401).error("Unauthorized")
                .message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError.builder().status(500).error("Internal Server Error")
                .message(ex.getMessage()).timestamp(LocalDateTime.now()).build());
    }
}

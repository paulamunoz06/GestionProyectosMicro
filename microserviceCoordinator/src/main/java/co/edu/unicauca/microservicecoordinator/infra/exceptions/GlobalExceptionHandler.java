package co.edu.unicauca.microservicecoordinator.infra.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<String> handleInvalidState(InvalidStateTransitionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

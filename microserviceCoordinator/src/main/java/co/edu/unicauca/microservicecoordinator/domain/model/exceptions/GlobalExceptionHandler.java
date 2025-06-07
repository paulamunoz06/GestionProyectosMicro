package co.edu.unicauca.microservicecoordinator.domain.model.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para la capa de controladores REST.
 *
 * Esta clase proporciona un punto centralizado para capturar y gestionar excepciones específicas
 * lanzadas durante la ejecución de controladores, permitiendo retornar respuestas HTTP personalizadas
 * sin repetir lógica en cada controlador.
 *
 * Utiliza la anotación RestControllerAdvice de Spring para aplicar el manejo de excepciones
 * a todos los controladores REST del sistema.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones del tipo InvalidStateTransitionException.
     *
     * Esta excepción se lanza cuando se intenta aplicar una transición de estado no válida
     * sobre un proyecto, de acuerdo con la lógica de negocio del dominio.
     *
     * @param ex Excepción capturada del tipo InvalidStateTransitionException.
     * @return Una respuesta HTTP con código 400 (Bad Request) y el mensaje de error como cuerpo.
     */
    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<String> handleInvalidState(InvalidStateTransitionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
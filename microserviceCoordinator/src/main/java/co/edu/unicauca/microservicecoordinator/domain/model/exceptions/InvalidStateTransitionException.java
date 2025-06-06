package co.edu.unicauca.microservicecoordinator.domain.model.exceptions;

/**
 * Excepción lanzada cuando se intenta realizar una transición de estado no permitida en un proyecto.
 *
 * Esta clase forma parte de la lógica de validación del dominio. Se utiliza para señalar que se ha intentado
 * aplicar un cambio de estado inválido, ya sea porque el estado destino no existe, no está permitido desde el
 * estado actual, o no respeta las reglas definidas en el modelo de dominio.
 *
 * Extiende de RuntimeException, por lo que es una excepción no verificada.
 */
public class InvalidStateTransitionException extends RuntimeException {

    /**
     * Construye una nueva excepción con el mensaje de error especificado.
     *
     * @param message Mensaje que describe el motivo de la excepción.
     */
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}

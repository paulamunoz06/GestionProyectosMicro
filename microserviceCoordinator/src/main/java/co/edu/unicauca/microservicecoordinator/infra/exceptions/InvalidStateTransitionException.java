package co.edu.unicauca.microservicecoordinator.infra.exceptions;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}

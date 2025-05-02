package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.infra.exceptions.InvalidStateTransitionException;

public class ClosedState implements ProjectState {
    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("No se puede recibir un proyecto cerrado.");
    }

    @Override
    public void reject(Project project) {
        throw new InvalidStateTransitionException("No se puede rechazar un proyecto cerrado.");
    }

    @Override
    public void accept(Project project) {
        throw new InvalidStateTransitionException("No se puede aceptar un proyecto cerrado.");
    }

    @Override
    public void close(Project project) {
        throw new InvalidStateTransitionException("El proyecto ya se cerró.");
    }

    @Override
    public void execute(Project project) {
        throw new InvalidStateTransitionException("No se puede ejecutar un proyecto cerrado.");
    }

    @Override
    public String toString() {
        return "ClosedState";
    }
}


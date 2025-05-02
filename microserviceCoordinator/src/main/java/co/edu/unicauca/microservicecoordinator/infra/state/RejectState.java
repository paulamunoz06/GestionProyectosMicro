package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.infra.exceptions.InvalidStateTransitionException;

public class RejectState implements ProjectState {
    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("No se puede recibir un proyecto rechazado.");
    }

    @Override
    public void reject(Project project) {
        System.out.println("Project is already rejected.");
    }

    @Override
    public void accept(Project project) {
        throw new InvalidStateTransitionException("No se puede aceptar un proyecto rechazado.");
    }

    @Override
    public void close(Project project) {
        throw new InvalidStateTransitionException("No se puede completar un proyecto rechazado.");
    }

    @Override
    public void execute(Project project) {
        throw new InvalidStateTransitionException("No se puede ejecutar un proyecto rechazado.");
    }

    @Override
    public String toString() {
        return "RejectState";
    }
}

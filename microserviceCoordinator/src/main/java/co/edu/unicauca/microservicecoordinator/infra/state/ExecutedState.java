package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.infra.exceptions.InvalidStateTransitionException;

public class ExecutedState implements ProjectState {
    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("No se puede recibir un proyecto en ejecución.");
    }

    @Override
    public void reject(Project project) {
        throw new InvalidStateTransitionException("No se puede rechazar un proyecto en ejecución.");
    }

    @Override
    public void accept(Project project) {
        throw new InvalidStateTransitionException("No se puede aceptar un proyecto que está en ejecución.");
    }

    @Override
    public void close(Project project) {
        project.setProState(EnumProjectState.CERRADO);
        project.setCurrentState(new ClosedState());
        System.out.println("Proyecto cerrado correctamente.");
    }

    @Override
    public void execute(Project project) {
        throw new InvalidStateTransitionException("El proyecto ya se encuentra en ejecución.");
    }

    @Override
    public String toString() {
        return "ExecutedState";
    }
}

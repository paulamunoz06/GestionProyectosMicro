package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.infra.exceptions.InvalidStateTransitionException;

public class ReceivedState implements ProjectState {

    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("El proyecto ya fue recibido");
    }

    @Override
    public void reject(Project project) {
        project.setProState(EnumProjectState.RECHAZADO);
        project.setCurrentState(new RejectState());
        System.out.println("Project rejected.");
    }

    @Override
    public void accept(Project project) {
        project.setProState(EnumProjectState.ACEPTADO);
        project.setCurrentState(new AcceptedState());
        System.out.println("Project assigned to a student.");
    }

    @Override
    public void close(Project project) {
        throw new InvalidStateTransitionException("No se puede cerrar un proyecto que no se ha aceptado.");
    }

    @Override
    public void execute(Project project) {
        throw new InvalidStateTransitionException("No se puede ejecutar un proyecto que no se ha aceptado.");
    }

    @Override
    public String toString() {
        return "ReceivedState";
    }
}

package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;

public class ProjectStateFactory {
    public static ProjectState getState(Project project) {
        switch (project.getProState()) {
            case RECIBIDO: return new ReceivedState();
            case ACEPTADO: return new AcceptedState();
            case RECHAZADO: return new RejectState();
            case EJECUCION: return new ExecutedState();
            case CERRADO: return new ClosedState();
            default: throw new IllegalArgumentException("Estado no válido");
        }
    }
}


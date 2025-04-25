package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;

public class ProjectStateFactory {
    public static ProjectState getState(Project project) {
        switch (project.getProStatus()) {
            case PENDING: return new PendingState();
            case ASSIGNED: return new AssignedState();
            case APPROVED: return new ApprovedState();
            case REJECTED: return new RejectState();
            case COMPLETED: return new CompletedState();
            default: throw new IllegalArgumentException("Estado no válido");
        }
    }
}


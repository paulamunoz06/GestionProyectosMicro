package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.ProjectStatus;

public class PendingState implements ProjectState {
    @Override
    public void approve(Project project) {
        project.setProStatus(ProjectStatus.APPROVED);
        project.setCurrentState(new ApprovedState());
        System.out.println("Project approved.");
    }

    @Override
    public void reject(Project project) {
        project.setProStatus(ProjectStatus.REJECTED);
        project.setCurrentState(new RejectState());
        System.out.println("Project rejected.");
    }

    @Override
    public void assign(Project project) {
        System.out.println("Cannot assign a project that is pending approval.");
    }

    @Override
    public void complete(Project project) {
        System.out.println("Cannot complete a pending project.");
    }

    @Override
    public String toString() {
        return "PendingState";
    }
}
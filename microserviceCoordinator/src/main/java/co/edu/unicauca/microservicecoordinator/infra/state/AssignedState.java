package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.ProjectStatus;

public class AssignedState implements ProjectState {
    @Override
    public void approve(Project project) {
        System.out.println("Project is already assigned, cannot approve again.");
    }

    @Override
    public void reject(Project project) {
        System.out.println("Assigned project cannot be rejected.");
    }

    @Override
    public void assign(Project project) {
        System.out.println("Project is already assigned.");
    }

    @Override
    public void complete(Project project) {
        project.setProStatus(ProjectStatus.COMPLETED);
        project.setCurrentState(new CompletedState());
        System.out.println("Project completed!");
    }

    @Override
    public String toString() {
        return "AssignedState";
    }
}

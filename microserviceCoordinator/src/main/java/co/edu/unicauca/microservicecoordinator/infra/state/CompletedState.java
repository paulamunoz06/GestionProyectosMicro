package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;

public class CompletedState implements ProjectState {
    @Override
    public void approve(Project project) {
        System.out.println("Completed project cannot be approved.");
    }

    @Override
    public void reject(Project project) {
        System.out.println("Completed project cannot be rejected.");
    }

    @Override
    public void assign(Project project) {
        System.out.println("Completed project cannot be assigned.");
    }

    @Override
    public void complete(Project project) {
        System.out.println("Project has already been completed.");
    }

    @Override
    public String toString() {
        return "CompletedState";
    }
}


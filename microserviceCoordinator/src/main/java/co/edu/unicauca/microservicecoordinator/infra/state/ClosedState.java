package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;

public class ClosedState implements ProjectState {
    @Override
    public void receive(Project project) {
        System.out.println("Completed project cannot be approved.");
    }

    @Override
    public void reject(Project project) {
        System.out.println("Completed project cannot be rejected.");
    }

    @Override
    public void accept(Project project) {
        System.out.println("Completed project cannot be assigned.");
    }

    @Override
    public void close(Project project) {
        System.out.println("Project has already been completed.");
    }

    @Override
    public void execute(Project project) {
        System.out.println("Project has been closed.");
    }

    @Override
    public String toString() {
        return "ClosedState";
    }
}


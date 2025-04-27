package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;

public class RejectState implements ProjectState {
    @Override
    public void receive(Project project) {
        System.out.println("Cannot approve a rejected project.");
    }

    @Override
    public void reject(Project project) {
        System.out.println("Project is already rejected.");
    }

    @Override
    public void accept(Project project) {
        System.out.println("Cannot assign a rejected project.");
    }

    @Override
    public void close(Project project) {
        System.out.println("Cannot complete a rejected project.");
    }

    @Override
    public void execute(Project project) {
        System.out.println("Cannot execute a rejected project.");
    }

    @Override
    public String toString() {
        return "RejectState";
    }
}

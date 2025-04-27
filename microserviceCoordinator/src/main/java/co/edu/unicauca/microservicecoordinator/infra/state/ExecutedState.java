package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;

public class ExecutedState implements ProjectState {
    @Override
    public void receive(Project project) {
        System.out.println("Cannot receive a running project.");
    }

    @Override
    public void reject(Project project) {
        System.out.println("Cannot reject a running project.");
    }

    @Override
    public void accept(Project project) {
        System.out.println("Cannot accept a project that is running.");
    }

    @Override
    public void close(Project project) {
        project.setProState(EnumProjectState.CERRADO);
        project.setCurrentState(new ClosedState());
        System.out.println("Project Completed.");
    }

    @Override
    public void execute(Project project) {
        System.out.println("Project is al ready running.");
    }

    @Override
    public String toString() {
        return "ExecutedState";
    }
}
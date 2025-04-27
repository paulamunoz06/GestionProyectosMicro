package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;

public class AcceptedState implements ProjectState {
    @Override
    public void receive(Project project) {
        System.out.println("Project is already accepted, cannot receive again.");
    }

    @Override
    public void reject(Project project) {
        System.out.println("Accepted project cannot be rejected.");
    }

    @Override
    public void accept(Project project) {
        System.out.println("Project is already accepted.");
    }

    @Override
    public void close(Project project) {
        System.out.println("Project cannot be closed.");
    }

    @Override
    public void execute(Project project) {
        project.setProState(EnumProjectState.EJECUCION);
        project.setCurrentState(new ExecutedState());
        System.out.println("Project in executed state.");
    }

    @Override
    public String toString() {
        return "AcceptedState";
    }
}

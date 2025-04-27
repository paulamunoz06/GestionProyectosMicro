package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;

public class ReceivedState implements ProjectState {

    @Override
    public void receive(Project project) {
        System.out.println("Project is already received.");
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
        System.out.println("Cannot complete a project that has not been accepted.");
    }

    @Override
    public void execute(Project project) {
        System.out.println("Cannot complete a project that has not been accepted.");
    }

    @Override
    public String toString() {
        return "ReceivedState";
    }
}

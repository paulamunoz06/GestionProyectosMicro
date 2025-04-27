package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;

/**
 * Interface que maneja el ciclo de vida del proyecto
 */
public interface ProjectState {
    void receive(Project project);

    void reject(Project project);

    void accept(Project project);

    void close(Project project);

    void execute(Project project);
}

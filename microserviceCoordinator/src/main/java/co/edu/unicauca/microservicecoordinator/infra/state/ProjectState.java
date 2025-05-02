package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.infra.exceptions.InvalidStateTransitionException;

/**
 * Interfaz que define el comportamiento de los estados en el ciclo de vida de un proyecto.
 *
 * <p>Esta interfaz forma parte de la implementación del patrón de diseño State,
 * permitiendo que un proyecto cambie su comportamiento cuando su estado interno cambia.
 * Cada estado concreto implementará esta interfaz y definirá el comportamiento específico
 * para las transiciones posibles desde ese estado.</p>
 * @see Project
 * @see EnumProjectState
 */
public interface ProjectState {

    /**
     * Transiciona el proyecto al estado RECIBIDO.
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Si la transición no es válida desde el estado actual
     */
    void receive(Project project);

    /**
     * Transiciona el proyecto al estado RECHAZADO.
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Si la transición no es válida desde el estado actual
     */
    void reject(Project project);

    /**
     * Transiciona el proyecto al estado ACEPTADO.
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Si la transición no es válida desde el estado actual
     */
    void accept(Project project);

    /**
     * Transiciona el proyecto al estado CERRADO.
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Si la transición no es válida desde el estado actual
     */
    void close(Project project);

    /**
     * Transiciona el proyecto al estado EJECUCION.
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Si la transición no es válida desde el estado actual
     */
    void execute(Project project);
}
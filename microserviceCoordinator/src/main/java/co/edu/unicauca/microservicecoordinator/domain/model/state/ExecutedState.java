package co.edu.unicauca.microservicecoordinator.domain.model.state;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;

/**
 * Implementación del estado "Ejecutado" para el patrón State que gestiona
 * los estados de un proyecto.
 *
 * <p>Esta clase representa el estado cuando un proyecto está en ejecución.
 * Desde este estado, solo se permite la transición al estado "Cerrado".
 * Cualquier otra transición generará una excepción.</p>
 *
 * @see ProjectState
 * @see Project
 * @see EnumProjectState
 */
public class ExecutedState implements ProjectState {

    /**
     * Intenta recibir un proyecto que está en estado de ejecución.
     *
     * <p>Esta transición no está permitida para un proyecto en ejecución.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("No se puede recibir un proyecto en ejecución.");
    }

    /**
     * Intenta rechazar un proyecto que está en estado de ejecución.
     *
     * <p>Esta transición no está permitida para un proyecto en ejecución.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void reject(Project project) {
        throw new InvalidStateTransitionException("No se puede rechazar un proyecto en ejecución.");
    }

    /**
     * Intenta aceptar un proyecto que está en estado de ejecución.
     *
     * <p>Esta transición no está permitida para un proyecto en ejecución.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void accept(Project project) {
        throw new InvalidStateTransitionException("No se puede aceptar un proyecto que está en ejecución.");
    }

    /**
     * Cierra un proyecto que está en estado de ejecución.
     *
     * <p>Cambia el estado del proyecto a "Cerrado" y actualiza el estado actual
     * a una instancia de {@link ClosedState}.</p>
     *
     * @param project El proyecto que se va a cerrar
     */
    @Override
    public void close(Project project) {
        project.changeState(EnumProjectState.CERRADO);
        System.out.println("Proyecto cerrado correctamente.");
    }

    /**
     * Intenta ejecutar un proyecto que ya está en estado de ejecución.
     *
     * <p>Esta transición no está permitida porque el proyecto ya se encuentra en ejecución.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void execute(Project project) {
        throw new InvalidStateTransitionException("El proyecto ya se encuentra en ejecución.");
    }

    /**
     * Devuelve una representación en cadena de texto del estado actual.
     *
     * @return El nombre del estado como cadena de texto
     */
    @Override
    public String toString() {
        return "ExecutedState";
    }
}
package co.edu.unicauca.microservicecoordinator.domain.model.state;

import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;

/**
 * Implementación del estado "Rechazado" para el patrón State que gestiona
 * los estados de un proyecto.
 *
 * <p>Esta clase representa el estado cuando un proyecto ha sido rechazado.
 * Este es un estado terminal desde el cual no se permite ninguna transición
 * a otros estados, excepto intentar rechazarlo nuevamente, lo cual simplemente
 * informa que ya está rechazado.</p>
 *
 * @see ProjectState
 * @see Project
 */
public class RejectState implements ProjectState {

    /**
     * Intenta recibir un proyecto que está en estado rechazado.
     *
     * <p>Esta transición no está permitida para un proyecto ya rechazado.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("No se puede recibir un proyecto rechazado.");
    }

    /**
     * Intenta rechazar un proyecto que ya está en estado rechazado.
     *
     * <p>Esta operación simplemente informa que el proyecto ya se encuentra en estado rechazado
     * y no realiza ningún cambio en el estado del proyecto.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     */
    @Override
    public void reject(Project project) {
        System.out.println("Project is already rejected.");
    }

    /**
     * Intenta aceptar un proyecto que está en estado rechazado.
     *
     * <p>Esta transición no está permitida para un proyecto rechazado.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void accept(Project project) {
        throw new InvalidStateTransitionException("No se puede aceptar un proyecto rechazado.");
    }

    /**
     * Intenta cerrar un proyecto que está en estado rechazado.
     *
     * <p>Esta transición no está permitida para un proyecto rechazado.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void close(Project project) {
        throw new InvalidStateTransitionException("No se puede completar un proyecto rechazado.");
    }

    /**
     * Intenta ejecutar un proyecto que está en estado rechazado.
     *
     * <p>Esta transición no está permitida para un proyecto rechazado.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void execute(Project project) {
        throw new InvalidStateTransitionException("No se puede ejecutar un proyecto rechazado.");
    }

    /**
     * Devuelve una representación en cadena de texto del estado actual.
     *
     * @return El nombre del estado como cadena de texto
     */
    @Override
    public String toString() {
        return "RejectState";
    }
}
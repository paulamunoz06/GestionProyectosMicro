package co.edu.unicauca.microservicecoordinator.infra.state;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.infra.exceptions.InvalidStateTransitionException;

/**
 * Implementación del estado "Recibido" para el patrón State que gestiona
 * los estados de un proyecto.
 *
 * <p>Esta clase representa el estado cuando un proyecto ha sido recibido
 * pero aún no ha sido procesado. Desde este estado, se permite la transición
 * a los estados "Rechazado" o "Aceptado".</p>
 *
 * @see ProjectState
 * @see Project
 * @see EnumProjectState
 */
public class ReceivedState implements ProjectState {

    /**
     * Intenta recibir un proyecto que ya está en estado recibido.
     *
     * <p>Esta transición no está permitida porque el proyecto ya ha sido recibido.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("El proyecto ya fue recibido");
    }

    /**
     * Rechaza un proyecto que está en estado recibido.
     *
     * <p>Cambia el estado del proyecto a "Rechazado" y actualiza el estado actual
     * a una instancia de {@link RejectState}.</p>
     *
     * @param project El proyecto que se va a rechazar
     */
    @Override
    public void reject(Project project) {
        project.setProState(EnumProjectState.RECHAZADO);
        project.setCurrentState(new RejectState());
        System.out.println("Project rejected.");
    }

    /**
     * Acepta un proyecto que está en estado recibido.
     *
     * <p>Cambia el estado del proyecto a "Aceptado" y actualiza el estado actual
     * a una instancia de {@link AcceptedState}. Este estado indica que el proyecto
     * ha sido asignado a un estudiante.</p>
     *
     * @param project El proyecto que se va a aceptar
     */
    @Override
    public void accept(Project project) {
        project.setProState(EnumProjectState.ACEPTADO);
        project.setCurrentState(new AcceptedState());
        System.out.println("Project assigned to a student.");
    }

    /**
     * Intenta cerrar un proyecto que está en estado recibido.
     *
     * <p>Esta transición no está permitida porque el proyecto debe ser aceptado
     * antes de poder cerrarlo.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void close(Project project) {
        throw new InvalidStateTransitionException("No se puede cerrar un proyecto que no se ha aceptado.");
    }

    /**
     * Intenta ejecutar un proyecto que está en estado recibido.
     *
     * <p>Esta transición no está permitida porque el proyecto debe ser aceptado
     * antes de poder ejecutarlo.</p>
     *
     * @param project El proyecto sobre el que se intenta realizar la transición
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no está permitida
     */
    @Override
    public void execute(Project project) {
        throw new InvalidStateTransitionException("No se puede ejecutar un proyecto que no se ha aceptado.");
    }

    /**
     * Devuelve una representación en cadena de texto del estado actual.
     *
     * @return El nombre del estado como cadena de texto
     */
    @Override
    public String toString() {
        return "ReceivedState";
    }
}
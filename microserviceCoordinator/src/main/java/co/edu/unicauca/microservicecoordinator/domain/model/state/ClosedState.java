package co.edu.unicauca.microservicecoordinator.domain.model.state;

import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;

/**
 * Implementación del estado CERRADO para un proyecto.
 *
 * <p>Esta clase representa el estado final de un proyecto que ha sido cerrado.
 * Define las reglas de transición desde este estado, que al ser un estado terminal,
 * no permite ninguna transición hacia otros estados.</p>
 *
 * <p>El estado CERRADO es un estado terminal en el ciclo de vida del proyecto,
 * por lo que todas las operaciones de transición lanzarán excepciones.</p>
 *
 * @see ProjectState
 * @see EnumProjectState
 */
public class ClosedState implements ProjectState {

    /**
     * Intenta transicionar el proyecto al estado RECIBIDO.
     *
     * <p>Esta transición no es válida desde el estado CERRADO, ya que es un estado terminal.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("No se puede recibir un proyecto cerrado.");
    }

    /**
     * Intenta transicionar el proyecto al estado RECHAZADO.
     *
     * <p>Esta transición no es válida desde el estado CERRADO, ya que es un estado terminal.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void reject(Project project) {
        throw new InvalidStateTransitionException("No se puede rechazar un proyecto cerrado.");
    }

    /**
     * Intenta transicionar el proyecto al estado ACEPTADO.
     *
     * <p>Esta transición no es válida desde el estado CERRADO, ya que es un estado terminal.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void accept(Project project) {
        throw new InvalidStateTransitionException("No se puede aceptar un proyecto cerrado.");
    }

    /**
     * Intenta transicionar el proyecto al estado CERRADO.
     *
     * <p>Esta operación no es válida ya que el proyecto ya se encuentra en estado CERRADO.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void close(Project project) {
        throw new InvalidStateTransitionException("El proyecto ya se cerró.");
    }

    /**
     * Intenta transicionar el proyecto al estado EJECUCION.
     *
     * <p>Esta transición no es válida desde el estado CERRADO, ya que es un estado terminal.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void execute(Project project) {
        throw new InvalidStateTransitionException("No se puede ejecutar un proyecto cerrado.");
    }

    /**
     * Devuelve una representación en texto del estado actual.
     *
     * @return El nombre del estado como String
     */
    @Override
    public String toString() {
        return "ClosedState";
    }
}
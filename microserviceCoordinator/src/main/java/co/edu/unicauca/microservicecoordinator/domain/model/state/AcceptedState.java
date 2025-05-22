package co.edu.unicauca.microservicecoordinator.domain.model.state;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;

/**
 * Implementación del estado ACEPTADO para un proyecto.
 *
 * <p>Esta clase representa el estado de un proyecto que ha sido evaluado
 * y aceptado. Define las transiciones permitidas desde este estado y
 * controla que no se realicen transiciones inválidas dentro del ciclo de vida
 * del proyecto.</p>
 *
 * <p>Desde el estado ACEPTADO, un proyecto solo puede pasar al estado EJECUCION.</p>
 *
 * @see ProjectState
 * @see EnumProjectState
 */
public class AcceptedState implements ProjectState {

    /**
     * Intenta transicionar el proyecto al estado RECIBIDO.
     *
     * <p>Esta transición no es válida desde el estado ACEPTADO.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void receive(Project project) {
        throw new InvalidStateTransitionException("No se puede recibir un proyecto aceptado.");
    }

    /**
     * Intenta transicionar el proyecto al estado RECHAZADO.
     *
     * <p>Esta transición no es válida desde el estado ACEPTADO.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void reject(Project project) {
        throw new InvalidStateTransitionException("No se puede rechazar un proyecto ya aceptado.");
    }

    /**
     * Intenta transicionar el proyecto al estado ACEPTADO.
     *
     * <p>Esta transición no es válida ya que el proyecto ya se encuentra en estado ACEPTADO.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void accept(Project project) {
        throw new InvalidStateTransitionException("El proyecto ya se encuentra aceptado.");
    }

    /**
     * Intenta transicionar el proyecto al estado CERRADO.
     *
     * <p>Esta transición no es válida desde el estado ACEPTADO.
     * Un proyecto solo puede cerrarse desde el estado EJECUCION.</p>
     *
     * @param project El proyecto a transicionar
     * @throws InvalidStateTransitionException Siempre, ya que esta transición no es válida
     */
    @Override
    public void close(Project project) {
        throw new InvalidStateTransitionException("No se puede cerrar un proyecto que no se encuentra en ejecucion.");
    }

    /**
     * Transiciona el proyecto del estado ACEPTADO al estado EJECUCION.
     *
     * <p>Esta es la única transición válida desde el estado ACEPTADO.</p>
     *
     * @param project El proyecto a transicionar al estado EJECUCION
     */
    @Override
    public void execute(Project project) {
        project.changeState(EnumProjectState.EJECUCION);
        System.out.println("Project in executed state.");
    }

    /**
     * Devuelve una representación en texto del estado actual.
     *
     * @return El nombre del estado como String
     */
    @Override
    public String toString() {
        return "AcceptedState";
    }
}
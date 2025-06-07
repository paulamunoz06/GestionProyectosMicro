package co.edu.unicauca.microservicecoordinator.domainTest.modelTest.stateTest;

import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.domain.model.state.ClosedState;
import co.edu.unicauca.microservicecoordinator.domain.model.state.ExecutedState;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ExecutedState}.
 *
 * <p>Se valida que:
 * <ul>
 *     <li>Solo se permite cerrar un proyecto en ejecución.</li>
 *     <li>Todas las demás transiciones lanzan una excepción.</li>
 *     <li>{@code toString()} retorna "ExecutedState".</li>
 * </ul>
 */
class ExecutedStateTest {

    private Project project;
    private ExecutedState executedState;

    @BeforeEach
    void setUp() {
        project = new Project(
                new ProjectId("P005"),
                new ProjectTitle("Proyecto en ejecución"),
                "Descripción",
                "Resumen",
                "Objetivos",
                new ProjectRegistrationDate(LocalDate.of(2025, 5, 15)),
                new ProjectDeadline(4),
                9000.0,
                EnumProjectState.EJECUCION,
                "COMPZ",
                "COORDZ"
        );
        executedState = new ExecutedState();
    }

    @Test
    void receiveShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> executedState.receive(project)
        );
        assertEquals("No se puede recibir un proyecto en ejecución.", ex.getMessage());
    }

    @Test
    void rejectShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> executedState.reject(project)
        );
        assertEquals("No se puede rechazar un proyecto en ejecución.", ex.getMessage());
    }

    @Test
    void acceptShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> executedState.accept(project)
        );
        assertEquals("No se puede aceptar un proyecto que está en ejecución.", ex.getMessage());
    }

    @Test
    void executeShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> executedState.execute(project)
        );
        assertEquals("El proyecto ya se encuentra en ejecución.", ex.getMessage());
    }

    @Test
    void closeShouldTransitionToClosedState() {
        assertDoesNotThrow(() -> executedState.close(project));
        assertEquals(EnumProjectState.CERRADO, project.getProState());
        assertTrue(project.getCurrentState() instanceof ClosedState);
    }

    @Test
    void toStringShouldReturnExecutedState() {
        assertEquals("ExecutedState", executedState.toString());
    }
}

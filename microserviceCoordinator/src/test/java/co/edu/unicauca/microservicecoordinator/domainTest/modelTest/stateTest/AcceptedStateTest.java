package co.edu.unicauca.microservicecoordinator.domainTest.modelTest.stateTest;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.domain.model.state.AcceptedState;
import co.edu.unicauca.microservicecoordinator.domain.model.state.ExecutedState;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link AcceptedState}.
 *
 * <p>Verifica que:
 * <ul>
 *     <li>Solo se permita la transición a estado EJECUCIÓN.</li>
 *     <li>Las transiciones inválidas lancen una {@link InvalidStateTransitionException}.</li>
 *     <li>El método {@code toString()} retorne el nombre correcto del estado.</li>
 * </ul>
 */
class AcceptedStateTest {

    private Project project;
    private AcceptedState acceptedState;

    @BeforeEach
    void setUp() {
        project = new Project(
                new ProjectId("P001"),
                new ProjectTitle("Proyecto Test"),
                "Descripción",
                "Resumen",
                "Objetivos",
                new ProjectRegistrationDate(LocalDate.of(2024, 5, 1)),
                new ProjectDeadline(6),
                10000.0,
                EnumProjectState.ACEPTADO,
                "COMP123",
                "COORD001"
        );
        acceptedState = new AcceptedState();
    }

    @Test
    void executeShouldTransitionToExecution() {
        acceptedState.execute(project);
        assertEquals(EnumProjectState.EJECUCION, project.getProState());
        assertTrue(project.getCurrentState() instanceof ExecutedState);
    }

    @Test
    void receiveShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> acceptedState.receive(project)
        );
        assertEquals("No se puede recibir un proyecto aceptado.", ex.getMessage());
    }

    @Test
    void rejectShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> acceptedState.reject(project)
        );
        assertEquals("No se puede rechazar un proyecto ya aceptado.", ex.getMessage());
    }

    @Test
    void acceptShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> acceptedState.accept(project)
        );
        assertEquals("El proyecto ya se encuentra aceptado.", ex.getMessage());
    }

    @Test
    void closeShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> acceptedState.close(project)
        );
        assertEquals("No se puede cerrar un proyecto que no se encuentra en ejecucion.", ex.getMessage());
    }

    @Test
    void toStringShouldReturnAcceptedState() {
        assertEquals("AcceptedState", acceptedState.toString());
    }
}

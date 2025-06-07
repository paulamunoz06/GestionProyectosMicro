package co.edu.unicauca.microservicecoordinator.domainTest.modelTest.stateTest;


import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.state.ClosedState;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ClosedState}.
 *
 * <p>Verifica que:
 * <ul>
 *     <li>Ninguna transición sea permitida desde el estado CERRADO.</li>
 *     <li>Cada método de transición lance una {@link InvalidStateTransitionException} con el mensaje esperado.</li>
 *     <li>El método {@code toString()} retorne "ClosedState".</li>
 * </ul>
 */
class ClosedStateTest {

    private Project project;
    private ClosedState closedState;

    @BeforeEach
    void setUp() {
        project = new Project(
                new ProjectId("P002"),
                new ProjectTitle("Proyecto Cerrado"),
                "Descripción",
                "Resumen",
                "Objetivos",
                new ProjectRegistrationDate(LocalDate.of(2024, 4, 15)),
                new ProjectDeadline(12),
                50000.0,
                EnumProjectState.CERRADO,
                "COMP999",
                "COORD999"
        );
        closedState = new ClosedState();
    }

    @Test
    void receiveShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> closedState.receive(project)
        );
        assertEquals("No se puede recibir un proyecto cerrado.", ex.getMessage());
    }

    @Test
    void rejectShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> closedState.reject(project)
        );
        assertEquals("No se puede rechazar un proyecto cerrado.", ex.getMessage());
    }

    @Test
    void acceptShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> closedState.accept(project)
        );
        assertEquals("No se puede aceptar un proyecto cerrado.", ex.getMessage());
    }

    @Test
    void closeShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> closedState.close(project)
        );
        assertEquals("El proyecto ya se cerró.", ex.getMessage());
    }

    @Test
    void executeShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> closedState.execute(project)
        );
        assertEquals("No se puede ejecutar un proyecto cerrado.", ex.getMessage());
    }

    @Test
    void toStringShouldReturnClosedState() {
        assertEquals("ClosedState", closedState.toString());
    }
}

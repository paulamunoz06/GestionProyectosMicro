package co.edu.unicauca.microservicecoordinator.domainTest.modelTest.stateTest;


import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.domain.model.state.RejectState;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link RejectState}.
 *
 * <p>Se valida que:
 * <ul>
 *     <li>No se permite recibir, aceptar, ejecutar ni cerrar un proyecto rechazado.</li>
 *     <li>Rechazar un proyecto ya rechazado no lanza excepción y simplemente informa el estado.</li>
 *     <li>{@code toString()} devuelve "RejectState".</li>
 * </ul>
 */
class RejectStateTest {

    private Project project;
    private RejectState rejectState;

    @BeforeEach
    void setUp() {
        project = new Project(
                new ProjectId("P004"),
                new ProjectTitle("Proyecto Rechazado"),
                "Descripción del proyecto",
                "Resumen",
                "Objetivos",
                new ProjectRegistrationDate(LocalDate.of(2025, 4, 1)),
                new ProjectDeadline(5),
                5000.0,
                EnumProjectState.RECHAZADO,
                "COMPX",
                "COORDX"
        );
        rejectState = new RejectState();
    }

    @Test
    void receiveShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> rejectState.receive(project)
        );
        assertEquals("No se puede recibir un proyecto rechazado.", ex.getMessage());
    }

    @Test
    void rejectShouldNotChangeStateOrThrowException() {
        // No se lanza excepción, solo mensaje informativo
        assertDoesNotThrow(() -> rejectState.reject(project));
        assertEquals(EnumProjectState.RECHAZADO, project.getProState());
        assertTrue(project.getCurrentState() instanceof RejectState);
    }

    @Test
    void acceptShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> rejectState.accept(project)
        );
        assertEquals("No se puede aceptar un proyecto rechazado.", ex.getMessage());
    }

    @Test
    void closeShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> rejectState.close(project)
        );
        assertEquals("No se puede completar un proyecto rechazado.", ex.getMessage());
    }

    @Test
    void executeShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> rejectState.execute(project)
        );
        assertEquals("No se puede ejecutar un proyecto rechazado.", ex.getMessage());
    }

    @Test
    void toStringShouldReturnRejectState() {
        assertEquals("RejectState", rejectState.toString());
    }
}


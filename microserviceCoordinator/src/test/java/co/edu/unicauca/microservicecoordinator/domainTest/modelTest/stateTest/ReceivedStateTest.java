package co.edu.unicauca.microservicecoordinator.domainTest.modelTest.stateTest;

import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.domain.model.state.AcceptedState;
import co.edu.unicauca.microservicecoordinator.domain.model.state.ReceivedState;
import co.edu.unicauca.microservicecoordinator.domain.model.state.RejectState;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ReceivedState}.
 *
 * <p>Se prueba que:
 * <ul>
 *     <li>No se puede recibir un proyecto ya recibido.</li>
 *     <li>Es posible aceptar un proyecto recibido, cambiando su estado a ACEPTADO.</li>
 *     <li>Es posible rechazar un proyecto recibido, cambiando su estado a RECHAZADO.</li>
 *     <li>No se permite cerrar o ejecutar directamente un proyecto recibido.</li>
 *     <li>El método {@code toString()} retorna "ReceivedState".</li>
 * </ul>
 */
class ReceivedStateTest {

    private Project project;
    private ReceivedState receivedState;

    @BeforeEach
    void setUp() {
        project = new Project(
                new ProjectId("P003"),
                new ProjectTitle("Proyecto Recibido"),
                "Descripción del proyecto",
                "Resumen",
                "Objetivos",
                new ProjectRegistrationDate(LocalDate.of(2025, 5, 1)),
                new ProjectDeadline(8),
                10000.0,
                EnumProjectState.RECIBIDO,
                "COMPX",
                "COORDX"
        );
        receivedState = new ReceivedState();
    }

    @Test
    void receiveShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> receivedState.receive(project)
        );
        assertEquals("El proyecto ya fue recibido", ex.getMessage());
    }

    @Test
    void rejectShouldChangeStateToRejected() {
        receivedState.reject(project);
        assertEquals(EnumProjectState.RECHAZADO, project.getProState());
        assertTrue(project.getCurrentState() instanceof RejectState);
    }

    @Test
    void acceptShouldChangeStateToAccepted() {
        receivedState.accept(project);
        assertEquals(EnumProjectState.ACEPTADO, project.getProState());
        assertTrue(project.getCurrentState() instanceof AcceptedState);
    }

    @Test
    void closeShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> receivedState.close(project)
        );
        assertEquals("No se puede cerrar un proyecto que no se ha aceptado.", ex.getMessage());
    }

    @Test
    void executeShouldThrowException() {
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> receivedState.execute(project)
        );
        assertEquals("No se puede ejecutar un proyecto que no se ha aceptado.", ex.getMessage());
    }

    @Test
    void toStringShouldReturnReceivedState() {
        assertEquals("ReceivedState", receivedState.toString());
    }
}


package co.edu.unicauca.microservicecoordinator.domainTest.modelTest;

import co.edu.unicauca.microservicecoordinator.domain.model.Coordinator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase de entidad de dominio {@link Coordinator}.
 * Estas pruebas verifican el comportamiento de los constructores y los métodos getters
 * de la clase {@code Coordinator}.
 */
class CoordinatorTest {

    /**
     * Prueba el constructor por defecto de la clase {@link Coordinator}.
     * Verifica que todos los campos se inicializan a {@code null} cuando se utiliza
     * el constructor sin argumentos.
     */
    @Test
    void testDefaultConstructor() {
        Coordinator coordinator = new Coordinator();
        assertNull(coordinator.getCoordinatorId(), "El ID del coordinador por defecto debería ser nulo.");
        assertNull(coordinator.getCoordinatorEmail(), "El email del coordinador por defecto debería ser nulo.");
        assertNull(coordinator.getCoordinatorPassword(), "La contraseña del coordinador por defecto debería ser nula.");
    }

    /**
     * Prueba el constructor parametrizado de la clase {@link Coordinator} y sus métodos getters.
     * Verifica que los valores proporcionados al constructor se asignan correctamente
     * a los campos correspondientes y que los getters devuelven estos mismos valores.
     */
    @Test
    void testParameterizedConstructorAndGetters() {
        String id = "coord123";
        String email = "coordinator@example.com";
        String password = "securePassword123";

        Coordinator coordinator = new Coordinator(id, email, password);

        assertEquals(id, coordinator.getCoordinatorId(), "El ID del coordinador debe coincidir con el argumento del constructor.");
        assertEquals(email, coordinator.getCoordinatorEmail(), "El email del coordinador debe coincidir con el argumento del constructor.");
        assertEquals(password, coordinator.getCoordinatorPassword(), "La contraseña del coordinador debe coincidir con el argumento del constructor.");
    }

    /**
     * Prueba los métodos getters de la clase {@link Coordinator} cuando se utilizan
     * valores nulos en el constructor parametrizado.
     * Verifica que si se pasan valores nulos al constructor, los getters
     * devuelven {@code null} para los campos correspondientes.
     */
    @Test
    void testGettersWithNullValuesFromConstructor() {
        Coordinator coordinator = new Coordinator(null, null, null);

        assertNull(coordinator.getCoordinatorId(), "El ID del coordinador debería ser nulo si se estableció como nulo.");
        assertNull(coordinator.getCoordinatorEmail(), "El email del coordinador debería ser nulo si se estableció como nulo.");
        assertNull(coordinator.getCoordinatorPassword(), "La contraseña del coordinador debería ser nula si se estableció como nula.");
    }
}
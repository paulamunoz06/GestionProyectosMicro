package co.edu.unicauca.microservicecoordinator.domainTest.modelTest;


import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.state.*;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase de entidad de dominio {@link Project}.
 * Estas pruebas verifican el comportamiento del constructor, los métodos getters,
 * la lógica de manejo de estados ({@code changeState}, {@code syncState}), y el comportamiento
 * de los valores por defecto para ciertos campos de la entidad {@code Project}.
 */
class ProjectTest {

    // Campos para los datos de prueba, inicializados en setUp()
    private ProjectId proId;
    private ProjectTitle proTitle;
    private String proDescription;
    private String proAbstract;
    private String proGoals;
    private ProjectRegistrationDate proDate;
    private ProjectDeadline proDeadLine;
    private Double proBudget;
    private EnumProjectState proStateInitial;
    private String idcompany;
    private String proCoordinator;

    /**
     * Configuración inicial ejecutada antes de cada método de prueba.
     * Inicializa los campos con valores de ejemplo para ser utilizados en los tests,
     * incluyendo objetos de valor y datos primitivos.
     */
    @BeforeEach
    void setUp() {
        proId = new ProjectId("PROJ-001");
        proTitle = new ProjectTitle("Innovatech Future Solutions");
        proDescription = "A groundbreaking project to revolutionize tech.";
        proAbstract = "Revolutionizing tech.";
        proGoals = "Goal 1; Goal 2";
        proDate = new ProjectRegistrationDate(LocalDate.now());
        proDeadLine = new ProjectDeadline(12); // 12 months
        proBudget = 50000.00;
        proStateInitial = EnumProjectState.RECIBIDO;
        idcompany = "COMP-XYZ";
        proCoordinator = "COORD-ABC";
    }

    /**
     * Prueba el constructor de {@link Project} cuando se proporcionan todos los valores válidos
     * y verifica que los getters devuelven los valores correctos.
     * También asegura que el estado actual ({@code currentState}) se inicializa correctamente
     * según el {@code EnumProjectState} proporcionado.
     */
    @Test
    void testConstructorWithAllValuesAndGetters() {
        Project project = new Project(proId, proTitle, proDescription, proAbstract, proGoals,
                proDate, proDeadLine, proBudget, proStateInitial,
                idcompany, proCoordinator);

        assertEquals(proId.getValue(), project.getProId().getValue(), "El ID del proyecto debe coincidir.");
        assertEquals(proTitle.getValue(), project.getProTitle().getValue(), "El título del proyecto debe coincidir.");
        assertEquals(proDescription, project.getProDescription(), "La descripción debe coincidir.");
        assertEquals(proAbstract, project.getProAbstract(), "El resumen debe coincidir.");
        assertEquals(proGoals, project.getProGoals(), "Los objetivos deben coincidir.");
        assertEquals(proDate.getDate(), project.getProDate().getDate(), "La fecha debe coincidir.");
        assertEquals(proDeadLine.getMonths(), project.getProDeadLine().getMonths(), "La duración debe coincidir.");
        assertEquals(proBudget, project.getProBudget(), "El presupuesto debe coincidir.");
        assertEquals(proStateInitial, project.getProState(), "El estado Enum debe coincidir.");
        assertEquals(idcompany, project.getIdcompany(), "El ID de la compañía debe coincidir.");
        assertEquals(proCoordinator, project.getProCoordinator(), "El ID del coordinador debe coincidir.");

        assertNotNull(project.getCurrentState(), "El estado actual (CurrentState) debería estar inicializado.");
        assertInstanceOf(ReceivedState.class, project.getCurrentState(), "El estado inicial debería ser ReceivedState para el enum RECIBIDO.");
    }

    /**
     * Prueba el comportamiento del constructor de {@link Project} cuando se proporciona un presupuesto nulo.
     * Verifica que el presupuesto del proyecto se establece a 0.0 por defecto.
     */
    @Test
    void testConstructorWithNullBudget() {
        Project project = new Project(proId, proTitle, proDescription, proAbstract, proGoals,
                proDate, proDeadLine, null, proStateInitial,
                idcompany, proCoordinator);
        assertEquals(0.0, project.getProBudget(), "El presupuesto debería ser 0.0 por defecto si es nulo en el constructor.");
    }

    /**
     * Prueba el comportamiento del constructor de {@link Project} cuando se proporciona un {@code EnumProjectState} nulo.
     * Verifica que el estado del proyecto se establece a {@code EnumProjectState.RECIBIDO} por defecto
     * y que el {@code currentState} corresponde a {@link ReceivedState}.
     */
    @Test
    void testConstructorWithNullEnumState() {
        Project project = new Project(proId, proTitle, proDescription, proAbstract, proGoals,
                proDate, proDeadLine, proBudget, null,
                idcompany, proCoordinator);
        assertEquals(EnumProjectState.RECIBIDO, project.getProState(), "El estado Enum debería ser RECIBIDO por defecto si es nulo en el constructor.");
        assertInstanceOf(ReceivedState.class, project.getCurrentState(), "El estado actual (CurrentState) debería ser ReceivedState si el estado enum es nulo.");
    }

    /**
     * Prueba el comportamiento del constructor de {@link Project} cuando se proporciona un ID de compañía nulo.
     * Verifica que el ID de la compañía se establece a una cadena vacía por defecto.
     */
    @Test
    void testConstructorWithNullIdCompany() {
        Project project = new Project(proId, proTitle, proDescription, proAbstract, proGoals,
                proDate, proDeadLine, proBudget, proStateInitial,
                null, proCoordinator);
        assertEquals("", project.getIdcompany(), "El ID de la compañía debería ser una cadena vacía por defecto si es nulo.");
    }

    /**
     * Prueba el comportamiento del constructor de {@link Project} cuando se proporciona un ID de coordinador nulo.
     * Verifica que el ID del coordinador se establece a una cadena vacía por defecto.
     */
    @Test
    void testConstructorWithNullProCoordinator() {
        Project project = new Project(proId, proTitle, proDescription, proAbstract, proGoals,
                proDate, proDeadLine, proBudget, proStateInitial,
                idcompany, null);
        assertEquals("", project.getProCoordinator(), "El ID del coordinador debería ser una cadena vacía por defecto si es nulo.");
    }

    /**
     * Prueba el método {@code changeState} de la clase {@link Project}.
     * Verifica que al cambiar el estado del proyecto, tanto el {@code EnumProjectState}
     * como la instancia de {@code currentState} (el objeto de estado concreto) se actualizan correctamente
     * a través de diferentes transiciones de estado.
     */
    @Test
    void testChangeState() {
        Project project = new Project(proId, proTitle, proDescription, proAbstract, proGoals,
                proDate, proDeadLine, proBudget, EnumProjectState.RECIBIDO,
                idcompany, proCoordinator);

        // Verificación del estado inicial
        assertEquals(EnumProjectState.RECIBIDO, project.getProState());
        assertInstanceOf(ReceivedState.class, project.getCurrentState());

        // Cambio a ACEPTADO
        project.changeState(EnumProjectState.ACEPTADO);
        assertEquals(EnumProjectState.ACEPTADO, project.getProState(), "El estado Enum debería cambiar a ACEPTADO.");
        assertInstanceOf(AcceptedState.class, project.getCurrentState(), "CurrentState debería ser AcceptedState.");

        // Cambio a EJECUCION
        project.changeState(EnumProjectState.EJECUCION);
        assertEquals(EnumProjectState.EJECUCION, project.getProState(), "El estado Enum debería cambiar a EJECUCION.");
        assertInstanceOf(ExecutedState.class, project.getCurrentState(), "CurrentState debería ser ExecutedState.");

        // Cambio a CERRADO
        project.changeState(EnumProjectState.CERRADO);
        assertEquals(EnumProjectState.CERRADO, project.getProState(), "El estado Enum debería cambiar a CERRADO.");
        assertInstanceOf(ClosedState.class, project.getCurrentState(), "CurrentState debería ser ClosedState.");

        // Cambio a RECHAZADO
        project.changeState(EnumProjectState.RECHAZADO);
        assertEquals(EnumProjectState.RECHAZADO, project.getProState(), "El estado Enum debería cambiar a RECHAZADO.");
        assertInstanceOf(RejectState.class, project.getCurrentState(), "CurrentState debería ser RejectState.");
    }

    /**
     * Prueba que el método {@code syncState} es llamado implícitamente por el constructor
     * de {@link Project}.
     * Se verifica al construir un proyecto con un estado específico (ej. ACEPTADO) y
     * comprobando que el {@code currentState} es la instancia correcta (ej. {@link AcceptedState}).
     */
    @Test
    void testSyncStateMethodIsCalledByConstructor() {
        Project project = new Project(proId, proTitle, proDescription, proAbstract, proGoals,
                proDate, proDeadLine, proBudget, EnumProjectState.ACEPTADO, // Iniciar con ACEPTADO
                idcompany, proCoordinator);

        assertEquals(EnumProjectState.ACEPTADO, project.getProState());
        assertInstanceOf(AcceptedState.class, project.getCurrentState(), "El constructor debería llamar a syncState y establecer currentState correctamente.");
    }

    /**
     * Prueba que los métodos getters que devuelven objetos de valor (Value Objects)
     * crean y devuelven nuevas instancias de dichos objetos.
     * Esto es importante para mantener la inmutabilidad y el encapsulamiento de los VOs.
     * Se usa {@code assertNotSame} para verificar que las instancias no son las mismas en memoria,
     * aunque sus valores internos sean iguales (verificado con {@code assertEquals} sobre el valor).
     */
    @Test
    void testGettersReturnValueObjects() {
        Project project = new Project(proId, proTitle, proDescription, proAbstract, proGoals,
                proDate, proDeadLine, proBudget, proStateInitial,
                idcompany, proCoordinator);

        assertEquals(proId.getValue(), project.getProId().getValue());
        assertNotSame(proId, project.getProId(), "getProId debería devolver una nueva instancia de ProjectId.");

        assertEquals(proTitle.getValue(), project.getProTitle().getValue());
        assertNotSame(proTitle, project.getProTitle(), "getProTitle debería devolver una nueva instancia de ProjectTitle.");

        assertEquals(proDate.getDate(), project.getProDate().getDate());
        assertNotSame(proDate, project.getProDate(), "getProDate debería devolver una nueva instancia de ProjectRegistrationDate.");

        assertEquals(proDeadLine.getMonths(), project.getProDeadLine().getMonths());
        assertNotSame(proDeadLine, project.getProDeadLine(), "getProDeadLine debería devolver una nueva instancia de ProjectDeadline.");
    }
}
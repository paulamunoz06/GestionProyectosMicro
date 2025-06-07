package co.edu.unicauca.microservicecoordinator.adapterTest.in.rest;

import co.edu.unicauca.microservicecoordinator.adapter.in.rest.CoordinatorService;
import co.edu.unicauca.microservicecoordinator.application.port.out.ProjectRepositoryPort;
import co.edu.unicauca.microservicecoordinator.domain.model.*;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.infraestructure.config.RabbitMQConfig;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link CoordinatorService}.
 * Estas pruebas verifican la lógica del servicio de coordinación, asegurando que interactúa
 * correctamente con sus dependencias (mockeadas) y maneja diferentes escenarios
 * de evaluación de proyectos.
 */
class CoordinatorServiceTest {

    /**
     * Mock del puerto de repositorio de proyectos.
     * Utilizado para simular las interacciones con la capa de persistencia de proyectos.
     */
    @Mock
    private ProjectRepositoryPort projectRepository;

    /**
     * Mock del RabbitTemplate.
     * Utilizado para verificar que los mensajes se envían correctamente a las colas de RabbitMQ
     * sin necesidad de una instancia real del broker.
     */
    @Mock
    private RabbitTemplate rabbitTemplateMock;

    /**
     * Instancia de {@link CoordinatorService} bajo prueba.
     * Los mocks {@code projectRepository} y {@code rabbitTemplateMock} serán inyectados en esta instancia.
     */
    @InjectMocks
    private CoordinatorService coordinatorService;

    /**
     * Recurso para cerrar los mocks abiertos por MockitoAnnotations después de cada prueba.
     */
    private AutoCloseable closeable;

    /**
     * Configuración inicial ejecutada antes de cada método de prueba.
     * Inicializa los mocks y establece el mock de {@code RabbitTemplate} en {@code coordinatorService}
     * usando {@code ReflectionTestUtils} debido a que el campo original usa {@code @Autowired}.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        // Inyección manual del mock de RabbitTemplate ya que el campo original está anotado con @Autowired
        ReflectionTestUtils.setField(coordinatorService, "rabbitTemplate", rabbitTemplateMock);
    }

    /**
     * Método helper para crear un {@link ProjectDto} completo con datos válidos.
     * Utilizado para simular los DTOs que se obtendrían del repositorio y que son necesarios
     * para que el {@code ProjectDtoMapper.projectToClass} funcione correctamente.
     *
     * @param id ID del proyecto.
     * @param title Título del proyecto.
     * @param date Fecha de registro del proyecto.
     * @param state Estado inicial del proyecto.
     * @param deadlineMonths Duración del proyecto en meses.
     * @param budget Presupuesto del proyecto.
     * @param description Descripción del proyecto.
     * @param proAbstract Resumen del proyecto.
     * @param goals Objetivos del proyecto.
     * @param companyId ID de la compañía.
     * @param coordinatorId ID del coordinador.
     * @return Un {@link ProjectDto} con todos los campos necesarios poblados.
     */
    private ProjectDto createCompleteProjectDto(String id, String title, LocalDate date, EnumProjectState state, int deadlineMonths, double budget, String description, String proAbstract, String goals, String companyId, String coordinatorId) {
        ProjectDto dto = new ProjectDto();
        dto.setProId(id);
        dto.setProTitle(title);
        dto.setProDescription(description != null ? description : "Default Description");
        dto.setProAbstract(proAbstract != null ? proAbstract : "Default Abstract");
        dto.setProGoals(goals != null ? goals : "Default Goals");
        dto.setProDate(date);
        dto.setProDeadLine(deadlineMonths);
        dto.setProBudget(budget);
        dto.setProState(state.name());
        dto.setIdcompany(companyId != null ? companyId : "DefaultCompany");
        dto.setProCoordinator(coordinatorId != null ? coordinatorId : "DefaultCoordinator");
        return dto;
    }

    /**
     * Prueba que el método {@code evaluateProject} actualiza correctamente el estado de un proyecto a "ACEPTADO".
     * Verifica que:
     * <ul>
     *     <li>El DTO resultante tenga el estado "ACEPTADO".</li>
     *     <li>Se guarde el proyecto con el estado actualizado en el repositorio.</li>
     *     <li>Se envíe un mensaje a RabbitMQ con el DTO del proyecto actualizado.</li>
     * </ul>
     */
    @Test
    void evaluateProject_shouldAcceptReceivedProject() {
        // Arrange
        ProjectDto dtoDelRepositorio = createCompleteProjectDto(
                "P001", "Proyecto Valido", LocalDate.now(), EnumProjectState.RECIBIDO,
                12, 5000.0, "Una descripción válida", "Un abstract válido",
                "Objetivos válidos", "COMP001", "COORD001"
        );
        // Simula que el repositorio encuentra el proyecto (como DTO, según la firma del puerto)
        when(projectRepository.findById("P001")).thenReturn(Optional.of(dtoDelRepositorio));

        // Act
        ProjectDto resultDto = coordinatorService.evaluateProject("P001", "ACEPTADO");

        // Assert
        assertNotNull(resultDto, "El DTO resultante no debe ser nulo.");
        assertEquals(EnumProjectState.ACEPTADO.name(), resultDto.getProState(), "El estado del DTO debe ser ACEPTADO.");

        // Verifica que el proyecto se guardó con el estado correcto
        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(projectCaptor.capture());
        Project savedProject = projectCaptor.getValue();
        assertNotNull(savedProject, "El proyecto guardado no debe ser nulo.");
        assertEquals(EnumProjectState.ACEPTADO, savedProject.getProState(), "El proyecto guardado debe tener el estado ACEPTADO.");
        assertEquals("P001", savedProject.getProId().getValue(), "El ID del proyecto guardado debe ser P001.");

        // Verifica que se envió el mensaje correcto a RabbitMQ
        ArgumentCaptor<ProjectDto> dtoCaptorRabbit = ArgumentCaptor.forClass(ProjectDto.class);
        verify(rabbitTemplateMock).convertAndSend(
                eq(RabbitMQConfig.UPDATEPROJECT_QUEUE),
                dtoCaptorRabbit.capture()
        );
        ProjectDto sentDto = dtoCaptorRabbit.getValue();
        assertNotNull(sentDto, "El DTO enviado a RabbitMQ no debe ser nulo.");
        assertEquals(EnumProjectState.ACEPTADO.name(), sentDto.getProState(), "El DTO enviado a RabbitMQ debe tener el estado ACEPTADO.");
        assertEquals("P001", sentDto.getProId(), "El ID del proyecto en el DTO enviado debe ser P001.");
    }

    /**
     * Prueba que {@code evaluateProject} lanza {@link InvalidStateTransitionException} cuando el proyecto no se encuentra.
     * Verifica también que no se realicen intentos de guardar el proyecto ni de enviar mensajes a RabbitMQ.
     */
    @Test
    void evaluateProject_shouldThrowWhenProjectNotFound() {
        // Arrange
        // Simula que el repositorio no encuentra el proyecto
        when(projectRepository.findById("NOEXISTE")).thenReturn(Optional.empty());

        // Act & Assert
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> coordinatorService.evaluateProject("NOEXISTE", "ACEPTADO")
        );

        assertEquals("Proyecto no encontrado con ID: NOEXISTE", ex.getMessage());
        // Verifica que no hubo interacciones no deseadas
        verify(rabbitTemplateMock, never()).convertAndSend(anyString(), any(Object.class));
        verify(projectRepository, never()).save(any(Project.class));
    }

    /**
     * Prueba que {@code evaluateProject} lanza {@link InvalidStateTransitionException} cuando se proporciona una cadena de estado inválida.
     * Verifica que la excepción se lanza antes de intentar guardar o enviar mensajes,
     * asumiendo que el proyecto sí fue encontrado inicialmente.
     */
    @Test
    void evaluateProject_shouldThrowForInvalidStateString() {
        // Arrange
        ProjectDto dtoDelRepositorio = createCompleteProjectDto(
                "P001", "Proyecto Valido para Test de Estado Inválido", LocalDate.now(), EnumProjectState.RECIBIDO,
                6, 2000.0, "Descripción completa", "Abstract completo",
                "Metas completas", "COMP002", "COORD002"
        );
        // Simula que el repositorio encuentra el proyecto
        when(projectRepository.findById("P001")).thenReturn(Optional.of(dtoDelRepositorio));

        // Act & Assert
        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> coordinatorService.evaluateProject("P001", "DESCONOCIDO") // Estado inválido
        );

        assertEquals("Estado inválido: DESCONOCIDO", ex.getMessage());
        // Verifica que no hubo interacciones no deseadas
        verify(rabbitTemplateMock, never()).convertAndSend(anyString(), any(Object.class));
        verify(projectRepository, never()).save(any(Project.class));
    }

    /**
     * Limpieza después de cada prueba.
     * Cierra los recursos de Mockito para evitar memory leaks o interferencias entre tests.
     *
     * @throws Exception si ocurre un error durante el cierre.
     */
    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }
}
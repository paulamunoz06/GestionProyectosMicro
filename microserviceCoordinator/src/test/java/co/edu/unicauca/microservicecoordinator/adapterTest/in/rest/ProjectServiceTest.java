package co.edu.unicauca.microservicecoordinator.adapterTest.in.rest;


import co.edu.unicauca.microservicecoordinator.adapter.in.rest.ProjectService;
import co.edu.unicauca.microservicecoordinator.application.port.out.ProjectRepositoryPort;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectDeadline;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectId;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectRegistrationDate;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectTitle;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link ProjectService}.
 * Estas pruebas verifican la lógica del servicio de proyectos, asegurando que delega
 * correctamente las operaciones al {@link ProjectRepositoryPort} y maneja la lógica
 * de negocio simple, como la conversión de estados y el procesamiento de proyectos recibidos.
 */
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    /**
     * Mock del puerto de repositorio de proyectos.
     * Simula las interacciones con la capa de persistencia para operaciones de proyectos.
     */
    @Mock
    private ProjectRepositoryPort projectRepositoryPortMock;

    /**
     * Instancia de {@link ProjectService} bajo prueba.
     * El mock {@code projectRepositoryPortMock} será inyectado en esta instancia.
     */
    @InjectMocks
    private ProjectService projectService;

    /**
     * DTO de proyecto de ejemplo utilizado en múltiples pruebas.
     */
    private ProjectDto sampleProjectDto1;
    /**
     * Otro DTO de proyecto de ejemplo utilizado en pruebas de listas.
     */
    private ProjectDto sampleProjectDto2;
    /**
     * ID de proyecto de ejemplo.
     */
    private final String projectId1 = "P001";

    /**
     * Configuración inicial ejecutada antes de cada método de prueba.
     * Inicializa los DTOs de proyecto de ejemplo con datos completos para asegurar
     * que los mappers (si se usan indirectamente) funcionen correctamente.
     */
    @BeforeEach
    void setUp() {
        sampleProjectDto1 = new ProjectDto();
        sampleProjectDto1.setProId(projectId1);
        sampleProjectDto1.setProTitle("Proyecto Alpha DTO");
        sampleProjectDto1.setProState(EnumProjectState.RECIBIDO.name());
        sampleProjectDto1.setProDate(LocalDate.now());
        sampleProjectDto1.setProDescription("Descripción Alpha");
        sampleProjectDto1.setProAbstract("Abstract Alpha");
        sampleProjectDto1.setProGoals("Metas Alpha");
        sampleProjectDto1.setProDeadLine(12);
        sampleProjectDto1.setProBudget(1000.0);
        sampleProjectDto1.setIdcompany("COMP01");
        sampleProjectDto1.setProCoordinator("COORD01");

        sampleProjectDto2 = new ProjectDto();
        sampleProjectDto2.setProId("P002");
        sampleProjectDto2.setProTitle("Proyecto Beta DTO");
        sampleProjectDto2.setProState(EnumProjectState.ACEPTADO.name());
        sampleProjectDto2.setProDate(LocalDate.now().minusDays(1));

    }

    /**
     * Prueba que {@code findById} devuelve un {@link Optional} conteniendo el {@link ProjectDto}
     * cuando el proyecto es encontrado por el repositorio.
     */
    @Test
    void findById_shouldReturnProjectDto_whenFound() {
        // Arrange
        when(projectRepositoryPortMock.findById(eq(projectId1))).thenReturn(Optional.of(sampleProjectDto1));

        // Act
        Optional<ProjectDto> result = projectService.findById(projectId1);

        // Assert
        assertTrue(result.isPresent(), "El Optional no debería estar vacío cuando se encuentra el proyecto.");
        assertEquals(sampleProjectDto1.getProId(), result.get().getProId(), "El ID del proyecto devuelto no coincide.");
        assertEquals(sampleProjectDto1.getProTitle(), result.get().getProTitle(), "El título del proyecto devuelto no coincide.");
        verify(projectRepositoryPortMock, times(1)).findById(eq(projectId1));
    }

    /**
     * Prueba que {@code findById} devuelve un {@link Optional#empty()}
     * cuando el proyecto no es encontrado por el repositorio.
     */
    @Test
    void findById_shouldReturnEmptyOptional_whenNotFound() {
        // Arrange
        String nonExistentId = "P999";
        when(projectRepositoryPortMock.findById(eq(nonExistentId))).thenReturn(Optional.empty());

        // Act
        Optional<ProjectDto> result = projectService.findById(nonExistentId);

        // Assert
        assertTrue(result.isEmpty(), "El Optional debería estar vacío cuando no se encuentra el proyecto.");
        verify(projectRepositoryPortMock, times(1)).findById(eq(nonExistentId));
    }

    /**
     * Prueba que {@code findAllProjects} devuelve una lista de {@link ProjectDto}
     * obtenida del repositorio.
     */
    @Test
    void findAllProjects_shouldReturnListOfProjectDtos() {
        // Arrange
        List<ProjectDto> expectedDtos = Arrays.asList(sampleProjectDto1, sampleProjectDto2);
        when(projectRepositoryPortMock.findAll()).thenReturn(expectedDtos);

        // Act
        List<ProjectDto> actualDtos = projectService.findAllProjects();

        // Assert
        assertNotNull(actualDtos, "La lista de DTOs no debería ser nula.");
        assertEquals(2, actualDtos.size(), "El tamaño de la lista de DTOs no coincide.");
        assertEquals(sampleProjectDto1.getProId(), actualDtos.get(0).getProId());
        assertEquals(sampleProjectDto2.getProId(), actualDtos.get(1).getProId());
        verify(projectRepositoryPortMock, times(1)).findAll();
    }

    /**
     * Prueba que {@code countByStatus} convierte correctamente la cadena de estado a {@link EnumProjectState}
     * y llama al repositorio para obtener el conteo.
     */
    @Test
    void countByStatus_shouldCallRepositoryWithCorrectEnum() {
        // Arrange
        String statusStr = "ACEPTADO";
        EnumProjectState expectedEnumStatus = EnumProjectState.ACEPTADO;
        Long expectedCount = 5L;
        when(projectRepositoryPortMock.countByProState(eq(expectedEnumStatus))).thenReturn(expectedCount);

        // Act
        Long actualCount = projectService.countByStatus(statusStr);

        // Assert
        assertEquals(expectedCount, actualCount, "El conteo por estado no coincide.");
        verify(projectRepositoryPortMock, times(1)).countByProState(eq(expectedEnumStatus));
    }

    /**
     * Prueba que {@code countByStatus} maneja la cadena de estado sin distinguir mayúsculas de minúsculas
     * antes de convertirla a {@link EnumProjectState} y llamar al repositorio.
     */
    @Test
    void countByStatus_shouldBeCaseInsensitiveAndCallRepository() {
        // Arrange
        String statusStr = "recibido"; // Uso de minúsculas
        EnumProjectState expectedEnumStatus = EnumProjectState.RECIBIDO;
        Long expectedCount = 3L;
        when(projectRepositoryPortMock.countByProState(eq(expectedEnumStatus))).thenReturn(expectedCount);

        // Act
        Long actualCount = projectService.countByStatus(statusStr);

        // Assert
        assertEquals(expectedCount, actualCount, "El conteo por estado (insensible a mayúsculas) no coincide.");
        verify(projectRepositoryPortMock, times(1)).countByProState(eq(expectedEnumStatus));
    }

    /**
     * Prueba que {@code countByStatus} lanza {@link IllegalArgumentException}
     * cuando se proporciona una cadena de estado inválida que no puede ser convertida a {@link EnumProjectState}.
     * Verifica también que no se interactúa con el repositorio en este caso.
     */
    @Test
    void countByStatus_shouldThrowIllegalArgumentException_forInvalidStatusString() {
        // Arrange
        String invalidStatusStr = "INEXISTENTE";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            projectService.countByStatus(invalidStatusStr);
        }, "Debería lanzarse IllegalArgumentException para un estado inválido.");

        verify(projectRepositoryPortMock, never()).countByProState(any(EnumProjectState.class));
    }

    /**
     * Prueba que {@code countTotalProjects} devuelve el conteo total de proyectos
     * obtenido del repositorio.
     */
    @Test
    void countTotalProjects_shouldReturnCountFromRepository() {
        // Arrange
        int expectedCount = 10;
        when(projectRepositoryPortMock.count()).thenReturn(expectedCount);

        // Act
        int actualCount = projectService.countTotalProjects();

        // Assert
        assertEquals(expectedCount, actualCount, "El conteo total de proyectos no coincide.");
        verify(projectRepositoryPortMock, times(1)).count();
    }

    /**
     * Prueba que el método {@code receiveProject} (anotado con {@code @RabbitListener} en la clase original)
     * mapea correctamente el {@link ProjectDto} recibido a un objeto de dominio {@link Project}
     * y luego llama al método {@code save} del repositorio.
     * <p>
     * Nota: Esta prueba unitaria invoca el método directamente y no prueba la funcionalidad
     * del listener de RabbitMQ en sí.
     * </p>
     */
    @Test
    void receiveProject_shouldMapDtoToDomainAndCallSave() {
        // Arrange
        // sampleProjectDto1 está completamente llenado en setUp para que el mapper (ProjectDtoMapper.projectToClass) funcione.
        Project expectedSavedProject = new Project(
                new ProjectId(sampleProjectDto1.getProId()),
                new ProjectTitle(sampleProjectDto1.getProTitle()),
                sampleProjectDto1.getProDescription(),
                sampleProjectDto1.getProAbstract(),
                sampleProjectDto1.getProGoals(),
                new ProjectRegistrationDate(sampleProjectDto1.getProDate()),
                new ProjectDeadline(sampleProjectDto1.getProDeadLine()),
                sampleProjectDto1.getProBudget(),
                EnumProjectState.valueOf(sampleProjectDto1.getProState()),
                sampleProjectDto1.getIdcompany(),
                sampleProjectDto1.getProCoordinator()
        );

        when(projectRepositoryPortMock.save(any(Project.class))).thenReturn(expectedSavedProject);

        // Act
        projectService.receiveProject(sampleProjectDto1);

        // Assert
        ArgumentCaptor<Project> projectArgumentCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepositoryPortMock, times(1)).save(projectArgumentCaptor.capture());

        Project capturedProject = projectArgumentCaptor.getValue();
        assertNotNull(capturedProject, "El proyecto capturado para guardar no debería ser nulo.");
        assertEquals(sampleProjectDto1.getProId(), capturedProject.getProId().getValue(), "El ID del proyecto guardado no coincide.");
        assertEquals(sampleProjectDto1.getProTitle(), capturedProject.getProTitle().getValue(), "El título del proyecto guardado no coincide.");
        assertEquals(EnumProjectState.valueOf(sampleProjectDto1.getProState()), capturedProject.getProState(), "El estado del proyecto guardado no coincide.");
    }
}
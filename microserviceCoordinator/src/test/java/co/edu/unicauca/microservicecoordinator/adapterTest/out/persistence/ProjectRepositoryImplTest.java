package co.edu.unicauca.microservicecoordinator.adapterTest.out.persistence;


import co.edu.unicauca.microservicecoordinator.adapter.out.persistence.ProjectRepositoryImpl;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaProjectEntity;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository.JpaProjectRepository;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link ProjectRepositoryImpl}.
 * Estas pruebas verifican que el adaptador de persistencia para proyectos interactúa
 * correctamente con el {@link JpaProjectRepository} (mockeado) y utiliza los
 * mappers estáticos ({@code ProjectJpaMapper} y {@code ProjectDtoMapper}) de forma adecuada
 * para convertir entre entidades de dominio, entidades JPA y DTOs.
 */
@ExtendWith(MockitoExtension.class)
class ProjectRepositoryImplTest {

    /**
     * Mock del repositorio JPA {@link JpaProjectRepository}.
     * Simula las interacciones con la capa de acceso a datos de Spring Data JPA.
     */
    @Mock
    private JpaProjectRepository jpaRepositoryMock;

    /**
     * Instancia de {@link ProjectRepositoryImpl} bajo prueba.
     * El mock {@code jpaRepositoryMock} será inyectado en esta instancia.
     */
    @InjectMocks
    private ProjectRepositoryImpl projectRepositoryImpl;

    /**
     * Objeto de dominio {@link Project} de ejemplo, utilizado como entrada para algunas pruebas.
     */
    private Project sampleDomainProject;
    /**
     * Entidad JPA {@link JpaProjectEntity} de ejemplo, simula lo que el {@code JpaProjectRepository} devolvería.
     */
    private JpaProjectEntity sampleJpaEntity;
    /**
     * DTO {@link ProjectDto} de ejemplo, simula lo que algunos métodos del puerto deben devolver.
     */
    private ProjectDto sampleProjectDto; // No se usa directamente en los tests, pero es útil para la conceptualización
    /**
     * ID de proyecto {@link ProjectId} de ejemplo.
     */
    private ProjectId projectId;

    /**
     * Configuración inicial ejecutada antes de cada método de prueba.
     * Inicializa los objetos de ejemplo {@code Project}, {@code JpaProjectEntity} y {@code ProjectDto}
     * con datos consistentes para asegurar que los mappers (usados indirectamente) funcionen como se espera.
     */
    @BeforeEach
    void setUp() {
        projectId = new ProjectId("P001");

        sampleDomainProject = new Project(
                projectId,
                new ProjectTitle("Test Domain Project"),
                "Description", "Abstract", "Goals",
                new ProjectRegistrationDate(LocalDate.now()),
                new ProjectDeadline(12),
                1000.0, EnumProjectState.RECIBIDO,
                "CompanyX", "CoordinatorY"
        );

        sampleJpaEntity = new JpaProjectEntity();
        sampleJpaEntity.setProId(projectId);
        sampleJpaEntity.setProTitle(new ProjectTitle("Test JPA Project")); // Usado para la aserción
        sampleJpaEntity.setProDescription("JPA Description");
        sampleJpaEntity.setProAbstract("JPA Abstract");
        sampleJpaEntity.setProGoals("JPA Goals");
        sampleJpaEntity.setProDate(new ProjectRegistrationDate(LocalDate.now()));
        sampleJpaEntity.setProDeadLine(new ProjectDeadline(12));
        sampleJpaEntity.setProBudget(1000.0);
        sampleJpaEntity.setProState(EnumProjectState.RECIBIDO);
        sampleJpaEntity.setIdcompany("CompanyX");
        sampleJpaEntity.setProCoordinator("CoordinatorY");

        sampleProjectDto = new ProjectDto();
        sampleProjectDto.setProId(projectId.getValue());
        sampleProjectDto.setProTitle("Test DTO Project"); // Para referencia
        sampleProjectDto.setProState(EnumProjectState.RECIBIDO.name());
        sampleProjectDto.setProDate(LocalDate.now());
    }

    /**
     * Prueba el método {@code save} del repositorio.
     * Verifica que se llama al método {@code save} del {@code JpaProjectRepository}
     * y que el proyecto de dominio devuelto es el resultado del mapeo de la entidad JPA guardada.
     * Los mappers ({@code ProjectJpaMapper.toJpaEntity} y {@code ProjectJpaMapper.toDomainEntity})
     * se ejecutan con sus implementaciones reales.
     */
    @Test
    void save_shouldCallJpaRepositoryAndMappers_AndReturnDomainProject() {
        // Arrange
        when(jpaRepositoryMock.save(any(JpaProjectEntity.class))).thenReturn(sampleJpaEntity);

        // Act
        Project savedProject = projectRepositoryImpl.save(sampleDomainProject);

        // Assert
        assertNotNull(savedProject, "El proyecto guardado no debería ser nulo.");
        assertEquals(sampleJpaEntity.getProId().getValue(), savedProject.getProId().getValue(), "El ID del proyecto guardado no coincide.");
        assertEquals(sampleJpaEntity.getProTitle().getValue(), savedProject.getProTitle().getValue(), "El título del proyecto guardado no coincide.");
        verify(jpaRepositoryMock, times(1)).save(any(JpaProjectEntity.class));
    }

    /**
     * Prueba el método {@code findById} cuando el proyecto existe.
     * Verifica que se llama al {@code JpaProjectRepository}, y que la entidad JPA devuelta
     * se mapea correctamente a un {@link ProjectDto} a través de los mappers estáticos.
     */
    @Test
    void findById_shouldReturnOptionalProjectDto_whenProjectExists() {
        // Arrange
        when(jpaRepositoryMock.findById(eq(projectId))).thenReturn(Optional.of(sampleJpaEntity));

        // Act
        Optional<ProjectDto> resultDtoOptional = projectRepositoryImpl.findById(projectId.getValue());

        // Assert
        assertTrue(resultDtoOptional.isPresent(), "El Optional no debería estar vacío si el proyecto existe.");
        ProjectDto resultDto = resultDtoOptional.get();
        assertNotNull(resultDto, "El DTO resultante no debería ser nulo.");
        assertEquals(sampleJpaEntity.getProId().getValue(), resultDto.getProId(), "El ID del DTO no coincide.");
        assertEquals(sampleJpaEntity.getProTitle().getValue(), resultDto.getProTitle(), "El título del DTO no coincide.");
        verify(jpaRepositoryMock, times(1)).findById(eq(projectId));
    }

    /**
     * Prueba el método {@code findById} cuando el proyecto no existe.
     * Verifica que devuelve un {@link Optional#empty()}.
     */
    @Test
    void findById_shouldReturnEmptyOptional_whenProjectDoesNotExist() {
        // Arrange
        when(jpaRepositoryMock.findById(eq(projectId))).thenReturn(Optional.empty());

        // Act
        Optional<ProjectDto> resultDtoOptional = projectRepositoryImpl.findById(projectId.getValue());

        // Assert
        assertTrue(resultDtoOptional.isEmpty(), "El Optional debería estar vacío si el proyecto no existe.");
        verify(jpaRepositoryMock, times(1)).findById(eq(projectId));
    }

    /**
     * Prueba el método {@code findAll} cuando existen proyectos.
     * Verifica que se llama al {@code JpaProjectRepository} y que la lista de entidades JPA
     * se mapea correctamente a una lista de {@link ProjectDto}.
     */
    @Test
    void findAll_shouldReturnListOfProjectDtos() {
        // Arrange
        JpaProjectEntity anotherJpaEntity = new JpaProjectEntity();
        anotherJpaEntity.setProId(new ProjectId("P002"));
        anotherJpaEntity.setProTitle(new ProjectTitle("Another JPA Project"));
        anotherJpaEntity.setProDate(new ProjectRegistrationDate(LocalDate.now().minusDays(1)));
        anotherJpaEntity.setProState(EnumProjectState.ACEPTADO);
        anotherJpaEntity.setProDescription("Desc2");
        anotherJpaEntity.setProAbstract("Abs2");
        anotherJpaEntity.setProGoals("Goals2");
        anotherJpaEntity.setProDeadLine(new ProjectDeadline(6));

        List<JpaProjectEntity> jpaEntities = Arrays.asList(sampleJpaEntity, anotherJpaEntity);
        when(jpaRepositoryMock.findAll()).thenReturn(jpaEntities);

        // Act
        List<ProjectDto> resultDtos = projectRepositoryImpl.findAll();

        // Assert
        assertNotNull(resultDtos, "La lista de DTOs no debería ser nula.");
        assertEquals(2, resultDtos.size(), "El tamaño de la lista de DTOs no coincide.");
        assertEquals(sampleJpaEntity.getProId().getValue(), resultDtos.get(0).getProId());
        assertEquals(sampleJpaEntity.getProTitle().getValue(), resultDtos.get(0).getProTitle());
        assertEquals(anotherJpaEntity.getProId().getValue(), resultDtos.get(1).getProId());
        assertEquals(anotherJpaEntity.getProTitle().getValue(), resultDtos.get(1).getProTitle());
        verify(jpaRepositoryMock, times(1)).findAll();
    }

    /**
     * Prueba el método {@code findAll} cuando no existen proyectos.
     * Verifica que devuelve una lista vacía.
     */
    @Test
    void findAll_shouldReturnEmptyList_whenNoProjectsExist() {
        // Arrange
        when(jpaRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ProjectDto> resultDtos = projectRepositoryImpl.findAll();

        // Assert
        assertNotNull(resultDtos, "La lista de DTOs no debería ser nula (pero sí vacía).");
        assertTrue(resultDtos.isEmpty(), "La lista de DTOs debería estar vacía.");
        verify(jpaRepositoryMock, times(1)).findAll();
    }

    /**
     * Prueba el método {@code countByProState}.
     * Verifica que delega la llamada al {@code JpaProjectRepository} y devuelve el conteo.
     */
    @Test
    void countByProState_shouldReturnCountFromJpaRepository() {
        // Arrange
        EnumProjectState testState = EnumProjectState.EJECUCION;
        Long expectedCount = 5L;
        when(jpaRepositoryMock.countByProState(eq(testState))).thenReturn(expectedCount);

        // Act
        Long actualCount = projectRepositoryImpl.countByProState(testState);

        // Assert
        assertEquals(expectedCount, actualCount, "El conteo por estado no coincide.");
        verify(jpaRepositoryMock, times(1)).countByProState(eq(testState));
    }

    /**
     * Prueba el método {@code count}.
     * Verifica que delega la llamada al {@code JpaProjectRepository} y devuelve el conteo total,
     * casteado a {@code int} según la firma del puerto.
     */
    @Test
    void count_shouldReturnTotalCountFromJpaRepository() {
        // Arrange
        long expectedTotalCount = 10L;
        when(jpaRepositoryMock.count()).thenReturn(expectedTotalCount);

        // Act
        int actualTotalCount = projectRepositoryImpl.count();

        // Assert
        assertEquals((int) expectedTotalCount, actualTotalCount, "El conteo total no coincide.");
        verify(jpaRepositoryMock, times(1)).count();
    }
}
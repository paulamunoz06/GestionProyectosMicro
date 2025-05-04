package co.edu.unicauca.microservicecompany.ServiceTest;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import co.edu.unicauca.microserviceCompany.service.ProjectRegistrationService;
import co.edu.unicauca.microserviceCompany.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private IProjectRepository projectRepository;

    @Mock
    private ICompanyRepository companyRepository;

    @Mock
    private ICompanyService companyService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ProjectRegistrationService projectRegistrationService;

    private ProjectService projectService;

    @BeforeEach
    public void setup() {
        // Create the project service with mocked dependencies
        projectService = new ProjectService(rabbitTemplate, projectRepository, companyRepository, companyService);

        // Explicitly inject the mocked ProjectRegistrationService
        org.springframework.test.util.ReflectionTestUtils.setField(
                projectService, "projectRegistrationService", projectRegistrationService);
    }

    @Test
    public void shouldCreateProject() throws Exception {
        // Given
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProId("P001");
        projectDto.setProTitle("Test Project");
        projectDto.setProDescription("Test Description");
        projectDto.setProAbstract("Test Abstract");
        projectDto.setProGoals("Test Goals");
        projectDto.setProDeadLine(12);
        projectDto.setProBudget(10000.0);
        projectDto.setCompanyId("C001");
        projectDto.setProCoordinator("COORD001");

        Project project = new Project(
                "P001",
                "Test Project",
                "Test Description",
                "Test Abstract",
                "Test Goals",
                12,
                10000.0,
                "C001",
                "COORD001"
        );

        // Mock repository to verify company exists
        when(companyRepository.findById("C001")).thenReturn(Optional.of(new Company()));

        // Mock the ProjectRegistrationService to return our project
        when(projectRegistrationService.registerEntity(any(ProjectDto.class))).thenReturn(project);

        // When
        Project createdProject = projectService.createProject(projectDto);

        // Then
        assertNotNull(createdProject);
        assertEquals("P001", createdProject.getProId());
        assertEquals("Test Project", createdProject.getProTitle());

        // Verify interactions
        verify(companyRepository).findById("C001");
        verify(projectRegistrationService).registerEntity(any(ProjectDto.class));
    }

    @Test
    public void shouldFindProjectById() {
        // Given
        String projectId = "P001";
        Project project = new Project(
                projectId,
                "Test Project",
                "Test Description",
                "Test Abstract",
                "Test Goals",
                12,
                10000.0,
                "C001",
                "COORD001"
        );

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // When
        Optional<Project> foundProject = projectService.findById(projectId);

        // Then
        assertTrue(foundProject.isPresent());
        assertEquals(projectId, foundProject.get().getProId());

        verify(projectRepository).findById(projectId);
    }

    @Test
    public void shouldThrowExceptionWhenFindingProjectWithNullId() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> projectService.findById(null));
    }

    @Test
    public void shouldGetCompanyInfo() throws Exception {
        // Given
        String projectId = "P001";
        String companyId = "C001";

        Project project = new Project();
        project.setProId(projectId);
        project.setIdcompany(companyId);

        Company company = new Company();
        company.setId(companyId);
        company.setCompanyName("Test Company");

        CompanyDto companyDto = new CompanyDto();
        companyDto.setUserId(companyId);
        companyDto.setCompanyName("Test Company");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(companyRepository.findCompanyById(companyId)).thenReturn(Optional.of(company));
        when(companyService.companyToDto(company)).thenReturn(companyDto);

        // When
        CompanyDto result = projectService.getCompanyInfo(projectId);

        // Then
        assertNotNull(result);
        assertEquals(companyId, result.getUserId());
        assertEquals("Test Company", result.getCompanyName());

        verify(projectRepository).findById(projectId);
        verify(companyRepository).findCompanyById(companyId);
        verify(companyService).companyToDto(company);
    }

    @Test
    public void shouldConvertProjectToDto() {
        // Given
        Project project = new Project(
                "P001",
                "Test Project",
                "Test Description",
                "Test Abstract",
                "Test Goals",
                12,
                10000.0,
                "C001",
                "COORD001"
        );
        project.setProState(EnumProjectState.ACEPTADO);
        project.setProDate(LocalDate.now());

        // When
        ProjectDto dto = projectService.projectToDto(project);

        // Then
        assertNotNull(dto);
        assertEquals("P001", dto.getProId());
        assertEquals("Test Project", dto.getProTitle());
        assertEquals("Test Description", dto.getProDescription());
        assertEquals("Test Abstract", dto.getProAbstract());
        assertEquals("Test Goals", dto.getProGoals());
        assertEquals(12, dto.getProDeadLine());
        assertEquals(10000.0, dto.getProBudget());
        assertEquals("ACEPTADO", dto.getProState());
        assertEquals("C001", dto.getCompanyId());
    }

    @Test
    public void shouldConvertDtoToProject() {
        // Given
        ProjectDto dto = new ProjectDto();
        dto.setProId("P001");
        dto.setProTitle("Test Project");
        dto.setProDescription("Test Description");
        dto.setProAbstract("Test Abstract");
        dto.setProGoals("Test Goals");
        dto.setProDeadLine(12);
        dto.setProBudget(10000.0);
        dto.setIdcompany("C001");
        dto.setProCoordinator("COORD001");
        dto.setProState("ACEPTADO");
        LocalDate now = LocalDate.now();
        dto.setProDate(now);

        // When
        Project project = projectService.projectToClass(dto);

        // Then
        assertNotNull(project);
        assertEquals("P001", project.getProId());
        assertEquals("Test Project", project.getProTitle());
        assertEquals("Test Description", project.getProDescription());
        assertEquals("Test Abstract", project.getProAbstract());
        assertEquals("Test Goals", project.getProGoals());
        assertEquals(12, project.getProDeadLine());
        assertEquals(10000.0, project.getProBudget());
        assertEquals(EnumProjectState.ACEPTADO, project.getProState());
        assertEquals("C001", project.getIdcompany());
        assertEquals("COORD001", project.getProCoordinator());
        assertEquals(now, project.getProDate());
    }

    @Test
    public void shouldUpdateProject() {
        // Given
        String projectId = "P001";

        Project existingProject = new Project(
                projectId,
                "Original Title",
                "Original Description",
                "Original Abstract",
                "Original Goals",
                12,
                10000.0,
                "C001",
                "COORD001"
        );
        existingProject.setProState(EnumProjectState.RECIBIDO);

        ProjectDto updateDto = new ProjectDto();
        updateDto.setProId(projectId);
        updateDto.setProTitle("Updated Title");
        updateDto.setProDescription("Updated Description");
        updateDto.setProState("ACEPTADO");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Project updatedProject = projectService.updateProject(updateDto);

        // Then
        assertNotNull(updatedProject);
        assertEquals("Updated Title", updatedProject.getProTitle());
        assertEquals("Updated Description", updatedProject.getProDescription());
        assertEquals(EnumProjectState.ACEPTADO, updatedProject.getProState());

        // Original fields should remain unchanged
        assertEquals("Original Abstract", updatedProject.getProAbstract());
        assertEquals("Original Goals", updatedProject.getProGoals());
        assertEquals(12, updatedProject.getProDeadLine());
        assertEquals(10000.0, updatedProject.getProBudget());

        // Verify interactions
        verify(projectRepository).findById(projectId);
        verify(projectRepository).save(any(Project.class));
        verify(rabbitTemplate).convertAndSend(anyString(), any(ProjectDto.class));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistentProject() {
        // Given
        ProjectDto updateDto = new ProjectDto();
        updateDto.setProId("P999");
        updateDto.setProTitle("Updated Title");

        when(projectRepository.findById("P999")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(updateDto));

        verify(projectRepository).findById("P999");
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingWithNullDto() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> projectService.updateProject(null));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingWithNullId() {
        // Given
        ProjectDto updateDto = new ProjectDto();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> projectService.updateProject(updateDto));
    }
}
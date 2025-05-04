package co.edu.unicauca.microservicecompany.Controller;

import co.edu.unicauca.microserviceCompany.controller.ProjectController;
import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import co.edu.unicauca.microserviceCompany.service.IProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProjectService projectService;

    @MockBean
    private ICompanyService companyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldRegisterProject() throws Exception {
        // Given
        String companyId = "C001";

        ProjectDto projectDto = new ProjectDto();
        projectDto.setProId("P001");
        projectDto.setProTitle("Test Project");
        projectDto.setProDescription("Test Project Description");
        projectDto.setProAbstract("Test Abstract");
        projectDto.setProGoals("Test Goals");
        projectDto.setProDeadLine(12);
        projectDto.setProBudget(10000.0);

        Project project = new Project(
                "P001",
                "Test Project",
                "Test Project Description",
                "Test Abstract",
                "Test Goals",
                12,
                10000.0,
                companyId,
                "COORD001"
        );

        Company company = new Company();
        company.setId(companyId);

        when(companyService.findById(companyId)).thenReturn(Optional.of(company));
        when(projectService.createProject(any(ProjectDto.class))).thenReturn(project);
        when(projectService.projectToDto(any(Project.class))).thenReturn(projectDto);

        // When/Then
        mockMvc.perform(post("/project/register")
                        .param("companyId", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.proId", is("P001")))
                .andExpect(jsonPath("$.proTitle", is("Test Project")));

        verify(companyService).findById(companyId);
        verify(projectService).createProject(any(ProjectDto.class));
        verify(projectService).projectToDto(any(Project.class));
    }

    @Test
    public void shouldReturnNotFoundWhenRegisteringProjectWithNonExistentCompany() throws Exception {
        // Given
        String companyId = "C999";

        ProjectDto projectDto = new ProjectDto();
        projectDto.setProId("P001");
        projectDto.setProTitle("Test Project");

        when(companyService.findById(companyId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(post("/project/register")
                        .param("companyId", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());

        verify(companyService).findById(companyId);
        verify(projectService, never()).createProject(any(ProjectDto.class));
    }

    @Test
    public void shouldReturnBadRequestWhenRegisteringProjectWithInvalidData() throws Exception {
        // Given
        String companyId = "C001";

        ProjectDto projectDto = new ProjectDto();
        projectDto.setProId("P001");

        Company company = new Company();
        company.setId(companyId);

        when(companyService.findById(companyId)).thenReturn(Optional.of(company));
        when(projectService.createProject(any(ProjectDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid project data"));

        // When/Then
        mockMvc.perform(post("/project/register")
                        .param("companyId", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        verify(companyService).findById(companyId);
        verify(projectService).createProject(any(ProjectDto.class));
    }

    @Test
    public void shouldGetProjectById() throws Exception {
        // Given
        String projectId = "P001";

        Project project = new Project(
                projectId,
                "Test Project",
                "Test Project Description",
                "Test Abstract",
                "Test Goals",
                12,
                10000.0,
                "C001",
                "COORD001"
        );

        ProjectDto projectDto = new ProjectDto();
        projectDto.setProId(projectId);
        projectDto.setProTitle("Test Project");

        when(projectService.findById(projectId)).thenReturn(Optional.of(project));
        when(projectService.projectToDto(project)).thenReturn(projectDto);

        // When/Then
        mockMvc.perform(get("/project/{projectId}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.proId", is(projectId)))
                .andExpect(jsonPath("$.proTitle", is("Test Project")));

        verify(projectService).findById(projectId);
        verify(projectService).projectToDto(project);
    }

    @Test
    public void shouldReturnNotFoundWhenProjectDoesNotExist() throws Exception {
        // Given
        String projectId = "P999";

        when(projectService.findById(projectId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/project/{projectId}", projectId))
                .andExpect(status().isNotFound());

        verify(projectService).findById(projectId);
        verify(projectService, never()).projectToDto(any(Project.class));
    }

    @Test
    public void shouldCheckIfProjectExists() throws Exception {
        // Given
        String projectId = "P001";

        when(projectService.findById(projectId)).thenReturn(Optional.of(new Project()));

        // When/Then
        mockMvc.perform(get("/project/exists/{projectId}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists", is(true)));

        verify(projectService).findById(projectId);
    }

    @Test
    public void shouldCheckIfProjectDoesNotExist() throws Exception {
        // Given
        String projectId = "P999";

        when(projectService.findById(projectId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/project/exists/{projectId}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists", is(false)));

        verify(projectService).findById(projectId);
    }

    @Test
    public void shouldGetCompanyByProjectId() throws Exception {
        // Given
        String projectId = "P001";
        String companyId = "C001";

        CompanyDto companyDto = new CompanyDto();
        companyDto.setUserId(companyId);  // Field name in the DTO is userId, not companyId
        companyDto.setCompanyName("Test Company");

        when(projectService.getCompanyInfo(projectId)).thenReturn(companyDto);

        // When/Then
        mockMvc.perform(get("/project/{projectId}/company", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(companyId)))  // Match the actual field name in the DTO
                .andExpect(jsonPath("$.companyName", is("Test Company")));

        verify(projectService).getCompanyInfo(projectId);
    }

    @Test
    public void shouldUpdateProject() throws Exception {
        // Given
        String projectId = "P001";

        ProjectDto projectDto = new ProjectDto();
        projectDto.setProId(projectId);
        projectDto.setProTitle("Updated Project");
        projectDto.setProDescription("Updated Description");
        projectDto.setProState("ACEPTADO");

        Project updatedProject = new Project(
                projectId,
                "Updated Project",
                "Updated Description",
                "Test Abstract",
                "Test Goals",
                12,
                10000.0,
                "C001",
                "COORD001"
        );
        updatedProject.setProState(EnumProjectState.ACEPTADO);

        when(projectService.findById(projectId)).thenReturn(Optional.of(new Project()));
        when(projectService.updateProject(any(ProjectDto.class))).thenReturn(updatedProject);
        when(projectService.projectToDto(updatedProject)).thenReturn(projectDto);

        // When/Then
        mockMvc.perform(put("/project/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.proId", is(projectId)))
                .andExpect(jsonPath("$.proTitle", is("Updated Project")))
                .andExpect(jsonPath("$.proState", is("ACEPTADO")));

        verify(projectService).findById(projectId);
        verify(projectService).updateProject(any(ProjectDto.class));
        verify(projectService).projectToDto(updatedProject);
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistentProject() throws Exception {
        // Given
        String projectId = "P999";

        ProjectDto projectDto = new ProjectDto();
        projectDto.setProId(projectId);
        projectDto.setProTitle("Updated Project");

        when(projectService.findById(projectId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(put("/project/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());

        verify(projectService).findById(projectId);
        verify(projectService, never()).updateProject(any(ProjectDto.class));
    }

    @Test
    public void shouldReturnBadRequestWhenUpdatingWithInvalidData() throws Exception {
        // Given
        String projectId = "P001";

        ProjectDto projectDto = new ProjectDto();
        projectDto.setProId(projectId);
        projectDto.setProTitle("Updated Project");

        when(projectService.findById(projectId)).thenReturn(Optional.of(new Project()));
        when(projectService.updateProject(any(ProjectDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid project data"));

        // When/Then
        mockMvc.perform(put("/project/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        verify(projectService).findById(projectId);
        verify(projectService).updateProject(any(ProjectDto.class));
    }
}
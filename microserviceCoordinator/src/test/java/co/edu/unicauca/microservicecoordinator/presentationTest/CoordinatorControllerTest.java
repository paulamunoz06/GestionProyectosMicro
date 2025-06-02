package co.edu.unicauca.microservicecoordinator.presentationTest;


import co.edu.unicauca.microservicecoordinator.application.port.in.CoordinatorServicePort;
import co.edu.unicauca.microservicecoordinator.application.port.in.ProjectServicePort;
import co.edu.unicauca.microservicecoordinator.presentation.CoordinatorController;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para la clase {@link CoordinatorController}.
 * Estas pruebas utilizan {@link WebMvcTest} para enfocar la capa MVC, simulando peticiones HTTP
 * y verificando las respuestas del controlador. Las dependencias del controlador
 * ({@link ProjectServicePort} y {@link CoordinatorServicePort}) son mockeadas con {@link MockBean}.
 */
@WebMvcTest(CoordinatorController.class)
class CoordinatorControllerTest {

    /**
     * Instancia de {@link MockMvc} para realizar peticiones HTTP simuladas al controlador.
     * Inyectada automáticamente por Spring Boot Test.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock del puerto de servicio de proyectos.
     * Reemplaza la implementación real del servicio para aislar el controlador durante las pruebas.
     */
    @MockBean
    private ProjectServicePort projectServicePortMock;

    /**
     * Mock del puerto de servicio de coordinación.
     * Reemplaza la implementación real del servicio para aislar el controlador.
     */
    @MockBean
    private CoordinatorServicePort coordinatorServicePortMock;

    /**
     * Instancia de {@link ObjectMapper} para convertir objetos Java a/desde JSON.
     * Inyectada automáticamente por Spring Boot Test.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * DTO de proyecto de ejemplo.
     */
    private ProjectDto projectDto1;
    /**
     * Otro DTO de proyecto de ejemplo.
     */
    private ProjectDto projectDto2;

    /**
     * Configuración inicial ejecutada antes de cada método de prueba.
     * Crea instancias de {@link ProjectDto} de ejemplo que se utilizarán en las pruebas.
     */
    @BeforeEach
    void setUp() {
        projectDto1 = new ProjectDto();
        projectDto1.setProId("P001");
        projectDto1.setProTitle("Proyecto Alpha");
        projectDto1.setProState("RECIBIDO");
        projectDto1.setProDate(LocalDate.now());

        projectDto2 = new ProjectDto();
        projectDto2.setProId("P002");
        projectDto2.setProTitle("Proyecto Beta");
        projectDto2.setProState("ACEPTADO");
        projectDto2.setProDate(LocalDate.now().minusDays(1));
    }

    // --- Pruebas para los Endpoints del Controlador ---

    /**
     * Prueba el endpoint GET {@code /coordinator/projects}.
     * Verifica que devuelve una lista de {@link ProjectDto} y un estado HTTP 200 OK
     * cuando existen proyectos.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc.
     */
    @Test
    void getAllProjects_shouldReturnListOfProjects() throws Exception {
        // Arrange
        List<ProjectDto> mockProjects = Arrays.asList(projectDto1, projectDto2);
        when(projectServicePortMock.findAllProjects()).thenReturn(mockProjects);

        // Act & Assert
        mockMvc.perform(get("/coordinator/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].proId", is("P001")))
                .andExpect(jsonPath("$[0].proTitle", is("Proyecto Alpha")))
                .andExpect(jsonPath("$[1].proId", is("P002")));

        verify(projectServicePortMock).findAllProjects();
    }

    /**
     * Prueba el endpoint GET {@code /coordinator/projects}.
     * Verifica que devuelve una lista vacía y un estado HTTP 200 OK
     * cuando no existen proyectos.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc.
     */
    @Test
    void getAllProjects_shouldReturnEmptyList_whenNoProjectsExist() throws Exception {
        // Arrange
        when(projectServicePortMock.findAllProjects()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/coordinator/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(projectServicePortMock).findAllProjects();
    }

    /**
     * Prueba el endpoint GET {@code /coordinator/projects/count-by-status/{status}}.
     * Verifica que devuelve el conteo correcto para un estado dado y un estado HTTP 200 OK.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc.
     */
    @Test
    void countByStatus_shouldReturnCountForGivenStatus() throws Exception {
        // Arrange
        String status = "ACEPTADO";
        long count = 5L;
        when(projectServicePortMock.countByStatus(eq(status))).thenReturn(count);

        // Act & Assert
        mockMvc.perform(get("/coordinator/projects/count-by-status/{status}", status)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(count), Long.class));

        verify(projectServicePortMock).countByStatus(eq(status));
    }

    /**
     * Prueba el endpoint GET {@code /coordinator/projects/count-total}.
     * Verifica que devuelve el conteo total de proyectos y un estado HTTP 200 OK.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc.
     */
    @Test
    void countTotalProjects_shouldReturnTotalCount() throws Exception {
        // Arrange
        int totalCount = 10;
        when(projectServicePortMock.countTotalProjects()).thenReturn(totalCount);

        // Act & Assert
        mockMvc.perform(get("/coordinator/projects/count-total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(totalCount)));

        verify(projectServicePortMock).countTotalProjects();
    }

    /**
     * Prueba el endpoint PUT {@code /coordinator/projects/update-status} para una actualización exitosa.
     * Verifica que devuelve el {@link ProjectDto} actualizado y un estado HTTP 200 OK.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc o la serialización JSON.
     */
    @Test
    void updateProjectStatus_shouldReturnUpdatedProject_whenSuccessful() throws Exception {
        // Arrange
        ProjectDto requestDto = new ProjectDto();
        requestDto.setProId("P001");
        requestDto.setProState("ACEPTADO");

        ProjectDto updatedResponseDto = new ProjectDto();
        updatedResponseDto.setProId("P001");
        updatedResponseDto.setProTitle("Proyecto Alpha Actualizado");
        updatedResponseDto.setProState("ACEPTADO");
        updatedResponseDto.setProDate(LocalDate.now());

        when(coordinatorServicePortMock.evaluateProject(eq("P001"), eq("ACEPTADO")))
                .thenReturn(updatedResponseDto);

        // Act & Assert
        mockMvc.perform(put("/coordinator/projects/update-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.proId", is("P001")))
                .andExpect(jsonPath("$.proState", is("ACEPTADO")))
                .andExpect(jsonPath("$.proTitle", is("Proyecto Alpha Actualizado")));

        verify(coordinatorServicePortMock).evaluateProject(eq("P001"), eq("ACEPTADO"));
    }

    /**
     * Prueba el endpoint PUT {@code /coordinator/projects/update-status} cuando el servicio no puede actualizar el proyecto.
     * Verifica que devuelve un estado HTTP 400 Bad Request, simulando que el servicio devuelve {@code null}.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc o la serialización JSON.
     */
    @Test
    void updateProjectStatus_shouldReturnBadRequest_whenServiceReturnsNull() throws Exception {
        // Arrange
        ProjectDto requestDto = new ProjectDto();
        requestDto.setProId("P001");
        requestDto.setProState("ESTADO_INVALIDO_PARA_TRANSICION");

        when(coordinatorServicePortMock.evaluateProject(eq("P001"), eq("ESTADO_INVALIDO_PARA_TRANSICION")))
                .thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/coordinator/projects/update-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(coordinatorServicePortMock).evaluateProject(eq("P001"), eq("ESTADO_INVALIDO_PARA_TRANSICION"));
    }


    /**
     * Prueba el endpoint GET {@code /coordinator/project/{idProject}} cuando el proyecto se encuentra.
     * Verifica que devuelve el {@link ProjectDto} y un estado HTTP 200 OK.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc.
     */
    @Test
    void getProjectById_shouldReturnProject_whenFound() throws Exception {
        // Arrange
        String projectId = "P001";
        when(projectServicePortMock.findById(eq(projectId))).thenReturn(Optional.of(projectDto1));

        // Act & Assert
        mockMvc.perform(get("/coordinator/project/{idProject}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.proId", is(projectId)))
                .andExpect(jsonPath("$.proTitle", is(projectDto1.getProTitle())));

        verify(projectServicePortMock).findById(eq(projectId));
    }

    /**
     * Prueba el endpoint GET {@code /coordinator/project/{idProject}} cuando el proyecto no se encuentra.
     * Verifica que devuelve un estado HTTP 404 Not Found.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc.
     */
    @Test
    void getProjectById_shouldReturnNotFound_whenProjectDoesNotExist() throws Exception {
        // Arrange
        String projectId = "P999";
        when(projectServicePortMock.findById(eq(projectId))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/coordinator/project/{idProject}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(projectServicePortMock).findById(eq(projectId));
    }

    /**
     * Prueba el endpoint GET {@code /coordinator/project/{idProject}} cuando el servicio lanza una excepción.
     * Verifica que el controlador maneja la excepción y devuelve un estado HTTP 400 Bad Request
     * con un cuerpo JSON que contiene el mensaje de error.
     *
     * @throws Exception si ocurre un error durante la ejecución de la petición con MockMvc.
     */
    @Test
    void getProjectById_shouldReturnBadRequest_whenServiceThrowsException() throws Exception {
        // Arrange
        String projectId = "P_ERROR";
        String errorMessage = "Error simulado en el servicio";
        when(projectServicePortMock.findById(eq(projectId))).thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        mockMvc.perform(get("/coordinator/project/{idProject}", projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(errorMessage)));

        verify(projectServicePortMock).findById(eq(projectId));
    }
}
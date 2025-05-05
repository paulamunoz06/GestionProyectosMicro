package co.edu.unicauca.microservicecompany.Controller;

import co.edu.unicauca.microserviceCompany.controller.CompanyController;
import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para {@link CompanyController} utilizando {@link @WebMvcTest}.
 * Se enfoca en la capa de controlador, utilizando {@link MockMvc} para simular peticiones HTTP
 * y {@link MockBean} para simular la lógica del servicio {@link ICompanyService}.
 */
@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    /**
     * Utilizado para simular peticiones HTTP al controlador sin necesidad de iniciar un servidor real.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Utilizado para convertir objetos Java a JSON y viceversa durante las pruebas.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Simula el comportamiento del servicio real {@link ICompanyService} sin ejecutar la lógica real.
     */
    // Crea un mock del servicio real, simula la logica del servicio
    @MockBean
    private ICompanyService companyService;

    /**
     * Instancia de prueba de la entidad {@link Company}, utilizada en múltiples pruebas.
     */
    private Company testCompany;

    /**
     * Instancia de prueba de {@link CompanyDto}, que representa los datos que se enviarían o recibirían del controlador.
     */
    private CompanyDto testCompanyDto;

    /**
     * Inicializa los datos de prueba antes de cada método de prueba.
     * Se ejecuta automáticamente gracias a la anotación {@link BeforeEach}.
     */
    @BeforeEach
    void setUp() {
        // Setup test company
        testCompany = new Company(
                "comp123",
                "Test Company",
                "John",
                "Doe",
                "123456789",
                "CEO",
                EnumSector.TECHNOLOGY,
                "test@company.com"
        );

        // Setup test company DTO
        testCompanyDto = new CompanyDto();
        testCompanyDto.setUserId("comp123");
        testCompanyDto.setUserEmail("test@company.com");
        testCompanyDto.setUserPassword("password");
        testCompanyDto.setCompanyName("Test Company");
        testCompanyDto.setContactName("John");
        testCompanyDto.setContactLastName("Doe");
        testCompanyDto.setContactPhone("123456789");
        testCompanyDto.setContactPosition("CEO");
        testCompanyDto.setCompanySector("TECHNOLOGY");
    }

    /**
     * Verifica que el registro de una compañía con datos válidos
     * devuelva un código 201 Created y la información correspondiente en el cuerpo de la respuesta.
     */
    @Test
    void registerCompany_WithValidData_ShouldReturnCreated() throws Exception {
        when(companyService.registerCompany(any(CompanyDto.class))).thenReturn(testCompany);
        when(companyService.companyToDto(any(Company.class))).thenReturn(testCompanyDto);

        mockMvc.perform(post("/company/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCompanyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is("comp123")))
                .andExpect(jsonPath("$.userEmail", is("test@company.com")))
                .andExpect(jsonPath("$.companyName", is("Test Company")));

        verify(companyService, times(1)).registerCompany(any(CompanyDto.class));
    }

    /**
     * Verifica que al intentar registrar una compañía con datos inválidos
     * (por ejemplo, email duplicado), se devuelva un código 400 Bad Request con un mensaje de error.
     */
    @Test
    void registerCompany_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(companyService.registerCompany(any(CompanyDto.class)))
                .thenThrow(new IllegalArgumentException("Email already exists"));

        // Act & Assert
        mockMvc.perform(post("/company/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCompanyDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Email already exists")));

        verify(companyService, times(1)).registerCompany(any(CompanyDto.class));
    }

    /**
     * Verifica que al solicitar una compañía existente por su ID,
     * se devuelva un código 200 OK y la información correspondiente de la compañía.
     */
    @Test
    void getCompanyById_WithExistingId_ShouldReturnCompany() throws Exception {
        // Arrange
        when(companyService.findById("comp123")).thenReturn(Optional.of(testCompany));
        when(companyService.companyToDto(testCompany)).thenReturn(testCompanyDto);

        // Act & Assert
        mockMvc.perform(get("/company/comp123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("comp123")))
                .andExpect(jsonPath("$.companyName", is("Test Company")));

        verify(companyService, times(1)).findById("comp123");
    }

    /**
     * Verifica que al solicitar una compañía inexistente por su ID,
     * se devuelva un código 404 Not Found.
     */
    @Test
    void getCompanyById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(companyService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/company/nonexistent"))
                .andExpect(status().isNotFound());

        verify(companyService, times(1)).findById("nonexistent");
    }

    /**
     * Verifica que al solicitar una compañía existente por su email,
     * se devuelva un código 200 OK y la información correspondiente de la compañía.
     */
    @Test
    void getCompanyByEmail_WithExistingEmail_ShouldReturnCompany() throws Exception {
        // Arrange
        when(companyService.findByEmail("test@company.com")).thenReturn(Optional.of(testCompany));
        when(companyService.companyToDto(testCompany)).thenReturn(testCompanyDto);

        // Act & Assert
        mockMvc.perform(get("/company/email/test@company.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("comp123")))
                .andExpect(jsonPath("$.companyName", is("Test Company")));

        verify(companyService, times(1)).findByEmail("test@company.com");
    }

    /**
     * Verifica que al solicitar una compañía inexistente por su email,
     * se devuelva un código 404 Not Found.
     */
    @Test
    void getCompanyByEmail_WithNonExistingEmail_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(companyService.findByEmail("nonexistent@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/company/email/nonexistent@email.com"))
                .andExpect(status().isNotFound());

        verify(companyService, times(1)).findByEmail("nonexistent@email.com");
    }

    /**
     * Verifica que se devuelvan todas las compañías registradas
     * con un código 200 OK y una lista en formato JSON.
     */
    @Test
    void getAllCompanies_ShouldReturnAllCompanies() throws Exception {
        // Arrange
        Company company2 = new Company(
                "comp456",
                "Another Company",
                "Jane",
                "Smith",
                "987654321",
                "CTO",
                EnumSector.HEALTH,
                "another@company.com"
        );

        CompanyDto company2Dto = new CompanyDto();
        company2Dto.setUserId("comp456");
        company2Dto.setCompanyName("Another Company");
        company2Dto.setUserEmail("another@company.com");

        when(companyService.findAllCompanies()).thenReturn(Arrays.asList(testCompany, company2));
        when(companyService.companyToDto(testCompany)).thenReturn(testCompanyDto);
        when(companyService.companyToDto(company2)).thenReturn(company2Dto);

        // Act & Assert
        mockMvc.perform(get("/company/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is("comp123")))
                .andExpect(jsonPath("$[1].userId", is("comp456")));

        verify(companyService, times(1)).findAllCompanies();
    }

    /**
     * Verifica que se obtenga correctamente el ID de un sector dado su nombre,
     * devolviendo un código 200 OK.
     */
    @Test
    void getSectorIdByName_WithValidSector_ShouldReturnSectorId() throws Exception {
        // Arrange
        when(companyService.getSectorIdByName("TECHNOLOGY")).thenReturn("TECHNOLOGY");

        // Act & Assert
        mockMvc.perform(get("/company/sector/TECHNOLOGY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sectorId", is("TECHNOLOGY")));

        verify(companyService, times(1)).getSectorIdByName("TECHNOLOGY");
    }

    /**
     * Verifica que se devuelva correctamente la cantidad total de compañías registradas,
     * con un código 200 OK y un campo `count` en la respuesta.
     */
    @Test
    void getCompanyCount_ShouldReturnCount() throws Exception {
        // Arrange
        when(companyService.countAllCompanies()).thenReturn(5);

        // Act & Assert
        mockMvc.perform(get("/company/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(5)));

        verify(companyService, times(1)).countAllCompanies();
    }
}
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

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICompanyService companyService;

    private Company testCompany;
    private CompanyDto testCompanyDto;

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

    @Test
    void registerCompany_WithValidData_ShouldReturnCreated() throws Exception {
        // Arrange
        when(companyService.registerCompany(any(CompanyDto.class))).thenReturn(testCompany);
        when(companyService.companyToDto(any(Company.class))).thenReturn(testCompanyDto);

        // Act & Assert
        mockMvc.perform(post("/company/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCompanyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is("comp123")))
                .andExpect(jsonPath("$.userEmail", is("test@company.com")))
                .andExpect(jsonPath("$.companyName", is("Test Company")));

        verify(companyService, times(1)).registerCompany(any(CompanyDto.class));
    }

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

    @Test
    void getCompanyById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(companyService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/company/nonexistent"))
                .andExpect(status().isNotFound());

        verify(companyService, times(1)).findById("nonexistent");
    }

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

    @Test
    void getCompanyByEmail_WithNonExistingEmail_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(companyService.findByEmail("nonexistent@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/company/email/nonexistent@email.com"))
                .andExpect(status().isNotFound());

        verify(companyService, times(1)).findByEmail("nonexistent@email.com");
    }

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
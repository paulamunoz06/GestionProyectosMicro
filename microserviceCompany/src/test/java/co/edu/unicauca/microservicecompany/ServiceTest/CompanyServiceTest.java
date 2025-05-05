package co.edu.unicauca.microservicecompany.ServiceTest;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import co.edu.unicauca.microserviceCompany.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el servicio CompanyService.
 * Se verifica el comportamiento de los métodos relacionados con la manipulación de empresas
 * como la búsqueda por ID, búsqueda por email, conversión entre entidades y DTOs, y la gestión de sectores.
 */
@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    private ICompanyRepository companyRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CompanyService companyService;

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

    /**
     * Verifica que al buscar una empresa por un ID válido, se retorne la empresa correcta.
     * Se espera que el resultado sea una empresa con el nombre "Test Company".
     */
    @Test
    void findById_WithValidId_ShouldReturnCompany() {
        // Arrange
        when(companyRepository.findById("comp123")).thenReturn(Optional.of(testCompany));

        // Act
        Optional<Company> result = companyService.findById("comp123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Company", result.get().getCompanyName());
        verify(companyRepository, times(1)).findById("comp123");
    }

    /**
     * Verifica que al buscar una empresa con un ID nulo, se lance una excepción EntityNotFoundException.
     * Se espera que no se realice ninguna llamada al repositorio.
     */
    @Test
    void findById_WithNullId_ShouldThrowException() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            companyService.findById(null);
        });
        verify(companyRepository, never()).findById(any());
    }


    /**
     * Verifica que al buscar una empresa por un correo electrónico válido, se retorne la empresa correcta.
     * Se espera que el resultado sea una empresa con el ID "comp123".
     */
    @Test
    void findByEmail_WithValidEmail_ShouldReturnCompany() {
        // Arrange
        when(companyRepository.findByEmail("test@company.com")).thenReturn(Optional.of(testCompany));

        // Act
        Optional<Company> result = companyService.findByEmail("test@company.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("comp123", result.get().getId());
        verify(companyRepository, times(1)).findByEmail("test@company.com");
    }

    /**
     * Verifica que al buscar una empresa con un correo electrónico vacío, se lance una excepción EntityNotFoundException.
     * Se espera que no se realice ninguna llamada al repositorio.
     */
    @Test
    void findByEmail_WithEmptyEmail_ShouldThrowException() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            companyService.findByEmail("");
        });
        verify(companyRepository, never()).findByEmail(any());
    }


    /**
     * Verifica que se puedan recuperar todas las empresas almacenadas.
     * Se espera que el tamaño de la lista de empresas sea 2.
     */
    @Test
    void findAllCompanies_ShouldReturnAllCompanies() {
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
        when(companyRepository.findAll()).thenReturn(Arrays.asList(testCompany, company2));

        // Act
        List<Company> result = companyService.findAllCompanies();

        // Assert
        assertEquals(2, result.size());
        verify(companyRepository, times(1)).findAll();
    }


    /**
     * Verifica que la conversión de una entidad Company a un DTO CompanyDto funcione correctamente.
     * Se espera que todos los campos de la entidad sean correctamente mapeados al DTO.
     */
    @Test
    void companyToDto_ShouldConvertCompanyToDto() {
        // Act
        CompanyDto result = companyService.companyToDto(testCompany);

        // Assert
        assertEquals(testCompany.getId(), result.getUserId());
        assertEquals(testCompany.getEmail(), result.getUserEmail());
        assertEquals(testCompany.getCompanyName(), result.getCompanyName());
        assertEquals(testCompany.getContactName(), result.getContactName());
        assertEquals(testCompany.getContactLastName(), result.getContactLastName());
        assertEquals(testCompany.getContactPhone(), result.getContactPhone());
        assertEquals(testCompany.getContactPosition(), result.getContactPosition());
        assertEquals(testCompany.getCompanySector().toString(), result.getCompanySector());
        assertEquals("", result.getUserPassword()); // Password should be empty for security
    }

    /**
     * Verifica que la conversión de un DTO CompanyDto a una entidad Company funcione correctamente.
     * Se espera que todos los campos del DTO sean correctamente mapeados a la entidad.
     */
    @Test
    void companyToEntity_ShouldConvertDtoToCompany() {
        // Act
        Company result = companyService.companyToEntity(testCompanyDto);

        // Assert
        assertEquals(testCompanyDto.getUserId(), result.getId());
        assertEquals(testCompanyDto.getUserEmail(), result.getEmail());
        assertEquals(testCompanyDto.getCompanyName(), result.getCompanyName());
        assertEquals(testCompanyDto.getContactName(), result.getContactName());
        assertEquals(testCompanyDto.getContactLastName(), result.getContactLastName());
        assertEquals(testCompanyDto.getContactPhone(), result.getContactPhone());
        assertEquals(testCompanyDto.getContactPosition(), result.getContactPosition());
        assertEquals(EnumSector.TECHNOLOGY, result.getCompanySector());
    }

    /**
     * Verifica que al buscar el ID de un sector con un nombre válido, se retorne el ID del sector correspondiente.
     * Se espera que el resultado sea "TECHNOLOGY".
     */
    @Test
    void getSectorIdByName_WithValidSector_ShouldReturnSectorId() {
        // Act
        String result = companyService.getSectorIdByName("TECHNOLOGY");

        // Assert
        assertEquals("TECHNOLOGY", result);
    }

    /**
     * Verifica que al buscar el ID de un sector con un nombre no válido, se retorne "OTHER".
     * Se espera que el resultado sea "OTHER".
     */
    @Test
    void getSectorIdByName_WithInvalidSector_ShouldReturnOther() {
        // Act
        String result = companyService.getSectorIdByName("NONEXISTENT_SECTOR");

        // Assert
        assertEquals("OTHER", result);
    }
}

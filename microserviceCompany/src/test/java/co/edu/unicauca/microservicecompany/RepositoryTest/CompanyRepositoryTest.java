package co.edu.unicauca.microservicecompany.RepositoryTest;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la interfaz ICompanyRepository utilizando una base de datos en memoria.
 * Se verifica el comportamiento de los métodos personalizados de consulta y existencia.
 */
@DataJpaTest
public class CompanyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ICompanyRepository companyRepository;

    /**
     * Verifica que se pueda encontrar una compañía existente por su ID.
     * Se espera que el resultado esté presente y contenga los datos correctos.
     */
    @Test
    void findCompanyById_WithExistingId_ShouldReturnCompany() {
        // Arrange
        Company company = new Company(
                "comp123",
                "Test Company",
                "John",
                "Doe",
                "123456789",
                "CEO",
                EnumSector.TECHNOLOGY,
                "test@company.com"
        );
        entityManager.persist(company);
        entityManager.flush();

        // Act
        Optional<Company> found = companyRepository.findCompanyById("comp123");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Test Company", found.get().getCompanyName());
    }

    /**
     * Verifica que se pueda encontrar una compañía existente por su correo electrónico.
     * Se espera que el resultado esté presente y coincida con el nombre registrado.
     */
    @Test
    void findByEmail_WithExistingEmail_ShouldReturnCompany() {
        // Arrange
        Company company = new Company(
                "comp123",
                "Test Company",
                "John",
                "Doe",
                "123456789",
                "CEO",
                EnumSector.TECHNOLOGY,
                "test@company.com"
        );
        entityManager.persist(company);
        entityManager.flush();

        // Act
        Optional<Company> found = companyRepository.findByEmail("test@company.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Test Company", found.get().getCompanyName());
    }

    /**
     * Verifica que al buscar una compañía por un correo que no existe,
     * el resultado esté vacío.
     */
    @Test
    void findByEmail_WithNonExistingEmail_ShouldReturnEmpty() {
        // Act
        Optional<Company> found = companyRepository.findByEmail("nonexistent@email.com");

        // Assert
        assertFalse(found.isPresent());
    }


    /**
     * Verifica que el método existsByEmail retorne true si el correo electrónico existe en la base de datos.
     */
    @Test
    void existsByEmail_WithExistingEmail_ShouldReturnTrue() {
        // Arrange
        Company company = new Company(
                "comp123",
                "Test Company",
                "John",
                "Doe",
                "123456789",
                "CEO",
                EnumSector.TECHNOLOGY,
                "test@company.com"
        );
        entityManager.persist(company);
        entityManager.flush();

        // Act
        boolean exists = companyRepository.existsByEmail("test@company.com");

        // Assert
        assertTrue(exists);
    }

    /**
     * Verifica que el método existsByEmail retorne false si el correo electrónico no existe.
     */
    @Test
    void existsByEmail_WithNonExistingEmail_ShouldReturnFalse() {
        // Act
        boolean exists = companyRepository.existsByEmail("nonexistent@email.com");

        // Assert
        assertFalse(exists);
    }

    /**
     * Verifica que el método countAllCompanies retorne la cantidad correcta de compañías registradas.
     */
    @Test
    void countAllCompanies_ShouldReturnCorrectCount() {
        // Arrange
        Company company1 = new Company(
                "comp123",
                "Test Company 1",
                "John",
                "Doe",
                "123456789",
                "CEO",
                EnumSector.TECHNOLOGY,
                "test1@company.com"
        );
        Company company2 = new Company(
                "comp456",
                "Test Company 2",
                "Jane",
                "Smith",
                "987654321",
                "CTO",
                EnumSector.HEALTH,
                "test2@company.com"
        );
        entityManager.persist(company1);
        entityManager.persist(company2);
        entityManager.flush();

        // Act
        int count = companyRepository.countAllCompanies();

        // Assert
        assertEquals(2, count);
    }

    /**
     * Verifica que el método existsBySector retorne true si existe al menos una compañía en ese sector.
     */
    @Test
    void existsBySector_WithExistingSector_ShouldReturnTrue() {
        // Arrange
        Company company = new Company(
                "comp123",
                "Test Company",
                "John",
                "Doe",
                "123456789",
                "CEO",
                EnumSector.TECHNOLOGY,
                "test@company.com"
        );
        entityManager.persist(company);
        entityManager.flush();

        // Act
        boolean exists = companyRepository.existsBySector(EnumSector.TECHNOLOGY);

        // Assert
        assertTrue(exists);
    }

    /**
     * Verifica que el método existsBySector retorne false si no existe ninguna compañía en ese sector.
     */
    @Test
    void existsBySector_WithNonExistingSector_ShouldReturnFalse() {
        // Arrange
        Company company = new Company(
                "comp123",
                "Test Company",
                "John",
                "Doe",
                "123456789",
                "CEO",
                EnumSector.TECHNOLOGY,
                "test@company.com"
        );
        entityManager.persist(company);
        entityManager.flush();

        // Act
        boolean exists = companyRepository.existsBySector(EnumSector.HEALTH);

        // Assert
        assertFalse(exists);
    }
}
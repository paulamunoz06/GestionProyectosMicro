package co.edu.unicauca.mycompany.projects.domain.services;

import co.edu.unicauca.mycompany.projects.domain.entities.Company;
import co.edu.unicauca.mycompany.projects.domain.entities.enumSector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase DataValidationCompany.
 */
public class DataValidationCompanyTest {
    
    private DataValidationCompany validator;
    
    @BeforeEach
    void setUp() {
        Company validCompany = new Company("Empresa", "ContactoNombre", "ContactoApellido",
                "3121234567", "ContactoCargo", enumSector.OTHER,
                "123", "prueba@gmail.com", "Pass_word1");
        validator = new DataValidationCompany(validCompany);
    }

}
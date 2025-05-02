package co.edu.unicauca.mycompany.projects.domain.services;

import co.edu.unicauca.mycompany.projects.domain.entities.Project;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase DataValidationProject.
 */
public class DataValidationProjectTest {
    
    private Project validProject;
    
    @BeforeEach
    public void setUp() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date validDate = sdf.parse("10/03/2025");
        
        validProject = new Project("P001", "Proyecto Test", "Descripción válida", 
                                   "Resumen válido", "Objetivos válidos", 12, validDate, 
                                   50000.0, "C123");
    }

}
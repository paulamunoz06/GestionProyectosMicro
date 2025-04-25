package co.edu.unicauca.mycompany.projects.domain.entities;

import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Company.
 */
public class CompanyTest {
    /**
     * Prueba el constructor de la clase Company y los métodos getters. Se
     * verifica que los valores iniciales asignados sean correctos.
     */
    @Test
    void testCompanyConstructorAndGetters() {
        // Se crea una instancia de Company con valores de prueba.
        Company companyTest = new Company(
                "Tech Solutions", "John", "Doe", "1234567890", "CEO",
                enumSector.OTHER, "5678", "company@example.com", "securepassword"
        );

        // Se validan los valores esperados con los valores obtenidos de los getters.
        assertThat(companyTest.getCompanyName()).isEqualTo("Tech Solutions");
        assertThat(companyTest.getContactName()).isEqualTo("John");
        assertThat(companyTest.getContactLastName()).isEqualTo("Doe");
        assertThat(companyTest.getContactPhone()).isEqualTo("1234567890");
        assertThat(companyTest.getContactPosition()).isEqualTo("CEO");
        assertThat(companyTest.getCompanySector()).isEqualTo(enumSector.OTHER);
        assertThat(companyTest.getUserId()).isEqualTo("5678");
        assertThat(companyTest.getUserEmail()).isEqualTo("company@example.com");
        assertThat(companyTest.getUserPassword()).isEqualTo("securepassword");
    }

    /**
     * Prueba los métodos setters de la clase Company. Se verifica que los
     * valores se actualicen correctamente.
     */
    @Test
    void testSetters() {
        // Se crea una instancia de Company con valores iniciales vacíos.
        Company company = new Company("", "", "", "", "", enumSector.HEALTH, "", "", "");

        // Se actualizan los valores usando los setters.
        company.setCompanyName("Innovative Corp");
        company.setContactName("Alice");
        company.setContactLastName("Smith");
        company.setContactPhone("9876543210");
        company.setContactPosition("CTO");
        company.setCompanySector(enumSector.HEALTH);
        company.setUserId("9999");
        company.setUserEmail("contact@innovative.com");
        company.setUserPassword("newpassword");

        // Se validan los valores modificados con los valores esperados.
        assertThat(company.getCompanyName()).isEqualTo("Innovative Corp");
        assertThat(company.getContactName()).isEqualTo("Alice");
        assertThat(company.getContactLastName()).isEqualTo("Smith");
        assertThat(company.getContactPhone()).isEqualTo("9876543210");
        assertThat(company.getContactPosition()).isEqualTo("CTO");
        assertThat(company.getCompanySector()).isEqualTo(enumSector.HEALTH);
        assertThat(company.getUserId()).isEqualTo("9999");
        assertThat(company.getUserEmail()).isEqualTo("contact@innovative.com");
        assertThat(company.getUserPassword()).isEqualTo("newpassword");
    }
    /**
     * Test of addProject method, of class Company.
     */
    /*
    @Test
    
    public void testAddProject() {
        System.out.println("addProject");
        String proId = "";
        String proTitle = "";
        String proDescription = "";
        String proAbstract = "";
        String proGoals = "";
        int proDeadLine = 0;
        Date proDate = null;
        double proBudget = 0.0;
        Company instance = null;
        instance.addProject(proId, proTitle, proDescription, proAbstract, proGoals, proDeadLine, proDate, proBudget);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
 */   
}

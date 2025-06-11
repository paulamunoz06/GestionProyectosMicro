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

}
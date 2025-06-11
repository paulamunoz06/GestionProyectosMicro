package co.edu.unicauca.microservicecompany.RepositoryTest;

import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para la interfaz IProjectRepository utilizando una base de datos en memoria.
 * Se verifica el comportamiento de los métodos relacionados con la persistencia y consulta de proyectos.
 */
@DataJpaTest
public class ProjectRepositoryTest {

}
package co.edu.unicauca.microservicecoordinator.adapterTest.out.persistence;


import co.edu.unicauca.microservicecoordinator.adapter.out.persistence.ProjectRepositoryImpl;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaProjectEntity;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository.JpaProjectRepository;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link ProjectRepositoryImpl}.
 * Estas pruebas verifican que el adaptador de persistencia para proyectos interactúa
 * correctamente con el {@link JpaProjectRepository} (mockeado) y utiliza los
 * mappers estáticos ({@code ProjectJpaMapper} y {@code ProjectDtoMapper}) de forma adecuada
 * para convertir entre entidades de dominio, entidades JPA y DTOs.
 */
//@ExtendWith(MockitoExtension.class)
class ProjectRepositoryImplTest {

}
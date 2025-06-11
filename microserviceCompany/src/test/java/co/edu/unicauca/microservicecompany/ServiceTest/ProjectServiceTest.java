package co.edu.unicauca.microservicecompany.ServiceTest;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import co.edu.unicauca.microserviceCompany.service.ProjectRegistrationService;
import co.edu.unicauca.microserviceCompany.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el servicio ProjectService.
 * Se verifica el comportamiento de los métodos relacionados con la manipulación de proyectos
 * como la creación, búsqueda, actualización, conversión entre entidades y DTOs, y la obtención de información de la empresa asociada.
 */
//@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

}
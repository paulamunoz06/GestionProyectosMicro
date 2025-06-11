package co.edu.unicauca.microservicecoordinator.adapterTest.in.rest;


import co.edu.unicauca.microservicecoordinator.adapter.in.rest.ProjectService;
import co.edu.unicauca.microservicecoordinator.application.port.out.ProjectRepositoryPort;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectDeadline;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectId;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectRegistrationDate;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectTitle;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link ProjectService}.
 * Estas pruebas verifican la lógica del servicio de proyectos, asegurando que delega
 * correctamente las operaciones al {@link ProjectRepositoryPort} y maneja la lógica
 * de negocio simple, como la conversión de estados y el procesamiento de proyectos recibidos.
 */
//@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

}
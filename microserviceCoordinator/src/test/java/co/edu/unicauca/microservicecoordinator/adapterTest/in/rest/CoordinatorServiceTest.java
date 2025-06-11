package co.edu.unicauca.microservicecoordinator.adapterTest.in.rest;

import co.edu.unicauca.microservicecoordinator.adapter.in.rest.CoordinatorService;
import co.edu.unicauca.microservicecoordinator.application.port.out.ProjectRepositoryPort;
import co.edu.unicauca.microservicecoordinator.domain.model.*;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.infraestructure.config.RabbitMQConfig;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link CoordinatorService}.
 * Estas pruebas verifican la lógica del servicio de coordinación, asegurando que interactúa
 * correctamente con sus dependencias (mockeadas) y maneja diferentes escenarios
 * de evaluación de proyectos.
 */
class CoordinatorServiceTest {

}
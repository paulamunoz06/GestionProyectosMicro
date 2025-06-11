package co.edu.unicauca.microservicecompany.ServiceTest;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import co.edu.unicauca.microserviceCompany.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el servicio CompanyService.
 * Se verifica el comportamiento de los métodos relacionados con la manipulación de empresas
 * como la búsqueda por ID, búsqueda por email, conversión entre entidades y DTOs, y la gestión de sectores.
 */
//@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

}

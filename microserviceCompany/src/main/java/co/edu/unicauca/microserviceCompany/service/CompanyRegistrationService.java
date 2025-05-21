package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

// Eliminar: import co.edu.unicauca.microserviceCompany.entity.User;
// Eliminar: import co.edu.unicauca.microserviceCompany.infra.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación concreta del servicio de registro para empresas.
 */
public class CompanyRegistrationService extends AbstractEntityRegistrationService<Company, CompanyDto> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyRegistrationService.class);

    private final ICompanyRepository companyRepository;

    public CompanyRegistrationService(ICompanyRepository companyRepository, RabbitTemplate rabbitTemplate) {
        this.companyRepository = companyRepository;
    }

    @Override
    protected void validateData(CompanyDto companyDto) throws IllegalArgumentException {
        if (companyDto == null) {
            throw new IllegalArgumentException("La información de la empresa (DTO) no puede ser nula.");
        }
        // El userId y userEmail ahora vienen del token y son seteados por el controlador.
        // Es crucial que estén presentes.
        if (companyDto.getUserId() == null || companyDto.getUserId().isEmpty()) {
            throw new IllegalArgumentException("El ID de usuario (proveniente del token) no puede estar vacío en el DTO.");
        }
        if (companyDto.getUserEmail() == null || companyDto.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico del usuario (proveniente del token) no puede estar vacío en el DTO.");
        }
        if (companyDto.getCompanyName() == null || companyDto.getCompanyName().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío.");
        }
        // No hay userPassword para validar
    }

    @Override
    protected void checkEntityExists(CompanyDto companyDto) throws IllegalArgumentException {
        if (companyRepository.existsById(companyDto.getUserId())) {
            throw new IllegalArgumentException("Ya existe una empresa registrada para el usuario con ID: " + companyDto.getUserId());
        }
    }

    @Override
    protected void preRegisterOperations(CompanyDto companyDto) throws Exception {
        logger.info("Operaciones previas al registro para la empresa del usuario ID: {}. La gestión de usuarios es externa (Keycloak).", companyDto.getUserId());
    }


    @Override
    protected Company dtoToEntity(CompanyDto companyDto) {
        EnumSector sector;
        try {
            // Validar que companySector no sea nulo o vacío antes de convertir
            if (companyDto.getCompanySector() == null || companyDto.getCompanySector().trim().isEmpty()) {
                logger.warn("Sector de la empresa no especificado o vacío para el usuario {}. Usando 'OTHER'.", companyDto.getUserId());
                sector = EnumSector.OTHER; // O lanzar error si el sector es obligatorio
            } else {
                sector = EnumSector.valueOf(companyDto.getCompanySector().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Valor de sector inválido '{}' para el usuario {}. Usando 'OTHER'.", companyDto.getCompanySector(), companyDto.getUserId());
            sector = EnumSector.OTHER;
        }

        // El ID de la entidad Company es el userId del DTO (que vino del token).
        // El Email de la entidad Company es el userEmail del DTO (que vino del token, o es un email de contacto).
        return new Company(
                companyDto.getUserId(),       // ID de la empresa (del token)
                companyDto.getCompanyName(),
                companyDto.getContactName(),
                companyDto.getContactLastName(),
                companyDto.getContactPhone(),
                companyDto.getContactPosition(),
                sector,
                companyDto.getUserEmail()    // Email de contacto de la empresa (del token)
        );
    }

    @Override
    protected Company saveEntity(Company company) {
        logger.info("Guardando entidad Company con ID (userId de Keycloak): {}", company.getId());
        return companyRepository.save(company);
    }

    @Override
    protected void postRegisterOperations(Company company) throws Exception {
        logger.info("Empresa registrada exitosamente con ID (userId de Keycloak): {}", company.getId());
    }
}
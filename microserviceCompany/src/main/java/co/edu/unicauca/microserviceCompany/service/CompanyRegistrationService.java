package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación concreta del servicio de registro para empresas.
 */
public class CompanyRegistrationService extends AbstractEntityRegistrationService<Company, CompanyDto> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyRegistrationService.class);

    private final ICompanyRepository companyRepository;
    private final RabbitTemplate rabbitTemplate; // Este puede ser null si no se usa para nada en esta clase

    public CompanyRegistrationService(ICompanyRepository companyRepository, RabbitTemplate rabbitTemplate) {
        this.companyRepository = companyRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected void validateData(CompanyDto companyDto) throws IllegalArgumentException {
        if (companyDto == null) {
            throw new IllegalArgumentException("La información de la empresa (CompanyDto) no puede ser nula.");
        }
        // Se espera que userId y userEmail ya vengan poblados en el CompanyDto
        // (desde el token o desde la creación en Keycloak por el controlador)
        if (companyDto.getUserId() == null || companyDto.getUserId().isEmpty()) {
            throw new IllegalArgumentException("El ID de usuario (userId en CompanyDto) no puede estar vacío.");
        }
        if (companyDto.getUserEmail() == null || companyDto.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico del usuario (userEmail en CompanyDto) no puede estar vacío.");
        }
        if (companyDto.getCompanyName() == null || companyDto.getCompanyName().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío.");
        }
        // No hay userPassword en CompanyDto para validar.
    }


    @Override
    protected void checkEntityExists(CompanyDto companyDto) throws IllegalArgumentException {
        // El ID de la entidad Company es el userId de Keycloak (que viene en companyDto.getUserId()).
        // Verificamos si ya existe un perfil de empresa asociado a este ID de usuario.
        if (companyRepository.existsById(companyDto.getUserId())) {
            // Esto podría ser un caso de actualización en lugar de un error,
            // dependiendo de cómo lo maneje el CompanyController y el CompanyService.
            // Si este servicio es SOLO para nuevos registros, entonces lanzar la excepción es correcto.
            throw new IllegalArgumentException("Ya existe un perfil de empresa registrado para el usuario con ID: " + companyDto.getUserId());
        }
    }

    @Override
    protected void preRegisterOperations(CompanyDto companyDto) throws Exception {
        logger.info("Operaciones previas al registro del perfil de empresa para el usuario ID: {}. La gestión de usuarios es externa (Keycloak).", companyDto.getUserId());
        // YA NO SE CREA User NI SE ENVÍA A RABBITMQ PARA CREAR EL USUARIO.
        // User userSaved = new User();
        // userSaved.setId(companyDto.getUserId());
        // userSaved.setPassword(companyDto.getUserPassword()); // userPassword ya no está en CompanyDto
        // userSaved.setEmail(companyDto.getUserEmail());
        // userSaved.setRole(3);
        // rabbitTemplate.convertAndSend(RabbitMQConfig.USER_QUEUE, userSaved);
    }

    @Override
    protected Company dtoToEntity(CompanyDto companyDto) {
        EnumSector sector;
        try {
            if (companyDto.getCompanySector() == null || companyDto.getCompanySector().trim().isEmpty()) {
                sector = EnumSector.OTHER; // Valor por defecto si no se proporciona o es inválido
            } else {
                sector = EnumSector.valueOf(companyDto.getCompanySector().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Valor de sector inválido '{}' para la empresa del usuario {}. Usando 'OTHER'.",
                    companyDto.getCompanySector(), companyDto.getUserId());
            sector = EnumSector.OTHER;
        }

        // companyDto.getUserId() es el ID del usuario de Keycloak
        // companyDto.getUserEmail() es el email del usuario de Keycloak (o un email de contacto)
        return new Company(
                companyDto.getUserId(),
                companyDto.getCompanyName(),
                companyDto.getContactName(),
                companyDto.getContactLastName(),
                companyDto.getContactPhone(),
                companyDto.getContactPosition(),
                sector,
                companyDto.getUserEmail()
        );
    }

    @Override
    protected Company saveEntity(Company company) {
        logger.info("Guardando perfil de entidad Company con ID (Keycloak User ID): {}", company.getId());
        return companyRepository.save(company);
    }

    @Override
    protected void postRegisterOperations(Company company) throws Exception {
        logger.info("Perfil de empresa registrado/actualizado exitosamente con ID (Keycloak User ID): {}", company.getId());
        // Si necesitas notificar a otros servicios que un PERFIL DE EMPRESA fue creado/actualizado,
        // podrías enviar un mensaje a RabbitMQ aquí usando `this.rabbitTemplate`.
        // Por ejemplo, a una cola diferente de la USER_QUEUE.
    }
}
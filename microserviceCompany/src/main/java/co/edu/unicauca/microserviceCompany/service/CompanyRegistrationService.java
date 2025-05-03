package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.entity.User;
import co.edu.unicauca.microserviceCompany.infra.config.RabbitMQConfig;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Implementación concreta del servicio de registro para empresas.
 */
public class CompanyRegistrationService extends AbstractEntityRegistrationService<Company, CompanyDto> {

    private final ICompanyRepository companyRepository;
    private final RabbitTemplate rabbitTemplate;

    public CompanyRegistrationService(ICompanyRepository companyRepository, RabbitTemplate rabbitTemplate) {
        this.companyRepository = companyRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected void validateData(CompanyDto companyDto) throws IllegalArgumentException {
        if (companyDto == null) {
            throw new IllegalArgumentException("La información de la empresa no puede ser nula");
        }

        if (companyDto.getUserEmail() == null || companyDto.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede estar vacío");
        }

        if (companyDto.getCompanyName() == null || companyDto.getCompanyName().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío");
        }
    }

    @Override
    protected void checkEntityExists(CompanyDto companyDto) throws IllegalArgumentException {
        if (companyRepository.existsByEmail(companyDto.getUserEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }
    }

    @Override
    protected void preRegisterOperations(CompanyDto companyDto) throws Exception {
        User userSaved = new User();
        userSaved.setId(companyDto.getUserId());
        userSaved.setPassword(companyDto.getUserPassword());
        userSaved.setEmail(companyDto.getUserEmail());
        userSaved.setRole(3);  // Rol para la empresa

        // Publicar la información de usuario en RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_QUEUE, userSaved);
    }

    @Override
    protected Company dtoToEntity(CompanyDto companyDto) {
        EnumSector sector;
        try {
            sector = EnumSector.valueOf(companyDto.getCompanySector());
        } catch (IllegalArgumentException e) {
            sector = EnumSector.OTHER;
        }

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
        return companyRepository.save(company);
    }

    @Override
    protected void postRegisterOperations(Company company) throws Exception {
        // No se requieren operaciones adicionales después del registro para las empresas
        // pero podrías implementar notificaciones, auditoría, etc.
    }
}
package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements ICompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    private final ICompanyRepository companyRepository;
    private final RabbitTemplate rabbitTemplate; // Puede ser opcional si no se usa en esta clase directamente
    private final CompanyRegistrationService companyRegistrationService;

    @Autowired
    public CompanyService(ICompanyRepository companyRepository, RabbitTemplate rabbitTemplate) {
        this.companyRepository = companyRepository;
        this.rabbitTemplate = rabbitTemplate; // rabbitTemplate se pasa a CompanyRegistrationService
        this.companyRegistrationService = new CompanyRegistrationService(this.companyRepository, this.rabbitTemplate);
    }

    @Override
    @Transactional
    public Company registerCompany(CompanyDto companyDto) throws Exception {
        // Se asume que companyDto.userId y companyDto.userEmail ya están poblados
        // por el CompanyController (desde el token o desde la creación en Keycloak).
        logger.info("Servicio: Registrando perfil de empresa para userId: {}", companyDto.getUserId());
        return companyRegistrationService.registerEntity(companyDto);
    }

    /**
     * Actualiza los datos del perfil de una empresa existente.
     *
     * @param companyId El ID de la empresa a actualizar (corresponde al userId de Keycloak).
     * @param companyDto DTO con los nuevos datos del perfil de la empresa.
     * @return La entidad Company actualizada.
     * @throws IllegalArgumentException si los IDs no coinciden o el DTO es nulo.
     * @throws EntityNotFoundException si la empresa no se encuentra con el companyId proporcionado.
     * @throws Exception para otros errores durante el proceso.
     */
    @Override
    @Transactional
    public Company updateCompany(String companyId, CompanyDto companyDto) throws Exception {
        logger.info("Servicio: Intentando actualizar empresa con ID: {}", companyId);

        if (!StringUtils.hasText(companyId)) {
            throw new IllegalArgumentException("El ID de la empresa para actualizar no puede ser nulo o vacío.");
        }
        if (companyDto == null) {
            throw new IllegalArgumentException("El DTO de la empresa para actualizar no puede ser nulo.");
        }
        // Opcional: Verificar si el DTO contiene un userId y si coincide con companyId
        if (StringUtils.hasText(companyDto.getUserId()) && !companyDto.getUserId().equals(companyId)) {
            logger.warn("Conflicto de ID en actualización: Path ID '{}' no coincide con DTO ID '{}'. Se usará el Path ID.", companyId, companyDto.getUserId());
            // Podrías lanzar una excepción si esto no es permitido.
        }


        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> {
                    logger.warn("No se encontró empresa con ID: {} para actualizar.", companyId);
                    return new EntityNotFoundException("No se encontró empresa con ID: " + companyId + " para actualizar.");
                });

        // Actualizar campos de la entidad existente con valores del DTO
        if (StringUtils.hasText(companyDto.getCompanyName())) {
            existingCompany.setCompanyName(companyDto.getCompanyName());
        }
        if (StringUtils.hasText(companyDto.getContactName())) {
            existingCompany.setContactName(companyDto.getContactName());
        }
        if (StringUtils.hasText(companyDto.getContactLastName())) {
            existingCompany.setContactLastName(companyDto.getContactLastName());
        }
        if (StringUtils.hasText(companyDto.getContactPhone())) {
            existingCompany.setContactPhone(companyDto.getContactPhone());
        }
        if (StringUtils.hasText(companyDto.getContactPosition())) {
            existingCompany.setContactPosition(companyDto.getContactPosition());
        }
        if (StringUtils.hasText(companyDto.getCompanySector())) {
            try {
                existingCompany.setCompanySector(EnumSector.valueOf(companyDto.getCompanySector().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("Sector inválido '{}' durante actualización. No se cambiará.", companyDto.getCompanySector());
            }
        }
        // Si el userEmail en el DTO representa un nuevo email de CONTACTO para la empresa
        // y este puede ser diferente del email del usuario en Keycloak.
        if (StringUtils.hasText(companyDto.getUserEmail()) && !existingCompany.getEmail().equals(companyDto.getUserEmail())) {
            logger.info("Actualizando email de contacto de la empresa {} de {} a {}", companyId, existingCompany.getEmail(), companyDto.getUserEmail());
            existingCompany.setEmail(companyDto.getUserEmail());
        }

        Company updatedCompany = companyRepository.save(existingCompany);
        logger.info("Empresa con ID {} actualizada exitosamente.", companyId);
        return updatedCompany;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findById(String id) {
        if (!StringUtils.hasText(id)) {
            // Log o lanzar excepción si se prefiere, o simplemente devolver vacío.
            return Optional.empty();
        }
        return companyRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }
        return companyRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public CompanyDto companyToDto(Company company) {
        if (company == null) {
            return null;
        }
        CompanyDto dto = new CompanyDto();
        dto.setUserId(company.getId()); // ID de la entidad es el userId de Keycloak
        dto.setUserEmail(company.getEmail()); // Email de contacto de la empresa
        // No hay userPassword
        dto.setCompanyName(company.getCompanyName());
        dto.setContactName(company.getContactName());
        dto.setContactLastName(company.getContactLastName());
        dto.setContactPhone(company.getContactPhone());
        dto.setContactPosition(company.getContactPosition());
        if (company.getCompanySector() != null) {
            dto.setCompanySector(company.getCompanySector().toString());
        }
        return dto;
    }

    @Override
    public Company companyToEntity(CompanyDto companyDto) {
        // Este método es llamado por CompanyRegistrationService.
        // Asegurarse que userId y userEmail estén presentes si se llama desde otro lado.
        if (companyDto == null || !StringUtils.hasText(companyDto.getUserId()) || !StringUtils.hasText(companyDto.getUserEmail())) {
            throw new IllegalArgumentException("CompanyDto, userId, o userEmail no pueden ser nulos/vacíos para convertir a entidad.");
        }
        EnumSector sector;
        try {
            if (!StringUtils.hasText(companyDto.getCompanySector())) {
                sector = EnumSector.OTHER;
            } else {
                sector = EnumSector.valueOf(companyDto.getCompanySector().toUpperCase());
            }
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
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return companyRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public int countAllCompanies() {
        // Tu ICompanyRepository tiene countAllCompanies(). Si no, usa companyRepository.count()
        return companyRepository.countAllCompanies();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySector(EnumSector sector) {
        if (sector == null) {
            return false;
        }
        // Tu ICompanyRepository tiene existsBySector(). Si no, necesitarías una query.
        return companyRepository.existsBySector(sector);
    }

    @Override
    public String getSectorIdByName(String sectorName) {
        if (!StringUtils.hasText(sectorName)) {
            return EnumSector.OTHER.toString(); // Devuelve un valor por defecto o lanza error
        }
        try {
            EnumSector sector = EnumSector.valueOf(sectorName.toUpperCase());
            return sector.toString();
        } catch (IllegalArgumentException e) {
            return EnumSector.OTHER.toString();
        }
    }

}
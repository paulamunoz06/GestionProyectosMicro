package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las empresas.
 * Implementa la interfaz ICompanyService y proporciona la lógica de negocio para registrar, actualizar,
 * consultar y contar las empresas.
 */
@Service
public class CompanyService implements ICompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    private ICompanyRepository companyRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Servicio que implementa el patrón Template Method para el registro de empresas
    private final CompanyRegistrationService companyRegistrationService;

    @Autowired
    public CompanyService(ICompanyRepository companyRepository, RabbitTemplate rabbitTemplate) {
        this.companyRepository = companyRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.companyRegistrationService = new CompanyRegistrationService(companyRepository, rabbitTemplate);
    }

    /**
     * Registra una nueva empresa.
     * Utiliza el patrón Template Method para estructurar el proceso de registro.
     *
     * @param companyDto DTO con la información de la empresa a registrar.
     * @return La empresa registrada.
     * @throws IllegalArgumentException si los datos de la empresa son inválidos.
     * @throws Exception si ocurre algún error durante el registro.
     */
    @Override
    @Transactional
    public Company registerCompany(CompanyDto companyDto) throws Exception {
        // Utilizamos el servicio de registro basado en el patrón Template Method
        return companyRegistrationService.registerEntity(companyDto);
    }

    /**
     * Actualiza los datos de una empresa existente.
     *
     * @param companyId El ID de la empresa a actualizar (corresponde al userId de Keycloak).
     * @param companyDto DTO con los nuevos datos de la empresa.
     * @return La empresa actualizada.
     * @throws IllegalArgumentException si los IDs no coinciden o el DTO es nulo.
     * @throws EntityNotFoundException si la empresa no se encuentra con el companyId proporcionado.
     * @throws Exception para otros errores durante el proceso.
     */

    @Transactional
    public Company updateCompany(String companyId, CompanyDto companyDto) throws Exception{
        logger.info("Intentando actualizar empresa con ID: {}", companyId);

        // Validación de parámetros de entrada
        if (!StringUtils.hasText(companyId)) {
            throw new IllegalArgumentException("El ID de la empresa para actualizar no puede ser nulo o vacío.");
        }
        if (companyDto == null) {
            throw new IllegalArgumentException("El DTO de la empresa para actualizar no puede ser nulo.");
        }

        if (StringUtils.hasText(companyDto.getUserId()) && !companyDto.getUserId().equals(companyId)) {
            logger.warn("Conflicto de ID: ID en path ({}) no coincide con ID en DTO ({}). Se usará el ID del path.", companyId, companyDto.getUserId());

        }

        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> {
                    logger.warn("No se encontró empresa con ID: {} para actualizar.", companyId);
                    return new EntityNotFoundException("No se encontró empresa con ID: " + companyId + " para actualizar.");
                });

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
                EnumSector newSector = EnumSector.valueOf(companyDto.getCompanySector().toUpperCase());
                existingCompany.setCompanySector(newSector);
            } catch (IllegalArgumentException e) {
                logger.warn("Sector inválido '{}' proporcionado durante la actualización de la empresa {}. El sector no se cambiará o se podría asignar uno por defecto.",
                        companyDto.getCompanySector(), companyId);
            }
        }

        if (StringUtils.hasText(companyDto.getUserEmail()) && !existingCompany.getEmail().equals(companyDto.getUserEmail())) {

            logger.info("Actualizando email de contacto de la empresa {} de {} a {}", companyId, existingCompany.getEmail(), companyDto.getUserEmail());
            existingCompany.setEmail(companyDto.getUserEmail());
        }

        Company updatedCompany = companyRepository.save(existingCompany);
        logger.info("Empresa con ID {} actualizada exitosamente.", companyId);


        return updatedCompany;
    }

    @Override
    @Transactional
    public Company updateCompany(Company company) throws Exception {
        if (company == null || company.getId() == null || company.getId().isEmpty()) {
            throw new IllegalArgumentException("La información de la empresa o su ID no pueden ser nulos/vacíos para actualizar.");
        }

        if (!companyRepository.existsById(company.getId())) {
            throw new EntityNotFoundException("No existe una empresa con el ID proporcionado: " + company.getId());
        }
        // Aquí, la entidad 'company' que llega ya no debería tener información sensible como la contraseña.
        // El mapeo de DTO a Entidad antes de llamar a este método debería haberlo manejado.
        return companyRepository.save(company);
    }

    // Método de actualización alternativo (más común para APIs)
    @Transactional
    public Company updateCompany(String companyId, CompanyDto companyDto) throws Exception {
        if (!StringUtils.hasText(companyId)) {
            throw new IllegalArgumentException("El ID de la empresa para actualizar no puede ser nulo o vacío.");
        }
        if (companyDto == null) {
            throw new IllegalArgumentException("El DTO de la empresa para actualizar no puede ser nulo.");
        }

        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró empresa con ID: " + companyId));

        // Actualizar campos permitidos de existingCompany con companyDto
        if (StringUtils.hasText(companyDto.getCompanyName())) existingCompany.setCompanyName(companyDto.getCompanyName());
        if (StringUtils.hasText(companyDto.getContactName())) existingCompany.setContactName(companyDto.getContactName());
        if (StringUtils.hasText(companyDto.getContactLastName())) existingCompany.setContactLastName(companyDto.getContactLastName());
        if (StringUtils.hasText(companyDto.getContactPhone())) existingCompany.setContactPhone(companyDto.getContactPhone());
        if (StringUtils.hasText(companyDto.getContactPosition())) existingCompany.setContactPosition(companyDto.getContactPosition());
        if (StringUtils.hasText(companyDto.getCompanySector())) {
            try {
                existingCompany.setCompanySector(EnumSector.valueOf(companyDto.getCompanySector().toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("Sector inválido durante actualización: {}", companyDto.getCompanySector());
            }
        }
        // El email asociado al usuario (ID) generalmente no se cambia aquí.
        // Si tienes un email de contacto de la empresa *distinto* al del usuario, podrías actualizarlo.
        // if (StringUtils.hasText(companyDto.getUserEmail()) && !existingCompany.getEmail().equals(companyDto.getUserEmail())) {
        //    existingCompany.setEmail(companyDto.getUserEmail());
        // }

        return companyRepository.save(existingCompany);
    }


    /**
     * Busca una empresa por su ID.
     *
     * @param id Identificador único de la empresa.
     * @return Un objeto Optional con la empresa encontrada.
     * @throws EntityNotFoundException si no se encuentra la empresa.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findById(String id) {
        if (id == null || id.isEmpty()) { // Es mejor verificar isEmpty también
            // throw new EntityNotFoundException("Id de la empresa es nulo o vacío"); // Opcional: lanzar o devolver vacío
            return Optional.empty();
        }
        return companyRepository.findById(id);
    }

    /**
     * Busca una empresa por su correo electrónico.
     *
     * @param email Correo electrónico de la empresa.
     * @return Un objeto Optional con la empresa encontrada.
     * @throws EntityNotFoundException si no se encuentra la empresa.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            // throw new EntityNotFoundException("Email de la empresa es nulo o vacío"); // Opcional
            return Optional.empty();
        }
        return companyRepository.findByEmail(email);
    }

    /**
     * Obtiene todas las empresas registradas.
     *
     * @return Lista de todas las empresas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    /**
     * Convierte una entidad Company en su DTO correspondiente.
     *
     * @param company Entidad Company a convertir.
     * @return El DTO correspondiente a la empresa.
     */
    @Override
    public CompanyDto companyToDto(Company company) {
        if (company == null) return null;
        CompanyDto dto = new CompanyDto();
        dto.setUserId(company.getId()); // El ID de la entidad Company es el userId
        dto.setUserEmail(company.getEmail()); // El email de la entidad Company
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
    /**
     * Convierte un DTO de empresa en su entidad correspondiente.
     *
     * @param companyDto DTO con los datos de la empresa.
     * @return La entidad Company correspondiente.
     */
    @Override
    public Company companyToEntity(CompanyDto companyDto) {
        // Este método se usa principalmente en CompanyRegistrationService.
        // Si se llama desde otro lugar, asegurarse que userId y userEmail estén poblados.
        if (companyDto == null || companyDto.getUserId() == null || companyDto.getUserEmail() == null) {
            throw new IllegalArgumentException("CompanyDto, userId o userEmail no pueden ser nulos para convertir a entidad.");
        }
        EnumSector sector;
        try {
            if (companyDto.getCompanySector() == null || companyDto.getCompanySector().trim().isEmpty()) {
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
    public boolean existsByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return companyRepository.existsByEmail(email);
    }

    @Override
    public int countAllCompanies() {
        // Asumiendo que ICompanyRepository no tiene countAllCompanies() pero JpaRepository sí tiene count()
        return (int) companyRepository.count();
    }

    @Override
    public boolean existsBySector(EnumSector sector) {
        if (sector == null) {
            return false;
        }
        // Asumiendo que tienes un método en ICompanyRepository como:
        // boolean existsByCompanySector(EnumSector companySector);
        return companyRepository.existsByCompanySector(sector);
    }

    @Override
    public String getSectorIdByName(String sectorName) {
        try {
            if(sectorName == null || sectorName.trim().isEmpty()) return EnumSector.OTHER.toString();
            EnumSector sector = EnumSector.valueOf(sectorName.toUpperCase());
            return sector.toString();
        } catch (IllegalArgumentException e) {
            return EnumSector.OTHER.toString();
        }
    }
}
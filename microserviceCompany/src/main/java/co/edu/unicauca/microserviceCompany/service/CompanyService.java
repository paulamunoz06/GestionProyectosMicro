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

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las empresas.
 * Implementa la interfaz ICompanyService y proporciona la lógica de negocio para registrar, actualizar,
 * consultar y contar las empresas.
 */
@Service
public class CompanyService implements ICompanyService {

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
     * Valida que la empresa no sea nula y que exista en la base de datos.
     *
     * @param company Objeto de la empresa con los nuevos datos.
     * @return La empresa actualizada.
     * @throws IllegalArgumentException si los datos de la empresa son inválidos.
     * @throws EntityNotFoundException si la empresa no existe en la base de datos.
     */
    @Override
    @Transactional
    public Company updateCompany(Company company) throws Exception {
        if (company == null) {
            throw new IllegalArgumentException("La información de la empresa no puede ser nula");
        }

        if (company.getId() == null) {
            throw new IllegalArgumentException("El ID de la empresa no puede ser nulo");
        }

        if (!companyRepository.existsById(company.getId())) {
            throw new EntityNotFoundException("No existe una empresa con el ID proporcionado");
        }

        return companyRepository.save(company);
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
        if (id == null) {
            throw new EntityNotFoundException("Id de la empresa es nulo");
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
            throw new EntityNotFoundException("Email de la empresa es nulo o vacío");
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
        CompanyDto companyDto = new CompanyDto();

        companyDto.setUserId(company.getId().toString());
        companyDto.setUserEmail(company.getEmail());
        companyDto.setUserPassword(""); // Por seguridad no se devuelve la contraseña
        companyDto.setCompanyName(company.getCompanyName());
        companyDto.setContactName(company.getContactName());
        companyDto.setContactLastName(company.getContactLastName());
        companyDto.setContactPhone(company.getContactPhone());
        companyDto.setContactPosition(company.getContactPosition());
        companyDto.setCompanySector(company.getCompanySector().toString());

        return companyDto;
    }

    /**
     * Convierte un DTO de empresa en su entidad correspondiente.
     *
     * @param companyDto DTO con los datos de la empresa.
     * @return La entidad Company correspondiente.
     */
    @Override
    public Company companyToEntity(CompanyDto companyDto) {
        EnumSector sector;
        try {
            sector = EnumSector.valueOf(companyDto.getCompanySector());
        } catch (IllegalArgumentException e) {
            sector = EnumSector.OTHER;
        }

        Company company = new Company(
                companyDto.getUserId(),
                companyDto.getCompanyName(),
                companyDto.getContactName(),
                companyDto.getContactLastName(),
                companyDto.getContactPhone(),
                companyDto.getContactPosition(),
                sector,
                companyDto.getUserEmail()
        );

        return company;
    }

    /**
     * Verifica si una empresa existe por su correo electrónico.
     *
     * @param email Correo electrónico de la empresa.
     * @return true si la empresa existe, false en caso contrario.
     */
    @Override
    public boolean existsByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return companyRepository.existsByEmail(email);
    }

    /**
     * Cuenta el número total de empresas registradas en la base de datos.
     *
     * @return El número total de empresas.
     */
    @Override
    public int countAllCompanies() {
        return companyRepository.countAllCompanies();
    }

    /**
     * Verifica si existe alguna empresa en un sector específico.
     *
     * @param sector Sector a verificar.
     * @return true si existen empresas en ese sector, false en caso contrario.
     */
    @Override
    public boolean existsBySector(EnumSector sector) {
        if (sector == null) {
            return false;
        }
        return companyRepository.existsBySector(sector);
    }

    /**
     * Obtiene el identificador de un sector a partir de su nombre.
     *
     * @param sectorName Nombre del sector.
     * @return El identificador del sector, o el valor "OTHER" si el sector no es válido.
     */
    @Override
    public String getSectorIdByName(String sectorName) {
        try {
            EnumSector sector = EnumSector.valueOf(sectorName.toUpperCase());
            return sector.toString();
        } catch (IllegalArgumentException e) {
            return EnumSector.OTHER.toString();
        }
    }
}
package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los métodos de servicio para gestionar las operaciones relacionadas con las empresas.
 * Esta interfaz proporciona la definición de métodos para registrar, actualizar, consultar y contar las empresas,
 * así como convertir entre entidades y DTOs.
 */
public interface ICompanyService {

    /**
     * Registra una nueva empresa.
     *
     * @param companyDto DTO con la información de la empresa a registrar.
     * @return La empresa registrada.
     * @throws Exception Si ocurre un error durante el registro.
     */
    Company registerCompany(CompanyDto companyDto) throws Exception;

    /**
     * Actualiza los datos de una empresa existente.
     *
     * @param company Objeto de la empresa con los nuevos datos.
     * @return La empresa actualizada.
     * @throws Exception Si ocurre un error durante la actualización.
     */
    Company updateCompany(String companyId, CompanyDto companyDto) throws Exception;

    /**
     * Busca una empresa por su ID.
     *
     * @param id Identificador único de la empresa.
     * @return Un objeto Optional con la empresa encontrada.
     */
    Optional<Company> findById(String id);

    /**
     * Busca una empresa por su correo electrónico.
     *
     * @param email Correo electrónico de la empresa.
     * @return Un objeto Optional con la empresa encontrada.
     */
    Optional<Company> findByEmail(String email);

    /**
     * Obtiene todas las empresas registradas.
     *
     * @return Lista de todas las empresas.
     */
    List<Company> findAllCompanies();

    /**
     * Convierte una entidad Company en su DTO correspondiente.
     *
     * @param company Entidad Company a convertir.
     * @return El DTO correspondiente a la empresa.
     */
    CompanyDto companyToDto(Company company);

    /**
     * Convierte un DTO de empresa en su entidad correspondiente.
     *
     * @param companyDto DTO con los datos de la empresa.
     * @return La entidad Company correspondiente.
     */
    Company companyToEntity(CompanyDto companyDto);

    /**
     * Verifica si una empresa existe por su correo electrónico.
     *
     * @param email Correo electrónico de la empresa.
     * @return true si la empresa existe, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Cuenta el número total de empresas registradas en la base de datos.
     *
     * @return El número total de empresas.
     */
    int countAllCompanies();

    /**
     * Verifica si existe alguna empresa en un sector específico.
     *
     * @param sector Sector a verificar.
     * @return true si existen empresas en ese sector, false en caso contrario.
     */
    boolean existsBySector(EnumSector sector);

    /**
     * Obtiene el identificador de un sector a partir de su nombre.
     *
     * @param sectorName Nombre del sector.
     * @return El identificador del sector, o el valor "OTHER" si el sector no es válido.
     */
    String getSectorIdByName(String sectorName);
}
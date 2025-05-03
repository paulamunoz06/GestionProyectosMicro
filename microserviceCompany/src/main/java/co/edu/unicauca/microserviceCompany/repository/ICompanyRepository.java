package co.edu.unicauca.microserviceCompany.repository;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de la entidad Company en la base de datos.
 * Extiende JpaRepository para realizar operaciones CRUD básicas.
 */
@Repository
public interface ICompanyRepository extends JpaRepository<Company, String> {

    /**
     * Busca una empresa por su correo electrónico.
     *
     * @param email Correo electrónico de la empresa a buscar.
     * @return Un {@link Optional} que contiene la empresa si se encuentra, vacío si no.
     */
    Optional<Company> findByEmail(String email);

    /**
     * Busca una empresa por su identificador único.
     *
     * @param id Identificador único de la empresa.
     * @return Un {@link Optional} que contiene la empresa si se encuentra, vacío si no.
     */
    @Query("SELECT c FROM Company c WHERE c.id = :id")
    Optional<Company> findCompanyById(String id);

    /**
     * Verifica si una empresa con el correo electrónico proporcionado ya existe en la base de datos.
     *
     * @param email Correo electrónico de la empresa a verificar.
     * @return true si la empresa existe, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Cuenta el total de empresas registradas en la base de datos.
     *
     * @return El número total de empresas registradas.
     */
    @Query("SELECT COUNT(c) FROM Company c")
    int countAllCompanies();

    /**
     * Verifica si existen empresas asociadas a un sector específico.
     *
     * @param sector Sector de la empresa a verificar.
     * @return true si existen empresas en ese sector, false en caso contrario.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c WHERE c.companySector = :sector")
    boolean existsBySector(EnumSector sector);
}
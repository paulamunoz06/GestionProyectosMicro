package co.edu.unicauca.microserviceCompany.repository;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, String> {

    Optional<Company> findByEmail(String email);

    @Query("SELECT c FROM Company c WHERE c.id = :id")
    Optional<Company> findCompanyById(String id);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(c) FROM Company c")
    int countAllCompanies();

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c WHERE c.companySector = :sector")
    boolean existsBySector(EnumSector sector);
}
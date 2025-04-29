package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;

import java.util.List;
import java.util.Optional;

public interface ICompanyService {

    Company registerCompany(CompanyDto companyDto) throws Exception;

    Optional<Company> findById(Long id);

    Optional<Company> findByEmail(String email);

    List<Company> findAllCompanies();

    CompanyDto companyToDto(Company company);

    Company companyToEntity(CompanyDto companyDto);

    boolean existsByEmail(String email);

    int countAllCompanies();

    boolean existsBySector(EnumSector sector);

    String getSectorIdByName(String sectorName);
}

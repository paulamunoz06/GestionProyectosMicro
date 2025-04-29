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

@Service
public class CompanyService implements ICompanyService {

    @Autowired
    private ICompanyRepository companyRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Company registerCompany(CompanyDto companyDto) throws Exception {
        if (companyDto == null) {
            throw new IllegalArgumentException("La información de la empresa no puede ser nula");
        }

        if (companyRepository.existsByEmail(companyDto.getUserEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        Company company = companyToEntity(companyDto);
        return companyRepository.save(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findById(Long id) {
        if (id == null) {
            throw new EntityNotFoundException("Id de la empresa es nulo");
        }
        return companyRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new EntityNotFoundException("Email de la empresa es nulo o vacío");
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

    @Override
    public Company companyToEntity(CompanyDto companyDto) {
        EnumSector sector;
        try {
            sector = EnumSector.valueOf(companyDto.getCompanySector());
        } catch (IllegalArgumentException e) {
            sector = EnumSector.OTHER;
        }

        Company company = new Company(
                companyDto.getCompanyName(),
                companyDto.getContactName(),
                companyDto.getContactLastName(),
                companyDto.getContactPhone(),
                companyDto.getContactPosition(),
                sector,
                companyDto.getUserEmail(),
                companyDto.getUserPassword()
        );

        return company;
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
        return companyRepository.countAllCompanies();
    }

    @Override
    public boolean existsBySector(EnumSector sector) {
        if (sector == null) {
            return false;
        }
        return companyRepository.existsBySector(sector);
    }

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
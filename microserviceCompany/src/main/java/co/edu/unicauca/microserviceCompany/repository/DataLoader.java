package co.edu.unicauca.microserviceCompany.repository;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.EnumSector;
import co.edu.unicauca.microserviceCompany.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    IProjectRepository projectRepository;
    @Autowired
    ICompanyRepository companyRepository;
    @Override
    @Transactional
    public void run(String... args){
        projectRepository.deleteAll();
        Company company = new Company();
        company.setId("juanvela");
        company.setCompanyName("Test Company");
        company.setEmail("juanvela@example.com");
        company.setContactName("JuanVela");
        company.setContactLastName("JuanVela");
        company.setCompanySector(EnumSector.EDUCATION);
        company.setContactPosition("Position");
        company.setContactPhone("Tu Puto Telefono");
        companyRepository.save(company);

    }
}

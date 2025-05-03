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
        company.setContactPhone("Tu telefono");
        companyRepository.save(company);

        // Proyecto 1
        Project project1 = new Project();
        project1.setProId("P1");
        project1.setProTitle("Sistema de Gestión");
        project1.setProDescription("Sistema para gestionar recursos");
        project1.setProAbstract("Resumen del sistema");
        project1.setProGoals("Objetivos claros");
        project1.setProDeadLine(30);
        project1.setProBudget(10000.0);
        project1.setProState(EnumProjectState.RECIBIDO);
        project1.setProDate(LocalDate.now());
        project1.setIdcompany("juanvela");

        Project project2 = new Project();
        project2.setProId("P2");
        project2.setProTitle("Aplicación de Seguimiento de Salud");
        project2.setProDescription("Crear una app móvil que permita a los usuarios llevar el control de su salud diaria.");
        project2.setProAbstract("App para registro de actividad física, alimentación y signos vitales.");
        project2.setProGoals("Promover hábitos saludables mediante el seguimiento diario.");
        project2.setProDate(LocalDate.now());
        project2.setProDeadLine(8);
        project2.setProBudget(30000.0);
        project2.setProState(EnumProjectState.RECIBIDO);
        project2.setIdcompany("juanvela");

        Project project3 = new Project();
        project3.setProId("P3");
        project3.setProTitle("Aplicación de Seguimiento de Salud");
        project3.setProDescription("Crear una app móvil que permita a los usuarios llevar el control de su salud diaria.");
        project3.setProAbstract("App para registro de actividad física, alimentación y signos vitales.");
        project3.setProGoals("Promover hábitos saludables mediante el seguimiento diario.");
        project3.setProDate(LocalDate.now());
        project3.setProDeadLine(8);
        project3.setProBudget(30000.0);
        project3.setProState(EnumProjectState.RECIBIDO);
        project3.setIdcompany("juanvela");

        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);
    }
}

package co.edu.unicauca.microserviceCompany.repository;

import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
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

    @Override
    @Transactional
    public void run(String... args){
        projectRepository.deleteAll();

        // Proyecto 1 con estudiante postulado
        Project project1 = new Project();
        project1.setProId("1");
        project1.setProTitle("Sistema de Gestión");
        project1.setProDescription("Sistema para gestionar recursos");
        project1.setProAbstract("Resumen del sistema");
        project1.setProGoals("Objetivos claros");
        project1.setProDeadLine(30);
        project1.setProBudget(10000.0);
        project1.setProState(EnumProjectState.ACEPTADO);
        project1.setProDate(LocalDate.now());
        projectRepository.save(project1);

        // Proyecto 2 con estudiante aprobado
        Project project2 = new Project();
        project2.setProId("2");
        project2.setProTitle("Aplicación Móvil");
        project2.setProDescription("App para gestión personal");
        project2.setProAbstract("Resumen de la app");
        project2.setProGoals("Metas específicas");
        project2.setProDeadLine(45);
        project2.setProBudget(8000.0);
        project2.setProState(EnumProjectState.ACEPTADO);
        project2.setProDate(LocalDate.now());


        projectRepository.save(project2);

    }
}

package co.edu.unicauca.microservicecoordinator.repository;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class DataLoader {

    @Autowired
    private IProjectRepository projectRepository;

    @PostConstruct
    public void loadData() {
        if (projectRepository.count() == 0) { // Para no duplicar datos si ya existen
            Project project1 = new Project();
            project1.setProId("P001");
            project1.setProTitle("Sistema de Gestión Académica");
            project1.setProDescription("Desarrollar un sistema completo para la gestión académica de una universidad.");
            project1.setProAbstract("Sistema para matrícula, notas y reportes académicos.");
            project1.setProGoals("Automatizar la gestión académica.");
            project1.setProDate(LocalDate.now());
            project1.setProDeadLine(12);
            project1.setProBudget(50000.0);
            project1.setProState(EnumProjectState.ACEPTADO);

            Project project2 = new Project();
            project2.setProId("P002");
            project2.setProTitle("Aplicación de Seguimiento de Salud");
            project2.setProDescription("Crear una app móvil que permita a los usuarios llevar el control de su salud diaria.");
            project2.setProAbstract("App para registro de actividad física, alimentación y signos vitales.");
            project2.setProGoals("Promover hábitos saludables mediante el seguimiento diario.");
            project2.setProDate(LocalDate.now());
            project2.setProDeadLine(8);
            project2.setProBudget(30000.0);
            project2.setProState(EnumProjectState.CERRADO);

            Project project3 = new Project();
            project3.setProId("P003");
            project3.setProTitle("Aplicación de Seguimiento de Salud");
            project3.setProDescription("Crear una app móvil que permita a los usuarios llevar el control de su salud diaria.");
            project3.setProAbstract("App para registro de actividad física, alimentación y signos vitales.");
            project3.setProGoals("Promover hábitos saludables mediante el seguimiento diario.");
            project3.setProDate(LocalDate.now());
            project3.setProDeadLine(8);
            project3.setProBudget(30000.0);
            project3.setProState(EnumProjectState.RECHAZADO);

            projectRepository.save(project1);
            projectRepository.save(project2);
            projectRepository.save(project3);

            System.out.println(">>>> Se cargaron proyectos de prueba en la base de datos <<<<");
        }
    }
}

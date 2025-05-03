package co.edu.unicauca.microservicecoordinator.repository;

import co.edu.unicauca.microservicecoordinator.entities.Coordinator;
import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.service.CoordinatorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class DataLoader {

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private CoordinatorService coordinatorService;
    @Autowired
    private CoordinatorRepository coordinatorRepository;

    @PostConstruct
    public void loadData() {
        if (projectRepository.count() == 0) {
            // coordinador
            Coordinator user2 = new Coordinator();
            user2.setCoordinatorId("paulamunoz");
            user2.setCoordinatorPassword("passwordPaula");
            user2.setCoordinatorEmail("paulamunoz@example.com");
            coordinatorRepository.save(user2);

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
}

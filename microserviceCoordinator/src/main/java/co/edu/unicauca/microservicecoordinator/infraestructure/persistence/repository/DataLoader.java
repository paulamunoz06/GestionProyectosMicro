package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.adapter.in.rest.CoordinatorService;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectDeadline;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectId;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectRegistrationDate;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectTitle;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaCoordinatorEntity;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaProjectEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class DataLoader {

    @Autowired
    private JpaProjectRepository projectRepository;
    @Autowired
    private CoordinatorService coordinatorService;
    @Autowired
    private JpaCoordinatorRepository coordinatorRepository;

    @PostConstruct
    public void loadData() {
        if (projectRepository.count() == 0) {
            // Coordinador
            JpaCoordinatorEntity user2 = new JpaCoordinatorEntity("paulamunoz","passwordPaula","paulamunoz@example.com");
            coordinatorRepository.save(user2);

            // Proyecto 1
            JpaProjectEntity project1 = new JpaProjectEntity(
                    new ProjectId("P1"),
                    new ProjectTitle("Sistema de Gestión"),
                    "Sistema para gestionar recursos",
                    "Resumen del sistema",
                    "Objetivos claros",
                    new ProjectRegistrationDate(LocalDate.now()),
                    new ProjectDeadline(30),
                    1.2,
                    EnumProjectState.RECIBIDO,
                    "juanvela",
                    "paulamunoz"
            );

            // Proyecto 2
            JpaProjectEntity project2 = new JpaProjectEntity(
                    new ProjectId("P2"),
                    new ProjectTitle("Aplicación de Seguimiento de Salud"),
                    "Crear una app móvil que permita a los usuarios llevar el control de su salud diaria.",
                    "App para registro de actividad física, alimentación y signos vitales.",
                    "Promover hábitos saludables mediante el seguimiento diario.",
                    new ProjectRegistrationDate(LocalDate.now()),
                    new ProjectDeadline(8),
                    1.5,
                    EnumProjectState.RECIBIDO,
                    "juanvela",
                    "paulamunoz"
            );

            // Proyecto 3 (idéntico a proyecto 2 pero con otro ID)
            JpaProjectEntity project3 = new JpaProjectEntity(
                    new ProjectId("P3"),
                    new ProjectTitle("Aplicación de Seguimiento de Salud"),
                    "Crear una app móvil que permita a los usuarios llevar el control de su salud diaria.",
                    "App para registro de actividad física, alimentación y signos vitales.",
                    "Promover hábitos saludables mediante el seguimiento diario.",
                    new ProjectRegistrationDate(LocalDate.now()),
                    new ProjectDeadline(8),
                    30000.0,
                    EnumProjectState.RECIBIDO,
                    "juanvela",
                    "paulamunoz"
            );

            // Guardar proyectos
            projectRepository.save(project1);
            projectRepository.save(project2);
            projectRepository.save(project3);
        }
    }
}

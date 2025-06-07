package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
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
                    10000.0,
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
                    30000.0,
                    EnumProjectState.RECIBIDO,
                    "juanvela",
                    "paulamunoz"
            );

            // Proyecto 3
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

            // Proyecto 4 - 2020.2
            JpaProjectEntity project4 = new JpaProjectEntity(
                    new ProjectId("P4"),
                    new ProjectTitle("Plataforma de Aprendizaje en Línea"),
                    "Diseñar una plataforma web educativa interactiva.",
                    "Portal con recursos, ejercicios y seguimiento para estudiantes.",
                    "Facilitar el acceso a la educación desde casa.",
                    new ProjectRegistrationDate(LocalDate.of(2020, 9, 1)),
                    new ProjectDeadline(6),
                    15000.0,
                    EnumProjectState.ACEPTADO,
                    "juanvela",
                    "paulamunoz"
            );

            // Proyecto 5 - 2023.1
            JpaProjectEntity project5 = new JpaProjectEntity(
                    new ProjectId("P5"),
                    new ProjectTitle("Sistema de Control de Inventario"),
                    "Control eficiente de inventario para pymes.",
                    "Sistema automatizado de seguimiento de productos.",
                    "Reducir pérdidas por mal manejo de inventario.",
                    new ProjectRegistrationDate(LocalDate.of(2023, 2, 1)),
                    new ProjectDeadline(5),
                    20000.0,
                    EnumProjectState.RECHAZADO,
                    "juanvela",
                    "paulamunoz"
            );

            // Proyecto 6 - 2024.1
            JpaProjectEntity project6 = new JpaProjectEntity(
                    new ProjectId("P6"),
                    new ProjectTitle("Chatbot de Atención al Cliente"),
                    "Desarrollar un chatbot inteligente para atención 24/7.",
                    "Soporte automatizado con IA para mejorar la atención al cliente.",
                    "Reducir carga de trabajo del personal de soporte.",
                    new ProjectRegistrationDate(LocalDate.of(2024, 2, 1)),
                    new ProjectDeadline(4),
                    25000.0,
                    EnumProjectState.EJECUCION,
                    "juanvela",
                    "paulamunoz"
            );

            // Proyecto 7 - 2024.2
            JpaProjectEntity project7 = new JpaProjectEntity(
                    new ProjectId("P7"),
                    new ProjectTitle("Sistema de Reservas Online"),
                    "Permitir reservas en línea para eventos y espacios.",
                    "Interfaz amigable para que los usuarios reserven desde cualquier dispositivo.",
                    "Digitalizar el sistema de reservas.",
                    new ProjectRegistrationDate(LocalDate.of(2024, 8, 1)),
                    new ProjectDeadline(3),
                    12000.0,
                    EnumProjectState.CERRADO,
                    "juanvela",
                    "paulamunoz"
            );

            // Guardar proyectos
            projectRepository.save(project1);
            projectRepository.save(project2);
            projectRepository.save(project3);
            projectRepository.save(project4);
            projectRepository.save(project5);
            projectRepository.save(project6);
            projectRepository.save(project7);
        }
    }
}

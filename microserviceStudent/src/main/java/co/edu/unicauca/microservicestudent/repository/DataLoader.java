package co.edu.unicauca.microservicestudent.repository;

import co.edu.unicauca.microservicestudent.entity.EnumProjectState;
import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Clase que carga datos iniciales en la base de datos al iniciar la aplicación.
 *
 * Esta clase se ejecuta automáticamente al arrancar el contexto de Spring y permite
 * inicializar datos de prueba para facilitar el desarrollo o pruebas del sistema.
 */
@Component
public class DataLoader implements CommandLineRunner {

    /**
     * Repositorio para gestionar operaciones de persistencia sobre la entidad Student.
     */
    @Autowired
    private IStudentRepository studentRepository;

    /**
     * Repositorio para gestionar operaciones de persistencia sobre la entidad Project.
     */
    @Autowired
    private IProjectRepository projectRepository;

    /**
     * Método que se ejecuta automáticamente al iniciar la aplicación.
     *
     * Elimina todos los registros previos en las tablas de estudiantes y proyectos
     * y crea datos de ejemplo para pruebas.
     *
     * @param args Argumentos de línea de comandos (no utilizados en este caso)
     */
    @Override
    @Transactional
    public void run(String... args) {
        // Eliminar registros existentes para evitar duplicados en pruebas
        studentRepository.deleteAll();
        projectRepository.deleteAll();

        // Crear y guardar un estudiante de ejemplo
        Student user3 = new Student("cunas", "cunas@example.com", "passwordcris");
        studentRepository.save(user3);

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
        //project1.setIdcompany("juanvela");

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
        //project2.setIdcompany("juanvela");

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
        //project3.setIdcompany("juanvela");

        // Proyecto 4 - 2020.2
        Project project4 = new Project();
        project4.setProId("P4");
        project4.setProTitle("Plataforma de Aprendizaje en Línea");
        project4.setProDescription("Diseñar una plataforma web educativa interactiva.");
        project4.setProAbstract("Portal con recursos, ejercicios y seguimiento para estudiantes.");
        project4.setProGoals("Facilitar el acceso a la educación desde casa.");
        project4.setProDate(LocalDate.of(2020, 9, 1)); // 2020.2
        project4.setProDeadLine(6);
        project4.setProBudget(15000.0);
        project4.setProState(EnumProjectState.ACEPTADO);
        //project4.setIdcompany("juanvela");

        // Proyecto 5 - 2023.1
        Project project5 = new Project();
        project5.setProId("P5");
        project5.setProTitle("Sistema de Control de Inventario");
        project5.setProDescription("Control eficiente de inventario para pymes.");
        project5.setProAbstract("Sistema automatizado de seguimiento de productos.");
        project5.setProGoals("Reducir pérdidas por mal manejo de inventario.");
        project5.setProDate(LocalDate.of(2023, 2, 1)); // 2023.1
        project5.setProDeadLine(5);
        project5.setProBudget(20000.0);
        project5.setProState(EnumProjectState.RECHAZADO);
        //project5.setIdcompany("juanvela");

        // Proyecto 6 - 2024.1
        Project project6 = new Project();
        project6.setProId("P6");
        project6.setProTitle("Chatbot de Atención al Cliente");
        project6.setProDescription("Desarrollar un chatbot inteligente para atención 24/7.");
        project6.setProAbstract("Soporte automatizado con IA para mejorar la atención al cliente.");
        project6.setProGoals("Reducir carga de trabajo del personal de soporte.");
        project6.setProDate(LocalDate.of(2024, 2, 1)); // 2024.1
        project6.setProDeadLine(4);
        project6.setProBudget(25000.0);
        project6.setProState(EnumProjectState.EJECUCION);
        //project6.setIdcompany("juanvela");

        // Proyecto 7 - 2024.2
        Project project7 = new Project();
        project7.setProId("P7");
        project7.setProTitle("Sistema de Reservas Online");
        project7.setProDescription("Permitir reservas en línea para eventos y espacios.");
        project7.setProAbstract("Interfaz amigable para que los usuarios reserven desde cualquier dispositivo.");
        project7.setProGoals("Digitalizar el sistema de reservas.");
        project7.setProDate(LocalDate.of(2024, 8, 1)); // 2024.2
        project7.setProDeadLine(3);
        project7.setProBudget(12000.0);
        project7.setProState(EnumProjectState.CERRADO);
        //project7.setIdcompany("juanvela");

        // Guardar todos
        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);
        projectRepository.save(project4);
        projectRepository.save(project5);
        projectRepository.save(project6);
        projectRepository.save(project7);
    }
}
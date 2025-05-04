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
        Student user3 = new Student("cunas", "cunas@example.com", "passwordCris");
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
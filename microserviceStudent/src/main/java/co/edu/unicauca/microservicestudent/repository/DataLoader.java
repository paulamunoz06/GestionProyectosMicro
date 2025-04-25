package co.edu.unicauca.microservicestudent.repository;

import co.edu.unicauca.microservicestudent.entity.EnumProjectState;
import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    IStudentRepository studentRepository;

    @Autowired
    IProjectRepository projectRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        studentRepository.deleteAll();
        projectRepository.deleteAll();

        // Crear estudiante 1
        Student student1 = new Student(1L, "Juan Pérez", "juan@example.com", "Password1");
        studentRepository.save(student1);

        // Crear estudiante 2
        Student student2 = new Student(2L, "Ana Torres", "ana@example.com", "Password2");
        studentRepository.save(student2);

        // Crear estudiante 3
        Student student3 = new Student(3L, "Paula Munoz", "paula@example.com", "Password32");
        studentRepository.save(student3);

        // Proyecto 1 con estudiante postulado
        Project project1 = new Project();
        project1.setProid(1L);
        project1.setProtitle("Sistema de Gestión");
        project1.setProdescription("Sistema para gestionar recursos");
        project1.setProAbstract("Resumen del sistema");
        project1.setProGoals("Objetivos claros");
        project1.setProDeadline(30);
        project1.setProBudget(10000.0);
        project1.setProState(EnumProjectState.ACEPTADO);
        project1.setProDate(LocalDate.now());
        projectRepository.save(project1);

        // Proyecto 2 con estudiante aprobado
        Project project2 = new Project();
        project2.setProid(2L);
        project2.setProtitle("Aplicación Móvil");
        project2.setProdescription("App para gestión personal");
        project2.setProAbstract("Resumen de la app");
        project2.setProGoals("Metas específicas");
        project2.setProDeadline(45);
        project2.setProBudget(8000.0);
        project2.setProState(EnumProjectState.ACEPTADO);
        project2.setProDate(LocalDate.now());

        // Asociar estudiante2 como aprobado
        project2.getApproved().add(student2);
        projectRepository.save(project2);

        student2.getApproved().add(project2);
        studentRepository.save(student2);
    }
}

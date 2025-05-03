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
    }
}
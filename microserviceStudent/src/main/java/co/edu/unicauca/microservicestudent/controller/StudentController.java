package co.edu.unicauca.microservicestudent.controller;

import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import co.edu.unicauca.microservicestudent.infra.dto.ProjectDto;
import co.edu.unicauca.microservicestudent.service.ProjectService;
import co.edu.unicauca.microservicestudent.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar operaciones relacionadas con estudiantes y sus proyectos.
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProjectService projectService;

    /**
     * Obtiene estadísticas de proyectos relacionados con un estudiante:
     * total de proyectos, postulaciones realizadas y proyectos aprobados.
     *
     * @param idStudent ID del estudiante.
     * @return Lista de tres valores enteros representando las estadísticas.
     */
    @GetMapping("/{idStudent}/projects")
    public ResponseEntity<?> getDataProjects(@PathVariable String idStudent) {
        try {
            List<Integer> datos = new ArrayList<>();
            datos.add(projectService.getAllProjects());
            datos.add(projectService.getPostulatedProjects(idStudent));
            datos.add(projectService.getApprovedProjects(idStudent));
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene la lista de proyectos disponibles para que un estudiante pueda postularse.
     *
     * @param idStudent ID del estudiante.
     * @return Lista de proyectos disponibles.
     */
    @GetMapping("/{idStudent}/projectsAvailable")
    public ResponseEntity<?> getProjectsAvailable(@PathVariable String idStudent) {
        try {
            List<ProjectDto> projects = projectService.getAvailableProjectsForStudent(idStudent);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Permite que un estudiante se postule a un proyecto específico.
     *
     * @param idStudent ID del estudiante.
     * @param idProject ID del proyecto.
     * @return Información del estudiante actualizada tras la postulación.
     */
    @PostMapping("/{idStudent}/project/{idProject}")
    public ResponseEntity<?> studentPostulation(@PathVariable String idStudent, @PathVariable String idProject) {
        try {
            Optional<Student> optionalStudent = studentService.findById(idStudent);
            if (optionalStudent.isEmpty()) {
                throw new Exception("Estudiante con ID " + idStudent + " no encontrado");
            }

            Optional<Project> optionalProject = projectService.findById(idProject);
            if (optionalProject.isEmpty()) {
                throw new Exception("Proyecto con ID " + idProject + " no encontrado");
            }

            Student updateStudent = studentService.studentPostulation(optionalStudent.get(), optionalProject.get());
            return ResponseEntity.ok(studentService.studentToDto(updateStudent));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene un estudiante por su ID.
     *
     * @param idStudent ID del estudiante.
     * @return El estudiante correspondiente o un error si no existe.
     */
    @GetMapping("/{idStudent}")
    public ResponseEntity<?> getStudentById(@PathVariable String idStudent) {
        try {
            Optional<Student> student = studentService.findById(idStudent);
            if (student.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(studentService.studentToDto(student.get()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtiene un proyecto por su ID.
     *
     * @param idProject ID del proyecto.
     * @return El proyecto correspondiente o un error si no existe.
     */
    @GetMapping("/project/{idProject}")
    public ResponseEntity<?> getProjectById(@PathVariable String idProject) {
        try {
            Optional<Project> project = projectService.findById(idProject);
            if (project.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(projectService.projectToDto(project.get()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
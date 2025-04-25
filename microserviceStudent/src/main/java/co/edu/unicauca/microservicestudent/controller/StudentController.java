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

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{idStudent}/projects")
    public ResponseEntity<?> getDataProjects(@PathVariable Long idStudent) {
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

    @GetMapping("/{idStudent}/projectsAvailable")
    public ResponseEntity<?> getProjectsAvailable(@PathVariable Long idStudent) {
        try {
            List<ProjectDto> projects = projectService.getAvailableProjectsForStudent(idStudent);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{idStudent}/project/{idProject}")
    public ResponseEntity<?> studentPostulation(@PathVariable Long idStudent, @PathVariable Long idProject) {
        try {
            Optional<Student> optionalStudent = studentService.findById(idStudent);
            if (optionalStudent.isEmpty()) {
                throw new Exception("Estudiante con ID " + idStudent + " no encontrado");
            }

            Optional<Project> optionalProject = projectService.findById(idProject);
            if (optionalProject.isEmpty()) {
                throw new Exception("Proyecto con ID " + idProject + " no encontrado");
            }

            Student updateStudent = studentService.studentPostulation(optionalStudent.get(),optionalProject.get());
            return ResponseEntity.ok(studentService.studentToDto(updateStudent));
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{idStudent}")
    public ResponseEntity<?> getStudentById(@PathVariable Long idStudent) {
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
}


package co.edu.unicauca.microservicecoordinator.controller;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    @GetMapping("/projects/count-by-status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable String status) {
        Long count = projectService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    // Endpoint para contar el total de proyectos
    @GetMapping("/projects/count-total")
    public ResponseEntity<Integer> countTotalProjects() {
        int totalProjects = projectService.countTotalProjects(); // Llama al servicio para contar proyectos
        return ResponseEntity.ok(totalProjects); // Devuelve el número total de proyectos
    }



}

package co.edu.unicauca.microservicecoordinator.presentation;

import co.edu.unicauca.microservicecoordinator.application.port.in.CoordinatorServicePort;
import co.edu.unicauca.microservicecoordinator.application.port.in.ProjectServicePort;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.adapter.in.rest.CoordinatorService;
import co.edu.unicauca.microservicecoordinator.adapter.in.rest.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {

    @Autowired
    private final ProjectServicePort projectService;

    @Autowired
    private CoordinatorServicePort coordinatorService;

    public CoordinatorController(ProjectServicePort service, CoordinatorServicePort coordinatorService) {
        this.projectService = service;
        this.coordinatorService = coordinatorService;
    }

    @GetMapping("/projects")
    public List<ProjectDto> getAllProjects() {
        return projectService.findAllProjects();
    }

    @GetMapping("/projects/count-by-status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable String status) {
        Long count = projectService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/projects/count-total")
    public ResponseEntity<Integer> countTotalProjects() {
        int totalProjects = projectService.countTotalProjects();
        return ResponseEntity.ok(totalProjects);
    }

    @PutMapping("/projects/update-status")
    public ResponseEntity<ProjectDto> updateProjectStatus(@RequestBody ProjectDto projectDto) {
        ProjectDto updatedProject = coordinatorService.evaluateProject(projectDto.getProId(), projectDto.getProState());
        if (updatedProject != null) {
            return ResponseEntity.ok(updatedProject);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/project/{idProject}")
    public ResponseEntity<?> getProjectById(@PathVariable String idProject) {
        try {
            Optional<ProjectDto> project = projectService.findById(idProject);
            if (project.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(project.get());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
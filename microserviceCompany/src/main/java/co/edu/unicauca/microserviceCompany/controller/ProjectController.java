package co.edu.unicauca.microserviceCompany.controller;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import co.edu.unicauca.microserviceCompany.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @Autowired
    private ICompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<?> registerProject(@RequestBody ProjectDto projectDto, @RequestParam String companyId) {
        try {
            // Verificar si la empresa existe
            Optional<Company> companyOpt = companyService.findById(companyId);
            if (companyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Empresa no encontrada con ID: " + companyId));
            }

            // Asignar el companyId al DTO del proyecto
            projectDto.setCompanyId(companyId.toString());

            // Crear y guardar el proyecto
            Project savedProject = projectService.createProject(projectDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(projectService.projectToDto(savedProject));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar el proyecto: " + e.getMessage()));
        }
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {
        try {
            Optional<Project> project = projectService.findById(projectId);
            if (project.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(projectService.projectToDto(project.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/exists/{projectId}")
    public ResponseEntity<?> existsProject(@PathVariable String projectId) {
        try {
            Optional<Project> project = projectService.findById(projectId);
            return ResponseEntity.ok(Map.of("exists", project.isPresent()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error checking if project exists: " + e.getMessage()));
        }
    }

    @GetMapping("/{projectId}/company")
    public ResponseEntity<?> getCompanyByProjectId(@PathVariable String projectId) {
        try {
            return ResponseEntity.ok(projectService.getCompanyInfo(projectId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
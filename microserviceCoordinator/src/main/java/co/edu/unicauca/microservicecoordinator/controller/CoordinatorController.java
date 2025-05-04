package co.edu.unicauca.microservicecoordinator.controller;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.infra.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.service.CoordinatorService;
import co.edu.unicauca.microservicecoordinator.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST que gestiona las operaciones relacionadas con la coordinación de proyectos.
 * Proporciona endpoints para la consulta y actualización de proyectos.
 *
 */
@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {

    /**
     * Servicio que maneja las operaciones relacionadas con los proyectos.
     */
    @Autowired
    private ProjectService projectService;

    /**
     * Servicio que maneja las operaciones del coordinador.
     */
    @Autowired
    private CoordinatorService coordinatorService;

    /**
     * Obtiene todos los proyectos registrados en el sistema.
     *
     * @return Lista de todos los proyectos
     */
    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    /**
     * Cuenta el número de proyectos que tienen un estado específico.
     *
     * @param status El estado por el cual filtrar los proyectos
     * @return ResponseEntity con el número de proyectos que coinciden con el estado especificado
     */
    @GetMapping("/projects/count-by-status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable String status) {
        Long count = projectService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    /**
     * Cuenta el número total de proyectos registrados en el sistema.
     *
     * @return ResponseEntity con el número total de proyectos
     */
    @GetMapping("/projects/count-total")
    public ResponseEntity<Integer> countTotalProjects() {
        int totalProjects = projectService.countTotalProjects();
        return ResponseEntity.ok(totalProjects);
    }

    /**
     * Actualiza el estado de un proyecto existente.
     *
     * @param projectDto DTO que contiene el ID del proyecto y el nuevo estado
     * @return ResponseEntity con el proyecto actualizado si la operación es exitosa,
     *         o una respuesta de error 400 (Bad Request) si el proyecto no existe o el estado es inválido
     */
    @PutMapping("/projects/update-status")
    public ResponseEntity<Project> updateProjectStatus(@RequestBody ProjectDto projectDto) {
        Project updatedProject = coordinatorService.evaluateProject(projectDto.getProId(), projectDto.getProState());
        if (updatedProject != null) {
            return ResponseEntity.ok(updatedProject);
        } else {
            return ResponseEntity.badRequest().build();
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
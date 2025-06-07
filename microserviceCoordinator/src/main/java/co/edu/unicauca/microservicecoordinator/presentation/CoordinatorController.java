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

/**
 * Controlador REST para la gestión de proyectos y coordinadores.
 *
 * Proporciona endpoints para obtener proyectos, contar proyectos por estado,
 * actualizar estados de proyectos y consultar proyectos por su identificador.
 */
@RestController
@RequestMapping("/")
public class CoordinatorController {

    @Autowired
    private final ProjectServicePort projectService;

    @Autowired
    private CoordinatorServicePort coordinatorService;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param service Servicio de proyectos.
     * @param coordinatorService Servicio de coordinadores.
     */
    public CoordinatorController(ProjectServicePort service, CoordinatorServicePort coordinatorService) {
        this.projectService = service;
        this.coordinatorService = coordinatorService;
    }

    /**
     * Obtiene la lista de todos los proyectos existentes.
     *
     * @return Lista de {@link ProjectDto} con todos los proyectos.
     */
    @GetMapping("/projects")
    public List<ProjectDto> getAllProjects() {
        return projectService.findAllProjects();
    }

    /**
     * Cuenta la cantidad de proyectos que se encuentran en un estado específico.
     *
     * @param status Estado del proyecto a filtrar (como String).
     * @return Número total de proyectos en el estado indicado.
     */
    @GetMapping("/projects/count-by-status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable String status) {
        Long count = projectService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    /**
     * Obtiene el conteo total de proyectos existentes.
     *
     * @return Número total de proyectos.
     */
    @GetMapping("/projects/count-total")
    public ResponseEntity<Integer> countTotalProjects() {
        int totalProjects = projectService.countTotalProjects();
        return ResponseEntity.ok(totalProjects);
    }

    /**
     * Actualiza el estado de un proyecto a partir del DTO recibido.
     *
     * @param projectDto DTO con el identificador y nuevo estado del proyecto.
     * @return DTO del proyecto actualizado si la operación fue exitosa, o una respuesta 400 en caso contrario.
     */
    @PutMapping("/projects/update-status")
    public ResponseEntity<ProjectDto> updateProjectStatus(@RequestBody ProjectDto projectDto) {
        ProjectDto updatedProject = coordinatorService.evaluateProject(projectDto.getProId(), projectDto.getProState());
        if (updatedProject != null) {
            return ResponseEntity.ok(updatedProject);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene un proyecto por su identificador único.
     *
     * @param idProject Identificador del proyecto.
     * @return Proyecto encontrado en un ResponseEntity, o 404 si no existe, o 400 con mensaje de error si ocurre una excepción.
     */
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
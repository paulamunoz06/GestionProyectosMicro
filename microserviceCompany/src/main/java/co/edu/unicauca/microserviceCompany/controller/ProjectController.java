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

/**
 * Controlador REST para gestionar las operaciones relacionadas con los proyectos en el sistema.
 *
 * Esta clase expone endpoints para registrar proyectos, consultar proyectos por su ID,
 * verificar la existencia de un proyecto, y obtener información de la empresa asociada a un proyecto.
 *
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    /**
     * Servicio que maneja la lógica de negocio relacionada con los proyectos.
     * Este servicio es responsable de registrar proyectos, consultar por ID y obtener detalles del proyecto.
     */
    @Autowired
    private IProjectService projectService;

    /**
     * Servicio que maneja la lógica de negocio relacionada con las empresas.
     * Este servicio se utiliza para verificar si la empresa asociada a un proyecto existe.
     */
    @Autowired
    private ICompanyService companyService;

    /**
     * Endpoint para registrar un nuevo proyecto y asociarlo a una empresa.
     *
     * Este método recibe un objeto {@link ProjectDto} con los datos del proyecto y un parámetro
     * {@code companyId} para asociar el proyecto a una empresa existente. Si la empresa no se encuentra,
     * se devuelve un error HTTP 404. Si el proyecto se crea exitosamente, se devuelve el proyecto
     * registrado con código HTTP 201.
     *
     * @param projectDto Objeto que contiene los datos del proyecto a registrar.
     * @param companyId ID de la empresa a asociar al proyecto.
     * @return ResponseEntity con el estado de la operación y los datos del proyecto registrado.
     */
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

    /**
     * Endpoint para obtener los detalles de un proyecto por su ID.
     *
     * Este método recibe el ID de un proyecto como parámetro en la URL y devuelve los detalles
     * del proyecto si se encuentra. Si no se encuentra el proyecto, se retorna una respuesta
     * con código HTTP 404 (Not Found).
     *
     * @param projectId ID del proyecto a consultar.
     * @return ResponseEntity con los detalles del proyecto o una respuesta 404 si no se encuentra.
     */
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

    /**
     * Endpoint para verificar si un proyecto existe.
     *
     * Este método recibe el ID de un proyecto como parámetro en la URL y verifica si el proyecto
     * existe en el sistema. Devuelve un valor booleano en formato JSON indicando si el proyecto
     * existe o no.
     *
     * @param projectId ID del proyecto a verificar.
     * @return ResponseEntity con un mapa indicando si el proyecto existe.
     */
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
                    .body(Map.of("error", "Error al verificar la existencia del proyecto: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener la información de la empresa asociada a un proyecto.
     *
     * Este método recibe el ID de un proyecto y devuelve los detalles de la empresa que está asociada
     * a dicho proyecto. Si ocurre un error, se devuelve un mensaje de error con el código HTTP adecuado.
     *
     * @param projectId ID del proyecto cuyo empresa asociada se desea consultar.
     * @return ResponseEntity con la información de la empresa asociada al proyecto.
     */
    @GetMapping("/{projectId}/company")
    public ResponseEntity<?> getCompanyByProjectId(@PathVariable String projectId) {
        try {
            return ResponseEntity.ok(projectService.getCompanyInfo(projectId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para actualizar un proyecto existente.
     *
     * Este método recibe un objeto {@link ProjectDto} con los datos actualizados del proyecto
     * y el ID del proyecto a actualizar. Si el proyecto no se encuentra, se devuelve un error
     * HTTP 404. Si el proyecto se actualiza exitosamente, se devuelve el proyecto actualizado.
     *
     * @param projectId ID del proyecto a actualizar.
     * @param projectDto Objeto que contiene los datos actualizados del proyecto.
     * @return ResponseEntity con el estado de la operación y los datos del proyecto actualizado.
     */
    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable String projectId, @RequestBody ProjectDto projectDto) {
        try {
            // Verificar si el proyecto existe
            Optional<Project> projectOpt = projectService.findById(projectId);
            if (projectOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Proyecto no encontrado con ID: " + projectId));
            }

            // Asegurarse de que el ID en el path coincida con el ID en el DTO
            projectDto.setProId(projectId);

            // Actualizar el proyecto
            Project updatedProject = projectService.updateProject(projectDto);

            return ResponseEntity.ok(projectService.projectToDto(updatedProject));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el proyecto: " + e.getMessage()));
        }
    }
}
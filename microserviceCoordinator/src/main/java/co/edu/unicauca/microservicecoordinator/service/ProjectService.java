package co.edu.unicauca.microservicecoordinator.service;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.infra.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que implementa la lógica de negocio relacionada con los proyectos.
 *
 * <p>Esta clase proporciona métodos para gestionar proyectos, incluyendo búsqueda,
 * conversión entre entidades y DTOs, y operaciones de conteo.</p>
 */
@Service
public class ProjectService implements IProjectService {

    /**
     * Repositorio para acceder a las operaciones de persistencia de proyectos.
     */
    @Autowired
    IProjectRepository projectRepository;

    /**
     * Busca un proyecto por su identificador.
     *
     * @param id Identificador del proyecto a buscar
     * @return Un Optional que contiene el proyecto si existe, o un Optional vacío si no se encuentra
     */
    @Override
    public Optional<Project> findById(Long id) {
        return Optional.empty();
    }

    /**
     * Recupera todos los proyectos almacenados en el sistema.
     *
     * @return Lista de todos los proyectos
     */
    @Override
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Convierte una entidad Project a un objeto DTO (Data Transfer Object).
     *
     * <p>Esta conversión permite transferir datos de proyectos entre capas de la aplicación
     * o hacia sistemas externos de manera segura.</p>
     *
     * @param project Entidad Project a convertir
     * @return ProjectDto con los datos del proyecto
     */
    @Override
    public ProjectDto projectToDto(Project project) {
        ProjectDto projectDto = new ProjectDto();

        projectDto.setProId(project.getProId());
        projectDto.setProTitle(project.getProTitle());
        projectDto.setProDescription(project.getProDescription());
        projectDto.setProAbstract(project.getProAbstract());
        projectDto.setProGoals(project.getProGoals());
        projectDto.setProDate(project.getProDate());
        projectDto.setProDeadLine(project.getProDeadLine());
        projectDto.setProBudget(project.getProBudget());
        projectDto.setProState(project.getProState().toString());
        projectDto.setIdcompany(project.getIdcompany());
        projectDto.setProCoordinator(project.getProCoordinator());

        return projectDto;
    }

    /**
     * Convierte un objeto DTO (Data Transfer Object) a una entidad Project.
     *
     * <p>Esta conversión permite procesar datos recibidos de la capa de presentación
     * o sistemas externos para su posterior almacenamiento.</p>
     *
     * @param projectDto DTO con los datos del proyecto
     * @return Entidad Project con los datos convertidos
     */
    @Override
    public Project projectToClass(ProjectDto projectDto) {
        Project projectClass = new Project();

        projectClass.setProId(projectDto.getProId());
        projectClass.setProTitle(projectDto.getProTitle());
        projectClass.setProDescription(projectDto.getProDescription());
        projectClass.setProAbstract(projectDto.getProAbstract());
        projectClass.setProGoals(projectDto.getProGoals());
        projectClass.setProDate(projectDto.getProDate());
        projectClass.setProDeadLine(projectDto.getProDeadLine());
        projectClass.setProBudget(projectDto.getProBudget());
        projectClass.setProState(EnumProjectState.valueOf(projectDto.getProState()));
        projectClass.setIdcompany(projectDto.getIdcompany());
        projectClass.setProCoordinator(projectDto.getProCoordinator());

        return projectClass;
    }

    /**
     * Cuenta el número de proyectos que tienen un estado específico.
     *
     * @param status Estado de los proyectos a contar (como String)
     * @return Número de proyectos con el estado especificado
     * @throws IllegalArgumentException Si el estado proporcionado no existe en EnumProjectState
     */
    @Override
    public Long countByStatus(String status) {
        // Convierte el String a EnumProjectState
        EnumProjectState enumStatus = EnumProjectState.valueOf(status.toUpperCase());
        return projectRepository.countByProState(enumStatus);
    }

    /**
     * Cuenta el número total de proyectos en el sistema.
     *
     * @return Número total de proyectos
     */
    @Override
    public int countTotalProjects() {
        return (int) projectRepository.count();
    }
}
package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.infra.config.RabbitMQConfig;
import co.edu.unicauca.microservicestudent.infra.dto.ProjectDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los servicios relacionados con la gestión de proyectos.
 *
 * Proporciona métodos para interactuar con los proyectos en el sistema,
 * incluyendo la actualización de su estado, la consulta de proyectos disponibles para un estudiante,
 * y la conversión entre objetos de tipo {@link Project} y {@link ProjectDto}.
 */
public interface IProjectService {

    /**
     * Actualiza el estado de un proyecto recibido a través de un mensaje de RabbitMQ.
     *
     * @param projectDto El objeto {@link ProjectDto} que contiene la información actualizada del proyecto.
     * @throws Exception Si ocurre algún error durante el procesamiento del proyecto.
     */
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.UPDATEPROJECT_QUEUE)
    void updateProject(ProjectDto projectDto) throws Exception;

    /**
     * Busca un proyecto en el sistema por su ID.
     *
     * @param id El ID del proyecto que se desea buscar.
     * @return Un {@link Optional} que contiene el proyecto si se encuentra, o está vacío si no existe.
     */
    @Transactional
    Optional<Project> findById(String id);

    /**
     * Obtiene todos los proyectos disponibles para un estudiante.
     *
     * @param studentId El ID del estudiante para el cual se buscan los proyectos disponibles.
     * @return Una lista de objetos {@link ProjectDto} que representan los proyectos disponibles para el estudiante.
     * @throws Exception Si ocurre un error durante la consulta de los proyectos.
     */
    List<ProjectDto> getAvailableProjectsForStudent(String studentId) throws Exception;

    /**
     * Obtiene el número total de proyectos existentes en el sistema.
     *
     * @return El número total de proyectos en el sistema.
     */
    int getAllProjects();

    /**
     * Obtiene el número de proyectos en los que un estudiante ha postulado.
     *
     * @param studentId El ID del estudiante cuyo número de proyectos postulados se desea obtener.
     * @return El número de proyectos en los que el estudiante ha postulado.
     */
    int getPostulatedProjects(String studentId);

    /**
     * Obtiene el número de proyectos que un estudiante ha aprobado.
     *
     * @param studentId El ID del estudiante cuyo número de proyectos aprobados se desea obtener.
     * @return El número de proyectos que el estudiante ha aprobado.
     */
    int getApprovedProjects(String studentId);

    /**
     * Convierte un objeto {@link Project} en un objeto {@link ProjectDto}.
     *
     * @param project El proyecto que se va a convertir.
     * @return El objeto {@link ProjectDto} que contiene los mismos datos que el proyecto original.
     */
    ProjectDto projectToDto(Project project);

    /**
     * Convierte un objeto {@link ProjectDto} en un objeto {@link Project}.
     *
     * @param projectDto El DTO de proyecto que se va a convertir.
     * @return El objeto {@link Project} correspondiente a los datos del DTO.
     */
    Project projectToClass(ProjectDto projectDto);
}
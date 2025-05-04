package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Interfaz que define los métodos de servicio para gestionar las operaciones relacionadas con los proyectos.
 * Esta interfaz proporciona la definición de métodos para crear proyectos, consultar proyectos por su ID,
 * obtener información de la empresa asociada a un proyecto, y convertir entre entidades y DTOs.
 */
public interface IProjectService {

    /**
     * Crea un nuevo proyecto.
     *
     * @param projectDto DTO con la información del proyecto a crear.
     * @return El proyecto creado.
     */
    @Transactional
    Project createProject(ProjectDto projectDto);

    /**
     * Actualiza un proyecto existente con la información proporcionada en el DTO.
     *
     * @param projectDto DTO con los datos actualizados del proyecto.
     * @return El proyecto actualizado.
     * @throws IllegalArgumentException Si hay datos inválidos en el DTO.
     * @throws EntityNotFoundException Si el proyecto a actualizar no existe.
     */
    Project updateProject(ProjectDto projectDto) throws IllegalArgumentException, EntityNotFoundException;

    /**
     * Busca un proyecto por su ID.
     *
     * @param id Identificador único del proyecto.
     * @return Un objeto Optional con el proyecto encontrado.
     */
    @Transactional
    Optional<Project> findById(String id);

    /**
     * Obtiene la información de la empresa asociada a un proyecto.
     *
     * @param projectId Identificador del proyecto.
     * @return Un DTO con la información de la empresa asociada.
     * @throws Exception Si ocurre un error al obtener la información de la empresa.
     */
    CompanyDto getCompanyInfo(String projectId) throws Exception;

    /**
     * Convierte una entidad Project en su DTO correspondiente.
     *
     * @param project Entidad Project a convertir.
     * @return El DTO correspondiente al proyecto.
     */
    ProjectDto projectToDto(Project project);

    /**
     * Convierte un DTO de proyecto en su entidad correspondiente.
     *
     * @param projectDto DTO con los datos del proyecto.
     * @return La entidad Project correspondiente.
     */
    Project projectToClass(ProjectDto projectDto);
}
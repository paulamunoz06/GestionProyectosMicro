package co.edu.unicauca.microservicecoordinator.service;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.infra.dto.ProjectDto;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProjectService {
    //Obtener proyecto por Id
    @Transactional
    Optional<Project> findById(String id);

    //Obtener todos los proyectos
    @Transactional
    List<Project> findAllProjects();

    ProjectDto projectToDto(Project project);

    Project projectToClass(ProjectDto projectDto);

    Long countByStatus(String status);

    int countTotalProjects();


}

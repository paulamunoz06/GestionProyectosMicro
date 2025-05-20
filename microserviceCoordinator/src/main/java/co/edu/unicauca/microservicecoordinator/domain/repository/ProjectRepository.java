package co.edu.unicauca.microservicecoordinator.domain.repository;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository.JpaProjectRepository;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);

    Optional<ProjectDto> findById(String id);

    List<ProjectDto> findAll();

    Long countByProState(EnumProjectState enumProjectState);

    int count();
}

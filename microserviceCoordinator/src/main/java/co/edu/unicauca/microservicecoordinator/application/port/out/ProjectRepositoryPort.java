package co.edu.unicauca.microservicecoordinator.application.port.out;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryPort {
    Project save(Project project);

    Optional<ProjectDto> findById(String id);

    List<ProjectDto> findAll();

    Long countByProState(EnumProjectState enumProjectState);

    int count();
}

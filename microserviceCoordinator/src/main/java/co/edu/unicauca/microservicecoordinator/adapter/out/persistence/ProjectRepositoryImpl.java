package co.edu.unicauca.microservicecoordinator.adapter.out.persistence;

import co.edu.unicauca.microservicecoordinator.application.port.out.ProjectRepositoryPort;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectId;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaProjectEntity;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository.JpaProjectRepository;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository.ProjectJpaMapper;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDtoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class ProjectRepositoryImpl implements ProjectRepositoryPort {

    private final JpaProjectRepository jpaRepository;

    public ProjectRepositoryImpl(JpaProjectRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Project save(Project project)  {
        JpaProjectEntity jpaEntity = ProjectJpaMapper.toJpaEntity(project);
        return ProjectJpaMapper.toDomainEntity(jpaRepository.save(jpaEntity));
    }

    @Override
    public Optional<ProjectDto> findById(String id) {
        return jpaRepository.findById(new ProjectId(id)).map(ProjectJpaMapper::toDomainEntity).map(ProjectDtoMapper::projectToDto);
    }

    @Override
    public List<ProjectDto> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(ProjectJpaMapper::toDomainEntity)
                .map(ProjectDtoMapper::projectToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByProState(EnumProjectState enumProjectState) {
        return jpaRepository.countByProState(enumProjectState);
    }

    @Override
    public int count() {
        return (int) jpaRepository.count();
    }

}

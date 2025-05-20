package co.edu.unicauca.microservicecoordinator.adapter.in.rest;

import co.edu.unicauca.microservicecoordinator.application.port.in.ProjectServicePort;
import co.edu.unicauca.microservicecoordinator.application.port.out.ProjectRepositoryPort;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.infraestructure.config.RabbitMQConfig;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDtoMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements ProjectServicePort {
    private final ProjectRepositoryPort projectRepository;

    public ProjectService(ProjectRepositoryPort projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<ProjectDto> findById(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<ProjectDto> findAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Long countByStatus(String status) {
        EnumProjectState enumStatus = EnumProjectState.valueOf(status.toUpperCase());
        return projectRepository.countByProState(enumStatus);
    }

    @Override
    public int countTotalProjects() {
        return projectRepository.count();
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.CREATEPROJECT_QUEUE)
    public void receiveProject(ProjectDto project) {
        System.out.println("Proyecto recibido: " + project.getProTitle());
        Project project1 = ProjectDtoMapper.projectToClass(project);
        projectRepository.save(project1);
    }

}
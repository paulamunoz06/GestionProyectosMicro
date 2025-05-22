package co.edu.unicauca.microservicecoordinator.application.port.in;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.infraestructure.config.RabbitMQConfig;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDtoMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;
import java.util.Optional;

public interface ProjectServicePort {
    Optional<ProjectDto> findById(String id);

    List<ProjectDto> findAllProjects();

    Long countByStatus(String status);

    int countTotalProjects();

    void receiveProject(ProjectDto project);

}

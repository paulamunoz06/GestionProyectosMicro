package co.edu.unicauca.microservicecoordinator.application;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.repository.ProjectRepository;
import co.edu.unicauca.microservicecoordinator.infraestructure.config.RabbitMQConfig;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDtoMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CoordinatorService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ProjectRepository projectRepository;

    public CoordinatorService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDto evaluateProject(String proId, String proStatusStr) {
        Optional<Project> optionalProject = projectRepository.findById(proId).map(ProjectDtoMapper::projectToClass);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.syncState();
            EnumProjectState newState;

            try {
                newState = EnumProjectState.valueOf(proStatusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidStateTransitionException("Estado inválido: " + proStatusStr);
            }

            // Ejecutar transición de estado usando el patrón State
            switch (newState) {
                case ACEPTADO -> project.getCurrentState().accept(project);
                case RECHAZADO -> project.getCurrentState().reject(project);
                case EJECUCION -> project.getCurrentState().execute(project);
                case CERRADO -> project.getCurrentState().close(project);
                case RECIBIDO -> project.getCurrentState().receive(project);
            }

            projectRepository.save(project);

            rabbitTemplate.convertAndSend(RabbitMQConfig.UPDATEPROJECT_QUEUE, ProjectDtoMapper.projectToDto(project));
            return ProjectDtoMapper.projectToDto(project);
        } else {
            throw new InvalidStateTransitionException("Proyecto no encontrado con ID: " + proId);
        }
    }
}
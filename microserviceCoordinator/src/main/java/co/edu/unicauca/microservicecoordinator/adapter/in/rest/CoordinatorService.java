package co.edu.unicauca.microservicecoordinator.adapter.in.rest;

import co.edu.unicauca.microservicecoordinator.application.port.in.CoordinatorServicePort;
import co.edu.unicauca.microservicecoordinator.application.port.out.ProjectRepositoryPort;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.infraestructure.config.RabbitMQConfig;
import co.edu.unicauca.microservicecoordinator.domain.model.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDtoMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Servicio encargado de coordinar la evaluación de proyectos,
 * aplicando transiciones de estado y notificando a otros componentes
 * mediante colas de mensajería (RabbitMQ).
 */
@Service
public class CoordinatorService implements CoordinatorServicePort {

    /**
     * Plantilla de RabbitMQ utilizada para enviar mensajes a las colas configuradas.
     * Se utiliza para notificar cambios en los proyectos evaluados.
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Repositorio que proporciona operaciones de acceso y persistencia
     * para los proyectos.
     */
    private final ProjectRepositoryPort projectRepository;

    /**
     * Constructor que inyecta la dependencia del repositorio de proyectos.
     *
     * @param projectRepository Repositorio de acceso a proyectos.
     */
    public CoordinatorService(ProjectRepositoryPort projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Evalúa el estado de un proyecto identificado por su ID.
     * Aplica una transición de estado basada en el nuevo estado recibido como cadena.
     * También persiste el cambio y lo comunica a través de una cola de RabbitMQ.
     *
     * @param proId        ID del proyecto a evaluar.
     * @param proStatusStr Estado nuevo en formato texto (debe coincidir con {@link EnumProjectState}).
     * @return DTO del proyecto actualizado.
     * @throws InvalidStateTransitionException Si el proyecto no se encuentra o el estado es inválido.
     */
    @Override
    public ProjectDto evaluateProject(String proId, String proStatusStr) {
        Optional<Project> optionalProject = projectRepository.findById(proId).map(ProjectDtoMapper::projectToClass);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.syncState(); // Asegura que el estado esté sincronizado con su lógica interna.

            EnumProjectState newState;
            try {
                newState = EnumProjectState.valueOf(proStatusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidStateTransitionException("Estado inválido: " + proStatusStr);
            }

            // Aplica la transición de estado correspondiente usando el patrón State.
            switch (newState) {
                case ACEPTADO -> project.getCurrentState().accept(project);
                case RECHAZADO -> project.getCurrentState().reject(project);
                case EJECUCION -> project.getCurrentState().execute(project);
                case CERRADO -> project.getCurrentState().close(project);
                case RECIBIDO -> project.getCurrentState().receive(project);
            }

            // Persiste los cambios y envía actualización por RabbitMQ
            projectRepository.save(project);
            rabbitTemplate.convertAndSend(RabbitMQConfig.UPDATEPROJECT_QUEUE, ProjectDtoMapper.projectToDto(project));

            return ProjectDtoMapper.projectToDto(project);
        } else {
            throw new InvalidStateTransitionException("Proyecto no encontrado con ID: " + proId);
        }
    }
}
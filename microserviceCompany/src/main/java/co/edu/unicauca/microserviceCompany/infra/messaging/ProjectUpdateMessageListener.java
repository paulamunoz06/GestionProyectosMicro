package co.edu.unicauca.microserviceCompany.infra.messaging;

import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.config.RabbitMQConfig;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Listener para los mensajes de actualización de proyectos recibidos a través de RabbitMQ.
 * Este componente escucha la cola de actualización de proyectos y procesa los cambios
 * para mantener sincronizados los proyectos entre los microservicios.
 */
@Component
public class ProjectUpdateMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ProjectUpdateMessageListener.class);

    @Autowired
    private IProjectRepository projectRepository;

    /**
     * Maneja los mensajes de actualización de proyectos recibidos de la cola RabbitMQ.
     * Actualiza el estado y los datos del proyecto en la base de datos local.
     *
     * @param projectDto DTO con los datos actualizados del proyecto
     */
    @RabbitListener(queues = RabbitMQConfig.UPDATEPROJECT_QUEUE)
    public void handleProjectUpdate(ProjectDto projectDto) {
        logger.info("Recibido mensaje de actualización para proyecto con ID: {}", projectDto.getProId());

        try {
            // Buscar el proyecto en la base de datos
            Optional<Project> projectOpt = projectRepository.findById(projectDto.getProId());

            if (projectOpt.isPresent()) {
                // Actualizar el proyecto existente
                Project project = projectOpt.get();
                updateProjectFromDto(project, projectDto);
                projectRepository.save(project);
                logger.info("Proyecto actualizado correctamente: {}", projectDto.getProId());
            } else {
                logger.warn("Proyecto no encontrado con ID: {}. No se puede actualizar.", projectDto.getProId());
            }
        } catch (Exception e) {
            logger.error("Error al procesar la actualización del proyecto: {}", e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un proyecto existente con la información recibida en el DTO.
     *
     * @param project Proyecto a actualizar
     * @param dto DTO con los datos actualizados
     */
    private void updateProjectFromDto(Project project, ProjectDto dto) {
        // Actualizar solo campos no nulos del DTO
        if (dto.getProTitle() != null) {
            project.setProTitle(dto.getProTitle());
        }

        if (dto.getProDescription() != null) {
            project.setProDescription(dto.getProDescription());
        }

        if (dto.getProAbstract() != null) {
            project.setProAbstract(dto.getProAbstract());
        }

        if (dto.getProGoals() != null) {
            project.setProGoals(dto.getProGoals());
        }

        if (dto.getProDeadLine() != 0) {
            project.setProDeadLine(dto.getProDeadLine());
        }

        if (dto.getProBudget() != null) {
            project.setProBudget(dto.getProBudget());
        }

        // Actualizar el estado si viene en el DTO
        if (dto.getProState() != null && !dto.getProState().isEmpty()) {
            try {
                project.setProState(EnumProjectState.valueOf(dto.getProState()));
            } catch (IllegalArgumentException e) {
                logger.warn("Estado inválido recibido: {}. Se mantiene el estado actual.", dto.getProState());
            }
        }
    }
}
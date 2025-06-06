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

/**
 * Servicio de aplicación que actúa como orquestador de las operaciones relacionadas con proyectos.
 *
 * Este servicio implementa el puerto de entrada ProjectServicePort, definido en el dominio,
 * y utiliza el puerto de salida ProjectRepositoryPort para interactuar con la capa de persistencia.
 */
@Service
public class ProjectService implements ProjectServicePort {

    /**
     * Puerto de salida que permite el acceso a la infraestructura de persistencia
     * sin acoplar esta clase a una implementación concreta.
     */
    private final ProjectRepositoryPort projectRepository;

    /**
     * Constructor que permite la inyección del repositorio de proyectos.
     *
     * @param projectRepository Puerto de salida para operaciones sobre la entidad Project.
     */
    public ProjectService(ProjectRepositoryPort projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Busca un proyecto por su identificador único.
     *
     * @param id Identificador del proyecto.
     * @return Un Optional que contiene el proyecto si existe, o vacío si no se encuentra.
     */
    @Override
    public Optional<ProjectDto> findById(String id) {
        return projectRepository.findById(id);
    }

    /**
     * Recupera todos los proyectos registrados.
     *
     * @return Lista de todos los proyectos existentes representados como DTOs.
     */
    @Override
    public List<ProjectDto> findAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Cuenta la cantidad de proyectos que se encuentran en un estado específico.
     *
     * @param status Estado del proyecto como texto (debe coincidir con EnumProjectState).
     * @return Número de proyectos con el estado indicado.
     * @throws IllegalArgumentException Si el estado no corresponde con ningún valor del enum.
     */
    @Override
    public Long countByStatus(String status) {
        EnumProjectState enumStatus = EnumProjectState.valueOf(status.toUpperCase());
        return projectRepository.countByProState(enumStatus);
    }

    /**
     * Obtiene el número total de proyectos registrados, sin importar su estado.
     *
     * @return Número total de proyectos.
     */
    @Override
    public int countTotalProjects() {
        return projectRepository.count();
    }

    /**
     * Método consumidor de mensajes que escucha en la cola {@code CREATEPROJECT_QUEUE}.
     * Al recibir un proyecto, lo transforma desde su representación DTO al modelo de dominio
     * y lo guarda mediante el repositorio.
     *
     * Este método permite desacoplar la creación de proyectos en sistemas distribuidos,
     * respondiendo a eventos de creación emitidos por otros microservicios.
     *
     * @param project Proyecto recibido en forma de DTO desde la cola de RabbitMQ.
     */
    @Override
    @RabbitListener(queues = RabbitMQConfig.CREATEPROJECT_QUEUE)
    public void receiveProject(ProjectDto project) {
        System.out.println("Proyecto recibido: " + project.getProTitle());
        Project project1 = ProjectDtoMapper.projectToClass(project);
        projectRepository.save(project1);
    }
}
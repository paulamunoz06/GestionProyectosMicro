package co.edu.unicauca.microservicecoordinator.service;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.infra.exceptions.InvalidStateTransitionException;
import co.edu.unicauca.microservicecoordinator.repository.CoordinatorRepository;
import co.edu.unicauca.microservicecoordinator.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio que implementa la lógica de negocio para la coordinación de proyectos.
 *
 * <p>Esta clase proporciona métodos para gestionar el ciclo de vida de los proyectos,
 * principalmente las transiciones entre estados utilizando el patrón de diseño State.</p>
 */
@Service
public class CoordinatorService implements ICoordinatorService {

    /**
     * Repositorio para acceder a las operaciones específicas del coordinador.
     */
    @Autowired
    private CoordinatorRepository coordinatorRepository;

    /**
     * Repositorio para acceder a las operaciones de persistencia de proyectos.
     */
    @Autowired
    private IProjectRepository projectRepository;

    /**
     * Evalúa y actualiza el estado de un proyecto.
     *
     * <p>Este método implementa la lógica para transicionar un proyecto entre diferentes estados
     * utilizando el patrón State. Busca el proyecto por su ID, valida el nuevo estado solicitado,
     * y aplica la transición correspondiente utilizando el objeto de estado actual del proyecto.</p>
     *
     * @param proId        Identificador del proyecto a evaluar
     * @param proStatusStr Nuevo estado al que se desea transicionar el proyecto (como String)
     * @return El proyecto actualizado con su nuevo estado
     * @throws InvalidStateTransitionException Si el estado solicitado es inválido o si el proyecto no existe
     */
    @Override
    @Transactional
    public Project updateProject(String proId, String proStatusStr) {
        Optional<Project> optionalProject = projectRepository.findById(proId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.syncState(); // Sincroniza con el estado actual

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
            return project;
        } else {
            throw new InvalidStateTransitionException("Proyecto no encontrado con ID: " + proId);
        }
    }
}
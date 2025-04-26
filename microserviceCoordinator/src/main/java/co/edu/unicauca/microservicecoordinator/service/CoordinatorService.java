package co.edu.unicauca.microservicecoordinator.service;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.ProjectStatus;
import co.edu.unicauca.microservicecoordinator.repository.CoordinatorRepository;
import co.edu.unicauca.microservicecoordinator.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CoordinatorService implements ICoordinatorService{
    @Autowired
    private CoordinatorRepository coordinatorRepository;
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Actualiza el estado de un proyecto y lo actualiza en la base de datos.
     * @param proId - Identificador del proyecto.
     * @param proStatus - Estado del proyecto que se desea actualizar.
     * @return Project - Proyecto actualizado.
     */
    @Override
    public Project evaluateProject(Long proId, ProjectStatus proStatus) {
        Optional<Project> project = projectRepository.findById(proId);
        if(project.isPresent()){
            project.get().setProStatus(proStatus);
            projectRepository.save(project.get());
            return project.get();
        }else{
            return null;
        }
    }
    /**
     * Actualiza un proyecto con su nuevo estado en la base de datos y lo devuelve.
     * @param proId - Identificador del proyecto.
     * @param project - Proyecto con el estado actualizado.
     * @return Project - Proyecto actualizado.
     */
    /*
    @Override
    public Project evaluateProject(Long proId, Project project) {
        Optional<Project> project = projectRepository.findById(proId);
        if(project.isPresent()){
            projectRepository.save(project.get());
            return project.get();
        }else{
            return null;
        }
    }
     */

    //Agregar evaluación - cambio de estado

    //Hacer asignación

    //Listar los proyectos
}

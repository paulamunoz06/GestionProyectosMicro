package co.edu.unicauca.microservicecoordinator.service;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface IProjectRename {
    //Obtener proyecto por Id
    @Transactional
    Optional<Project> findById(Long id);

    //Obtener todos los proyectos
    @Transactional
    List<Project> findAllProjects();
}

package co.edu.unicauca.microservicecoordinator.repository;

import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProjectRepository extends JpaRepository<Project, String> {
    Long countByProState(EnumProjectState status);

}


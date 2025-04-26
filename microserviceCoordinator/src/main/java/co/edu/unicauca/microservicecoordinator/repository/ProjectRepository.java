package co.edu.unicauca.microservicecoordinator.repository;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

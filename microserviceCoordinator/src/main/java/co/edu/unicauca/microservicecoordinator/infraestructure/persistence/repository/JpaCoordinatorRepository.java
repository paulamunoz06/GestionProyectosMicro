package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository;

import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaCoordinatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad Coordinator
 *
 * Esta interfaz proporciona acceso a operaciones CRUD estándar para la entidad Coordinator
 * mediante la extensión de JpaRepository
 *
 */
public interface JpaCoordinatorRepository extends JpaRepository<JpaCoordinatorEntity, String> {
}
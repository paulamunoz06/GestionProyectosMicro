package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectId;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad Project
 *
 * Esta interfaz proporciona acceso a operaciones CRUD estándar para la entidad Project
 * mediante la extensión de JpaRepository.
 *
 * Además de los métodos heredados de JpaRepository, este repositorio ofrece
 * un conteo basado en el estado del proyecto
 */
public interface JpaProjectRepository extends JpaRepository<JpaProjectEntity, ProjectId> {

    /**
     * Cuenta el número de proyectos que tienen un estado específico.
     *
     * Este método utiliza la convención de nomenclatura de Spring Data JPA
     * para generar automáticamente una consulta que cuenta los registros
     * donde el campo proState coincide con el estado especificado.
     *
     * @param status El estado del proyecto ({@link EnumProjectState}) por el cual filtrar
     * @return El número de proyectos que tienen el estado especificado
     */
    Long countByProState(EnumProjectState status);
}
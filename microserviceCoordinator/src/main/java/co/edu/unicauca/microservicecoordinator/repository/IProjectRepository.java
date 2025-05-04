package co.edu.unicauca.microservicecoordinator.repository;

import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Project}.
 *
 * <p>Esta interfaz proporciona acceso a operaciones CRUD estándar para la entidad Project
 * mediante la extensión de JpaRepository. Hereda métodos como save, findById, findAll, delete, etc.,
 * que permiten manipular los datos de proyectos en la base de datos.</p>
 *
 * <p>Además de los métodos heredados de JpaRepository, este repositorio ofrece consultas
 * personalizadas para obtener información específica sobre los proyectos, como conteos
 * basados en el estado del proyecto.</p>
 *
 * @see Project
 * @see EnumProjectState
 * @see JpaRepository
 */
public interface IProjectRepository extends JpaRepository<Project, String> {

    /**
     * Cuenta el número de proyectos que tienen un estado específico.
     *
     * <p>Este método utiliza la convención de nomenclatura de Spring Data JPA
     * para generar automáticamente una consulta que cuenta los registros
     * donde el campo proState coincide con el estado especificado.</p>
     *
     * @param status El estado del proyecto ({@link EnumProjectState}) por el cual filtrar
     * @return El número de proyectos que tienen el estado especificado
     */
    Long countByProState(EnumProjectState status);

}
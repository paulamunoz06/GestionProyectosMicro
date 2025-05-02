package co.edu.unicauca.microservicecoordinator.repository;

import co.edu.unicauca.microservicecoordinator.entities.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad {@link Coordinator}.
 *
 * <p>Esta interfaz proporciona acceso a operaciones CRUD estándar para la entidad Coordinator
 * mediante la extensión de JpaRepository. Hereda métodos como save, findById, findAll, delete, etc.,
 * que permiten manipular los datos de coordinadores en la base de datos.</p>
 *
 * <p>Al extender de JpaRepository, se obtienen automáticamente las implementaciones
 * para las operaciones básicas de persistencia, así como características de paginación
 * y ordenamiento.</p>
 * @see Coordinator
 */
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {
    // Los métodos básicos de CRUD son heredados de JpaRepository

}
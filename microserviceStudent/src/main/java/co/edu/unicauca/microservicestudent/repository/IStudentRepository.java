package co.edu.unicauca.microservicestudent.repository;

import co.edu.unicauca.microservicestudent.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio que proporciona operaciones de acceso a datos para la entidad {@link Student}.
 */
@Repository
public interface IStudentRepository extends JpaRepository<Student, String> {
}

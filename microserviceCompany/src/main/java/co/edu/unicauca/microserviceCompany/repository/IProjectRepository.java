package co.edu.unicauca.microserviceCompany.repository;

import co.edu.unicauca.microserviceCompany.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio para gestionar las operaciones de la entidad Project en la base de datos.
 * Extiende JpaRepository para realizar operaciones CRUD básicas.
 */
public interface IProjectRepository extends JpaRepository<Project, String> {

    /**
     * Encuentra los proyectos disponibles para un estudiante. Un proyecto se considera disponible si
     * no ha sido postulada ni aprobado por el estudiante y su estado es 'ACEPTADO'.
     *
     * @param studentId Identificador único del estudiante.
     * @return Lista de proyectos disponibles para el estudiante.
     */
    @Query(value = """
    SELECT p.* FROM project p
    WHERE NOT EXISTS (
        SELECT 1 FROM postulated a WHERE a.pro_Id = p.proId AND a.student_Id = :studentId
    )
    AND NOT EXISTS (
        SELECT 1 FROM approved d WHERE d.pro_Id = p.proId AND d.student_Id = :studentId
    )
    AND p.proState = 'ACEPTADO'
    """, nativeQuery = true)
    List<Project> findAvailableProjectsForStudent(@Param("studentId") String studentId);

    /**
     * Cuenta el número total de proyectos registrados en la base de datos.
     *
     * @return Número total de proyectos.
     */
    @Query(value = """
    SELECT COUNT(p.proId) FROM project p
    """, nativeQuery = true)
    int countAllProjects();

    /**
     * Cuenta el número de proyectos en los que un estudiante ha postulado.
     *
     * @param studentId Identificador único del estudiante.
     * @return Número de proyectos postulados por el estudiante.
     */
    @Query(value = """
    SELECT COUNT(p.proId) FROM project p JOIN postulated a ON p.proId = a.pro_Id WHERE a.student_Id = :studentId
    """, nativeQuery = true)
    int countPostulatedProjects(@Param("studentId") String studentId);

    /**
     * Cuenta el número de proyectos que un estudiante ha aprobado.
     *
     * @param studentId Identificador único del estudiante.
     * @return Número de proyectos aprobados por el estudiante.
     */
    @Query(value = """
    SELECT COUNT(p.proId) FROM project p JOIN approved d ON p.proId = d.pro_Id WHERE d.student_Id = :studentId
    """, nativeQuery = true)
    int countApprovedProjects(@Param("studentId") String studentId);
}
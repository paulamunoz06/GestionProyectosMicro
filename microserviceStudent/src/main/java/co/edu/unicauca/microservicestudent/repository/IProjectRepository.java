package co.edu.unicauca.microservicestudent.repository;

import co.edu.unicauca.microservicestudent.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio que proporciona operaciones de acceso a datos para la entidad {@link Project}.
 */
@Repository
public interface IProjectRepository extends JpaRepository<Project, String> {

    /**
     * Busca los proyectos disponibles para un estudiante específico.
     *
     * Un proyecto se considera disponible si el estudiante no se ha postulado ni ha sido aprobado en él,
     * y además el estado del proyecto es 'ACEPTADO'.
     *
     * @param studentId ID del estudiante para el cual se consultan los proyectos disponibles.
     * @return Lista de proyectos disponibles para el estudiante.
     */
    @Query(value = """
        SELECT p.* FROM Project p
        WHERE NOT EXISTS (
            SELECT 1 FROM Postulated a WHERE a.pro_Id = p.proId AND a.student_Id = :studentId
        )
        AND NOT EXISTS (
            SELECT 1 FROM Approved d WHERE d.pro_Id = p.proId AND d.student_Id = :studentId
        )
        AND p.proState = 'ACEPTADO'
        """, nativeQuery = true)
    List<Project> findAvailableProjectsForStudent(@Param("studentId") String studentId);

    /**
     * Cuenta la cantidad total de proyectos registrados en la base de datos.
     *
     * @return Número total de proyectos.
     */
    @Query(value = """
        SELECT COUNT(p.proId) FROM Project p
        """, nativeQuery = true)
    int countAllProjects();

    /**
     * Cuenta la cantidad de proyectos en los que un estudiante se ha postulado.
     *
     * @param studentId ID del estudiante del cual se desea contar las postulaciones.
     * @return Número de proyectos en los que el estudiante se ha postulado.
     */
    @Query(value = """
        SELECT COUNT(p.proId) FROM Project p JOIN Postulated a ON p.proId = a.pro_Id WHERE a.student_Id = :studentId
        """, nativeQuery = true)
    int countPostulatedProjects(@Param("studentId") String studentId);

    /**
     * Cuenta la cantidad de proyectos en los que un estudiante ha sido aprobado.
     *
     * @param studentId ID del estudiante del cual se desea contar los proyectos aprobados.
     * @return Número de proyectos aprobados por el estudiante.
     */
    @Query(value = """
        SELECT COUNT(p.proId) FROM Project p JOIN Approved d ON p.proId = d.pro_Id WHERE d.student_Id = :studentId
        """, nativeQuery = true)
    int countApprovedProjects(@Param("studentId") String studentId);
}
